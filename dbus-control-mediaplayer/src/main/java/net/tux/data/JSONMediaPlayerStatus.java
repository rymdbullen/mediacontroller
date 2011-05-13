package net.tux.data;

import java.util.Iterator;
import java.util.Map;

import org.freedesktop.dbus.Variant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMediaPlayerStatus {
	//private DBusMediaPlayer player;
	private String id;
	private Map<String, Variant> metadata;

	public JSONMediaPlayerStatus(/*DBusMediaPlayer player*/ String id, Map<String, Variant> metadata) {
		//this.player = player;
		this.id = id;
		this.metadata = metadata;
	}

	public String JSONStatus() throws JSONException {

		// create a JSONObject to hold relevant info for each item in cart and
		// stuff all of these objects in a JSONArray
		JSONArray itemList = new JSONArray();
		Iterator<String> iter = metadata.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Object thisValue = metadata.get(key);
			System.out.println(key + " " + thisValue);

			JSONObject ci = new JSONObject();
			ci.put(key, thisValue);
			itemList.put(ci);
		}

		// create a JSONObject to hold relevant info for the status of the player
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("playerId", id);
//		jsonObj.put("playerId", player.getId());
//		jsonObj.put("playerRunning", player.isPlayerRunning());
		jsonObj.put("itemList", itemList);

		// return the object as a JSON String
		return jsonObj.toString();
	}
}
