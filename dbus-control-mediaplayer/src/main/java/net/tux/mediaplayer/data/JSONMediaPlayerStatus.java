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
	
	private MediaPlayerStatus playerStatus;
	
	@SuppressWarnings("rawtypes")
	public JSONMediaPlayerStatus(String id, Map<String, Variant> metadata) {
		this.playerStatus = new MediaPlayerStatus(id);
	}

	public JSONMediaPlayerStatus(MediaPlayerStatus playerStatus) {
		this.playerStatus = playerStatus;
	}

	public String JSONStatus() {

		JSONObject jsonObj = new JSONObject();
		try {
			// create a JSONObject to hold relevant info for each item in cart and
			// stuff all of these objects in a JSONArray
			JSONArray itemList = new JSONArray();
			Iterator<String> iter = this.playerStatus.getMetadata().keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object thisValue = this.playerStatus.getMetadata().get(key);
				if(Constants.DEBUG)
				{
					logger.debug(key + " " + thisValue);
				}
				JSONObject ci = new JSONObject();
				ci.put(key, thisValue);
				itemList.put(ci);
			}
	
			// create a JSONObject to hold relevant info for the status of the player
			jsonObj.put("playerId", this.playerStatus.getId());
			jsonObj.put("nowPlaying", this.playerStatus.getNowPlaying());
			jsonObj.put("itemList", itemList);
		} catch (JSONException e) {
			logger.debug("Failed to create JSON object for: " + this.playerStatus.getId() + ": "+this.playerStatus.getMetadata());
		}

		// return the object as a JSON String
		return jsonObj.toString();
	}

	public MediaPlayerStatus getPlayerStatus() {
		return playerStatus;
	}
}
