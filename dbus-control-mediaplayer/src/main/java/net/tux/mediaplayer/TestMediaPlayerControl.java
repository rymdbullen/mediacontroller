package net.tux.mediaplayer;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;

import net.tux.data.DBusMediaPlayer;
import net.tux.data.JSONMediaPlayerStatus;

public class TestMediaPlayerControl {

	public static void main(String[] args) throws JSONException {
		Map<String, JSONMediaPlayerStatus> jsonStatuses = DBusMediaPlayer.getJSONStatuses();
		Iterator<String> iter = jsonStatuses.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			JSONMediaPlayerStatus status = jsonStatuses.get(key);
			System.out.println("status: "+status.JSONStatus());
		}
		
		/*
		JSONMediaPlayerStatus jsonStatus = DBusMediaPlayer.getJSONStatus();
		if(jsonStatus==null) {
			System.out.println("not playing");
			DBusMediaPlayer.playPause();
			System.out.println("now it's playing");
		}
		jsonStatus = DBusMediaPlayer.getJSONStatus();
		if(jsonStatus!=null) {
			System.out.println(jsonStatus.JSONStatus().toString());
			System.out.println("is playing");
			DBusMediaPlayer.playPause();
			System.out.println("not playing anymore");
		}
		*/
	}
}