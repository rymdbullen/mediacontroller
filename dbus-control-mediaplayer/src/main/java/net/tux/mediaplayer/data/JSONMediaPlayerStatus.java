package net.tux.mediaplayer.data;

import java.util.Iterator;
import java.util.Map;

import net.tux.mediaplayer.config.Constants;

import org.apache.log4j.Logger;
import org.freedesktop.dbus.Variant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMediaPlayerStatus {
	Logger logger = Logger.getLogger(JSONMediaPlayerStatus.class);
	
	private String id;
	private Map<String, Variant> metadata;

	public JSONMediaPlayerStatus(String id, Map<String, Variant> metadata) {
		this.id = id;
		this.metadata = metadata;
	}

	public JSONMediaPlayerStatus(MediaPlayerStatus playerStatus) {
		this.id = playerStatus.getId();
		this.metadata = playerStatus.getMetadata();
	}

	public JSONMediaPlayerStatus(Map<String, MediaPlayerStatus> status) {
		Iterator<String> keySetIter = status.keySet().iterator();
		
	}

	public String JSONStatus() {

		JSONObject jsonObj = new JSONObject();
		try {
			// create a JSONObject to hold relevant info for each item in cart and
			// stuff all of these objects in a JSONArray
			JSONArray itemList = new JSONArray();
			Iterator<String> iter = metadata.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object thisValue = metadata.get(key);
				if(Constants.DEBUG)
				{
					logger.debug(key + " " + thisValue);
				}
				JSONObject ci = new JSONObject();
				ci.put(key, thisValue);
				itemList.put(ci);
			}
	
			// create a JSONObject to hold relevant info for the status of the player
			jsonObj.put("playerId", id);
			jsonObj.put("itemList", itemList);
		} catch (JSONException e) {
			logger.debug("Failed to create JSON object for: "+id+ ": "+metadata);
		}

		// return the object as a JSON String
		return jsonObj.toString();
	}

	public String getId() {
		return id;
	}

	public Map<String, Variant> getMetadata() {
		return metadata;
	}
}
