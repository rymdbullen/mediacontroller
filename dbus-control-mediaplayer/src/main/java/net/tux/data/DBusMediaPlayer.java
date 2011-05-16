package net.tux.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.tux.config.Constants;

import org.freedesktop.DBus;
import org.freedesktop.DBus.Error.ServiceUnknown;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MediaPlayer2.Player;

public class DBusMediaPlayer {
	private String playerId;
	private String playerTune;
	private static String currentServiceBusName = null;
	
	private ArrayList<String> activePlayers = new ArrayList<String>();
	
	private static Player playerSpotify;
	private static Player player;
	private static String[] playerIds = new String[] {Constants.DBUS_SERVICE_BUS_NAME_CLEMENTINE, Constants.DBUS_SERVICE_BUS_NAME_SPOTIFY};;
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	public static ArrayList<Player> syncronExecuteCommand(String command) {
		
		DBusConnection conn = null;
		ArrayList<Player> activePlayers = new ArrayList<Player>();
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			player = conn.getRemoteObject(Constants.DBUS_SERVICE_BUS_NAME_CLEMENTINE, Constants.DBUS_OBJECT_PATH_ORG_MPRIS_MEDIAPLAYER2, Player.class);
			if(player != null) {
				System.out.println("clementine active");
				activePlayers.add(player);
			}
			playerSpotify = conn.getRemoteObject(Constants.DBUS_SERVICE_BUS_NAME_SPOTIFY, Constants.DBUS_OBJECT_PATH_ORG_MPRIS_MEDIAPLAYER2, Player.class);
			if(playerSpotify != null) {
				System.out.println("spotify active");
				activePlayers.add(playerSpotify);
			}
			
			executeCommand(activePlayers, command);
			
		} catch (DBusException ex) {
			ex.printStackTrace();
		} finally {
			if(conn==null) {
				System.exit(1);
			}
			conn.disconnect();
		}
		return activePlayers;
	}
	/**
	 * 
	 * @param activePlayers
	 * @param command
	 */
	private static void executeCommand(ArrayList<Player> activePlayers, String command) {
		Iterator<Player> playerIter = activePlayers.iterator();
		while (playerIter.hasNext()) {
			Player player = (Player) playerIter.next();			
			if (command.equals("playpause")) 
			{
				player.PlayPause();
			} 
			//else if (command.equals("stop")&&running) 
			else if (command.equals("stop")) 
			{
				player.Stop();
			}
			//else if (command.equals("play")&&!running) 
			else if (command.equals("play")) 
			{
				player.Play();
			} 
			//else if (command.equals("next")&&running) 
			else if (command.equals("next")) 
			{
				player.Next();
			} 
			//else if (command.equals("previous")&&running) 
			else if (command.equals("previous")) 
			{
				player.Previous();
			} 
			else 
			{
				// do nothing
			}		
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static Map<String, JSONMediaPlayerStatus> getJSONStatuses() {
		
		HashMap<String, JSONMediaPlayerStatus> getJSONStatuses = new HashMap<String, JSONMediaPlayerStatus>(0);
		
		for (int i = 0; i < playerIds.length; i++) {
			String playerId = playerIds[i];
			MediaPlayerStatus playerStatus = new MediaPlayerStatus(playerId);
//			JSONMediaPlayerStatus getJSONStatus = new JSONMediaPlayerStatus(playerStatus);
			JSONMediaPlayerStatus getJSONStatus = getJSONStatus();
			if(getJSONStatus!=null) {
				getJSONStatuses.put(playerId, getJSONStatus);
			}
		}
		if(getJSONStatuses.keySet().size()==0) {
			System.out.println("Failed to locate any players");
		}
		return getJSONStatuses;
	}
		
	/**
	 * 
	 * @return
	 * @deprecated use getJSONStatuses
	 */
	public static JSONMediaPlayerStatus getJSONStatus() {
		String propertyName = "Metadata";
		Object value = null;
		
		currentServiceBusName = Constants.DBUS_SERVICE_BUS_NAME_CLEMENTINE;
		value = getPropertyValue(currentServiceBusName, propertyName);
		if(value == null) 
		{
			System.out.println("failed to locate clementine");
			currentServiceBusName = Constants.DBUS_SERVICE_BUS_NAME_SPOTIFY;
			value = getPropertyValue(currentServiceBusName, propertyName);
			if(value == null) 
			{
				System.out.println("failed to locate spotify");
				System.out.println("no players found");
				currentServiceBusName = null;
				return null;
			}
		}
		
		if(value instanceof Map) {
			Map<String, Variant> allMetadata = (Map<String, Variant>) value;
			if(allMetadata.keySet().size()==0)
			{
				System.out.println("currently player not playing");
				return null;
			}
			else
			{
				JSONMediaPlayerStatus status = new JSONMediaPlayerStatus(currentServiceBusName, allMetadata);
				return status;
			}
		}
		
		return null;
	}
	/**
	 * 
	 * @param serviceBusName
	 * @param propertyName
	 * @return
	 */
	private static Object getPropertyValue(String serviceBusName, String propertyName) {
		DBusConnection conn = null;
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			DBus.Properties props = conn.getRemoteObject(serviceBusName, Constants.DBUS_OBJECT_PATH_ORG_MPRIS_MEDIAPLAYER2, DBus.Properties.class);

			Map<String, Variant> allProperties = props.GetAll(Constants.DBUS_INTERFACE_NAME_ORG_MPRIS_MEDIAPLAYER);
			Variant property = allProperties.get(propertyName);
			
			return property.getValue();
		} catch (ServiceUnknown e) {
			e.printStackTrace();
		} catch (DBusException ex) {
			ex.printStackTrace();
		} finally {
			if(conn==null) {
				return null;
			}
			conn.disconnect();
		}
		return null;
	}

	public static JSONMediaPlayerStatus next() {
		syncronExecuteCommand("next");
		return getJSONStatus();
	}

	public static JSONMediaPlayerStatus previous() {
		syncronExecuteCommand("previous");
		return getJSONStatus();
	}

	public static JSONMediaPlayerStatus pause() {
		syncronExecuteCommand("pause");
		return getJSONStatus();
	}

	public static JSONMediaPlayerStatus playPause() {
		syncronExecuteCommand("playpause");
		return getJSONStatus();
	}

	public static JSONMediaPlayerStatus stop() {
		syncronExecuteCommand("stop");
		return getJSONStatus();
	}

	public static JSONMediaPlayerStatus play() {
		syncronExecuteCommand("play");
		return getJSONStatus();
	}

	public static void seek(long offset) {
		//syncronExecuteCommand("seek", offset);
	}

	public static void setPosition(DBusInterface TrackId, long Position) {
		//syncronExecuteCommand("seek", offset);
		player.SetPosition(TrackId, Position);
	}

	public static void openUri(String Uri) {
		player.OpenUri(Uri);
	}
	
	public String getPlayerId() {
		return currentServiceBusName;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerTune() {
		return playerTune;
	}

	public void setPlayerTune(String playerTune) {
		this.playerTune = playerTune;
	}

	public boolean isPlayerRunning() {
		return playerTune == null || playerTune.equals("");
	}

	public String getId() {
		return this.playerId;
	}	
}
