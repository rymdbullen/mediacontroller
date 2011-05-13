package net.tux.mediaplayer;

import org.json.JSONException;

import net.tux.data.DBusMediaPlayer;
import net.tux.data.JSONMediaPlayerStatus;

public class MediaPlayerControl {

	private static boolean debug = false;
	
	public static void main(String[] args) throws JSONException {	
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
	}
}