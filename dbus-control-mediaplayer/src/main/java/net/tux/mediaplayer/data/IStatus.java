package net.tux.mediaplayer.data;

import java.util.ArrayList;

public abstract class IStatus {
	private ArrayList<String> activePlayers = new ArrayList<String>();
	private String id = null;
	private String nowPlayingUrl = null;
	private String nowPlaying = null;
	private String playbackStatus = null;

	public boolean isPlaying() {
		return nowPlaying != null || false == nowPlaying.equals("");
	}

	public ArrayList<String> getActivePlayers() {
		return activePlayers;
	}

	public void setActivePlayers(ArrayList<String> activePlayers) {
		this.activePlayers = activePlayers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNowPlayingUrl() {
		return nowPlayingUrl;
	}

	public void setNowPlayingUrl(String nowPlayingUrl) {
		this.nowPlayingUrl = nowPlayingUrl;
	}

	public String getNowPlaying() {
		return nowPlaying;
	}

	public void setNowPlaying(String nowPlaying) {
		this.nowPlaying = nowPlaying;
	}

	public String getPlaybackStatus() {
		return playbackStatus;
	}

	public void setPlaybackStatus(String playbackStatus) {
		this.playbackStatus = playbackStatus;
	}
}
