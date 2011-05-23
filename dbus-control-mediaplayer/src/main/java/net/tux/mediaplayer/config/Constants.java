package net.tux.mediaplayer.config;

public class Constants {
	public static final String DBUS_SERVICE_BUS_NAME_SPOTIFY = "org.mpris.MediaPlayer2.spotify";
	public static final String DBUS_SERVICE_BUS_NAME_CLEMENTINE = "org.mpris.MediaPlayer2.clementine";
	public static String[] PLAYER_IDS = new String[] {DBUS_SERVICE_BUS_NAME_SPOTIFY, DBUS_SERVICE_BUS_NAME_CLEMENTINE};
	public static final String DBUS_INTERFACE_NAME_ORG_MPRIS_MEDIAPLAYER = "org.mpris.MediaPlayer2.Player";
	public static final String DBUS_OBJECT_PATH_ORG_MPRIS_MEDIAPLAYER2 = "/org/mpris/MediaPlayer2";
	public static final boolean DEBUG = false;
}
