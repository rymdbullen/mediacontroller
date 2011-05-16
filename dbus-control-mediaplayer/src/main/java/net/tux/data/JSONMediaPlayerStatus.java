package net.tux.data;

import java.util.Iterator;
import java.util.Map;

import net.tux.config.Constants;

import org.freedesktop.dbus.Variant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMediaPlayerStatus {
	private String id;
	private Map<String, Variant> metadata;

	public JSONMediaPlayerStatus(String id, Map<String, Variant> metadata) {
		this.id = id;
		this.metadata = metadata;
	}

	public JSONMediaPlayerStatus(MediaPlayerStatus playerStatus) {
		// TODO Auto-generated constructor stub
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
					System.out.println(key + " " + thisValue);
				}
				JSONObject ci = new JSONObject();
				ci.put(key, thisValue);
				itemList.put(ci);
			}
	
			// create a JSONObject to hold relevant info for the status of the player
			jsonObj.put("playerId", id);
			jsonObj.put("itemList", itemList);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// return the object as a JSON String
		return jsonObj.toString();
	}
}
