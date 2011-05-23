package net.tux.mediaplayer.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.tux.mediaplayer.config.Constants;

import org.apache.log4j.Logger;
import org.freedesktop.DBus;
import org.freedesktop.DBus.Error.ServiceUnknown;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

public class MediaPlayerStatus {
	private static Logger logger = Logger.getLogger(MediaPlayerStatus.class);
	private String id;
	@SuppressWarnings("rawtypes")
	private Map<String, Variant> metadata = new HashMap<String, Variant>(0);

	/**
	 * 
	 * @return
	 */
	public MediaPlayerStatus(String[] serviceBusNames) {
		for (int i = 0; i < serviceBusNames.length; i++) {
			String serviceBusName = serviceBusNames[i];
			populateMetadata(serviceBusName);
		}
	}
	/**
	 * 
	 * @return
	 */
	public MediaPlayerStatus(String serviceBusName) {
		populateMetadata(serviceBusName);
	}
	/**
	 * 
	 */
	private void populateMetadata(String serviceBusName) {
		String propertyName = "Metadata";
		
		// get all props and loop them
		Map<String, Variant> allProperties = getAllPropertyValues(serviceBusName);
		if(allProperties==null) {
			// failed to locate any properties
			return;
		}
		this.id = serviceBusName;
		Iterator<String> keySetIter = allProperties.keySet().iterator();
		while (keySetIter.hasNext()) {
			String key = (String) keySetIter.next();
			Object value = allProperties.get(key);
			
			if(value == null)
			{
				logger.debug("Failed to obtain "+propertyName + " for " + serviceBusName);
				logger.debug("value is " + value + ": for " + serviceBusName);
				value = "<unknown>";
			}
			if(value instanceof Map)
			{
				if(Constants.DEBUG) 
				{
					// iterate the values in the map
					// debug code, printing all properties
					Iterator<String> iter = ((Map)value).keySet().iterator();
					while (iter.hasNext()) 
					{
						String thisKey = (String) iter.next();
						Object thisValue = ((Map)value).get(key);
						logger.debug(thisKey+" "+thisValue);
					}
				}
			} 
			else 
			{
				if(Constants.DEBUG)
				{	
					logger.debug(key+" "+value);
				}
			}
			this.metadata.put(key, (Variant)value);
		}
	}
	/**
	 * 
	 * @param serviceBusName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, Variant> getAllPropertyValues(String serviceBusName) {
		if(serviceBusName==null) {
			throw new IllegalArgumentException("serviceBusName must not be null");
		}
		DBusConnection conn = null;
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			DBus.Properties props = conn.getRemoteObject(serviceBusName, Constants.DBUS_OBJECT_PATH_ORG_MPRIS_MEDIAPLAYER2, DBus.Properties.class);

			Map<String, Variant> allProperties = props.GetAll(Constants.DBUS_INTERFACE_NAME_ORG_MPRIS_MEDIAPLAYER);
			return allProperties;
		} catch (ServiceUnknown e) {
			logger.debug("Service Unknown for: " + serviceBusName);
		} catch (DBusException ex) {
			logger.debug("DBusException for: " + serviceBusName);
		} finally {
			if(conn==null) {
				logger.debug("no DBUS connection for: " + serviceBusName);
				return null;
			}
			conn.disconnect();
		}
		logger.debug("failed to get info for: " + serviceBusName);
		return null;
	}
	/**
	 * 
	 * @param serviceBusName
	 * @param propertyName
	 * @return
	 */
	private Variant<Object> getPropertyValue(String propertyName) {
		return metadata.get(propertyName);
	}
	/**
	 * 
	 * @param serviceBusName
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Object getPropertyValue(String serviceBusName, String propertyName) {
		Map<String, Variant> allProperties = getAllPropertyValues(serviceBusName);
		Variant property = allProperties.get(propertyName);
		
		return property.getValue();
	}
	public String getId() {
		return id;
	}
	public boolean isPlaying() {
		return getPlaybackStatus().contains("Playing");
	}
	
	public String getPlaybackStatus() {
		Variant<Object> playBackStatusObject = getPropertyValue("PlaybackStatus");
		
		if(playBackStatusObject==null) {
			return "notRunning";
		}
		return playBackStatusObject.toString();
	}
	public Map<String, Variant> getMetadata() {
		return metadata;
	}
	
}
