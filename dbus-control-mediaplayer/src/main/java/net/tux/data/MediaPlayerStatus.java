package net.tux.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.tux.config.Constants;

import org.freedesktop.DBus;
import org.freedesktop.DBus.Error.ServiceUnknown;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

public class MediaPlayerStatus {
	
	@SuppressWarnings("rawtypes")
	private Map<String, Variant> metadata = new HashMap<String, Variant>(0);

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public MediaPlayerStatus(String serviceBusName) {
		String propertyName = "Metadata";
		
		// get all props and loop them
		Map<String, Variant> allProperties = getAllPropertyValues(serviceBusName);
		if(allProperties==null) {
			// failed to locate any properties
			return;
		}
		Iterator<String> keySetIter = allProperties.keySet().iterator();
		while (keySetIter.hasNext()) {
			String key = (String) keySetIter.next();
			Object value = allProperties.get(key);
			
			if(value == null) 
			{
				System.out.println("Failed to obtain "+propertyName + " for " + serviceBusName);
				System.out.println("value is " + value + ": for " + serviceBusName);
				value = "<unknown>";
			}
			if(value instanceof Map && Constants.DEBUG) {
				// iterate the values in the map
				// debug code, printing all properties
				Iterator<String> iter = ((Map)value).keySet().iterator();
				while (iter.hasNext()) {
					String thisKey = (String) iter.next();
					Object thisValue = ((Map)value).get(key);
					System.out.println(thisKey+" "+thisValue);
				}
			} else {
				System.out.println(key+" "+value);
			}
			metadata.put(key, (Variant)value);

		}
		/*
		Object value = getPropertyValue(serviceBusName, propertyName);
		if(value == null || false == value instanceof Map) 
		{
			System.out.println("Failed to obtain "+propertyName + " for " + serviceBusName);
			System.out.println("value is " + value + ": for " + serviceBusName);
//			return;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Variant> allMetadata = (Map<String, Variant>) value;
		if(allMetadata.keySet().size()==0)
		{
			System.out.println("no metadata available for: "+serviceBusName);
			//return;
		} else {			
			metadata = allMetadata;
		}
		
		// check if player is available at all
		propertyName = "PlaybackStatus";
		value = getPropertyValue(serviceBusName, propertyName);
		if(value != null) 
		{
			System.out.println("player state: "+value);
			metadata.put(propertyName, (Variant)value);
		}

		if(Constants.DEBUG) 
		{
			// debug code, printing all properties
			Iterator<String> iter = allMetadata.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object thisValue = allMetadata.get(key);
				System.out.println(key+" "+thisValue);
			}
		}
		*/
	}
	/**
	 * 
	 * @param serviceBusName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, Variant> getAllPropertyValues(String serviceBusName) {
		DBusConnection conn = null;
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			DBus.Properties props = conn.getRemoteObject(serviceBusName, Constants.DBUS_OBJECT_PATH_ORG_MPRIS_MEDIAPLAYER2, DBus.Properties.class);

			Map<String, Variant> allProperties = props.GetAll(Constants.DBUS_INTERFACE_NAME_ORG_MPRIS_MEDIAPLAYER);
			return allProperties;
		} catch (ServiceUnknown e) {
			e.printStackTrace();
		} catch (DBusException ex) {
			ex.printStackTrace();
		} finally {
			if(conn==null) {
				System.out.println("no DBUS connection for " + serviceBusName);
				return null;
			}
			conn.disconnect();
		}
		System.out.println("failed to locate " + serviceBusName);
		return null;
	}
	/**
	 * 
	 * @param serviceBusName
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Object getPropertyValue(String serviceBusName, String propertyName) {
		Map<String, Variant> allProperties = getAllPropertyValues(serviceBusName);
		Variant property = allProperties.get(propertyName);
		
		return property.getValue();
	}
}
