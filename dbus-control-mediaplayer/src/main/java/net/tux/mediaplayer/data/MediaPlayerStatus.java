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

public class MediaPlayerStatus extends IStatus {
	private static Logger logger = Logger.getLogger(MediaPlayerStatus.class);
	
	@SuppressWarnings("rawtypes")
	private Map<String, Variant> metadata = new HashMap<String, Variant>(0);

	/**
	 * Default constructor
	 */
	public MediaPlayerStatus() {}
	/**
	 * 
	 * @return
	 */
	public MediaPlayerStatus(String[] serviceBusNames) {
		for (int i = 0; i < serviceBusNames.length; i++) {
			String serviceBusName = serviceBusNames[i];
			populateMetadata(serviceBusName);
			extractNowPlaying();
			extractPlayBackStatus();
		}
	}
	/**
	 * 
	 * @return
	 */
	public MediaPlayerStatus(String serviceBusName) {
		populateMetadata(serviceBusName);
		extractNowPlaying();
		extractPlayBackStatus();
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
		this.setId(serviceBusName);
		Iterator<String> keySetIter = allProperties.keySet().iterator();
		while (keySetIter.hasNext()) {
			String key = (String) keySetIter.next();
			Object value = allProperties.get(key);
			
			if(value == null)
			{
				logger.debug("Failed to obtain " + propertyName + " for " + serviceBusName);
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
	private void extractPlayBackStatus() {
		Object thisValue = metadata.get("PlaybackStatus");
		if(thisValue==null) {
			// not playing
			return;
		}
		if(false == thisValue instanceof Variant) {
			// not playing
			return;
		}
		Variant thisMetadata = (Variant)thisValue; //new HashMap<String, Variant>();
		if(false == thisMetadata.getValue() instanceof String) {
			// not playing
			return;
		}
		this.setPlaybackStatus((String)thisMetadata.getValue());
		
	}
	String extractNowPlaying() {
		Object metadataValue = metadata.get("Metadata");
		if(metadataValue==null) {
			// not playing
			return null;
		}
		if(false == metadataValue instanceof Variant) {
			// not playing
			return null;
		}
		Variant thisMetadata = (Variant)metadataValue; //new HashMap<String, Variant>();
		
		Object value = thisMetadata.getValue();
		if(value instanceof Map) {
			Iterator<String> iter = ((Map)value).keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object thisValue = ((Map)value).get(key);
				if(Constants.DEBUG)
				{
					logger.debug(key + " " + thisValue);
				}
				logger.debug(key + " " + thisValue);
				Variant thisValue1 = (Variant)thisValue;
				if(key.equals("xesam:title")) {
					if(thisValue1.getValue() instanceof String) {
						this.setNowPlaying((String)thisValue1.getValue());
					}
				}
				else if(key.equals("xesam:url")) 
				{
					if(thisValue1.getValue() instanceof String) {
						this.setNowPlayingUrl((String)thisValue1.getValue());
					}
				}
				else if(key.equals("xesam:url")) 
				{
					if(thisValue1.getValue() instanceof String) {
						this.setNowPlayingUrl((String)thisValue1.getValue());
					}
				}
			}
		}
		return null;
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
			if(logger.isInfoEnabled()) {
				logger.info("          got info for: " + serviceBusName);
			}
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
		if(logger.isInfoEnabled()) {
			logger.info("failed to get info for: " + serviceBusName);
		}
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
	public Map<String, Variant> getMetadata() {
		return metadata;
	}
	
}
