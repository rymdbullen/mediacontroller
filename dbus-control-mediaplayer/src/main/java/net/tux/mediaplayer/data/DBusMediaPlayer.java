package net.tux.mediaplayer.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.tux.mediaplayer.config.Constants;

import org.apache.log4j.Logger;
import org.freedesktop.DBus;
import org.freedesktop.DBus.Error.NoReply;
import org.freedesktop.DBus.Error.ServiceUnknown;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MediaPlayer2.Player;

public class DBusMediaPlayer {
	private static Logger logger = Logger.getLogger(DBusMediaPlayer.class);
	private String playerId;
	private String playerTune;
	private static String currentServiceBusName = null;
	
	/**
	 * Default constructor
	 */
	public DBusMediaPlayer() {
		
	}
	public DBusMediaPlayer(String serviceBusName) {
		currentServiceBusName = serviceBusName;
	}

	/**
	 * 
	 * @param playerId
	 * @param command
	 * @return
	 */
	public static MediaPlayerStatus syncronExecuteCommand(String playerId, String command) {
		DBusConnection conn = null;
		Map<String, MediaPlayerStatus> statuses = getStatuses();
		
		Iterator<String> keySetIter = statuses.keySet().iterator();
		while (keySetIter.hasNext()) {
			String key = (String) keySetIter.next();
			if(playerId != null && !playerId.equals(playerId)) {
				continue;
			}
			try {
				// MediaPlayerStatus status = statuses.get(key);
				// if(status.isPlaying()) {
				conn = DBusConnection.getConnection(DBusConnection.SESSION);
				Player player = conn.getRemoteObject(key, Constants.DBUS_OBJECT_PATH_ORG_MPRIS_MEDIAPLAYER2, Player.class);
				if(player != null) {
					logger.debug("player active: "+key);
					executeCommand(player, command);
				}
				//}
			} catch (DBusException ex) {
				logger.warn("DbusException for connection");
			} finally {
				if(conn==null) {
					logger.warn("No Dbus Connection to close");
				}
				conn.disconnect();
			}
		}
		return getStatus(playerId);
	}
	/**
	 * 
	 * @param activePlayers
	 * @param command
	 */
	private static void executeCommand(Player activePlayer, String command) {
		try {
			
			if (command.equals("playpause")) 
			{
				activePlayer.PlayPause();
			} 
			//else if (command.equals("stop")&&running) 
			else if (command.equals("stop")) 
			{
				activePlayer.Stop();
			}
			//else if (command.equals("play")&&!running) 
			else if (command.equals("play")) 
			{
				activePlayer.Play();
			} 
			//else if (command.equals("next")&&running) 
			else if (command.equals("next")) 
			{
				activePlayer.Next();
			} 
			//else if (command.equals("previous")&&running) 
			else if (command.equals("previous")) 
			{
				activePlayer.Previous();
			} 
			else 
			{
				// do nothing
			}		
		} catch (NoReply e) {
			logger.error("No reply within specified time", e);
		}
	}
	/**
	 * 
	 * @param activePlayers
	 * @param command
	 */
	private static void executeCommand(ArrayList<Player> activePlayers, String command) {
		Iterator<Player> playerIter = activePlayers.iterator();
		while (playerIter.hasNext()) {
			Player activePlayer = (Player) playerIter.next();
			executeCommand(activePlayer, command);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static Map<String, MediaPlayerStatus> getStatuses() {
		HashMap<String, MediaPlayerStatus> statuses = new HashMap<String, MediaPlayerStatus>(0);
		
		for (int i = 0; i < Constants.PLAYER_IDS.length; i++) 
		{
			String thisPlayerId = Constants.PLAYER_IDS[i];
			MediaPlayerStatus getJSONStatus = getStatus(thisPlayerId);
			if(getJSONStatus != null) {
				if( getJSONStatus.getMetadata().size()>0) {
					statuses.put(thisPlayerId, getJSONStatus);
				}
			}
		}
		if(statuses.keySet().size()==0) 
		{
			logger.debug("Failed to locate any players");
		}
		return statuses;
	}
	/**
	 * 
	 * @return
	 */
	public static Map<String, JSONMediaPlayerStatus> getJSONStatuses() {
		HashMap<String, JSONMediaPlayerStatus> getJSONStatuses = new HashMap<String, JSONMediaPlayerStatus>(0);
		Map<String, MediaPlayerStatus> statuses = getStatuses();
		
		Iterator<String> keySetIter = statuses.keySet().iterator();
		while (keySetIter.hasNext()) {
			String key = (String) keySetIter.next();
			getJSONStatuses.put(key, new JSONMediaPlayerStatus(statuses.get(key)));
		}
		if(getJSONStatuses.keySet().size()==0) 
		{
			logger.debug("Failed to locate any players");
		}
		return getJSONStatuses;
	}
	
	/**
	 * 
	 * @return
	 */
	public static JSONMediaPlayerStatus getJSONStatus() {
		return getJSONStatus(currentServiceBusName);
	}
	/**
	 * 
	 * @return
	 */
	public static MediaPlayerStatus getStatus() {
		return getStatus(currentServiceBusName);
	}
	/**
	 * 
	 * @return
	 */
	public static MediaPlayerStatus getStatus(String playerId) {
		MediaPlayerStatus playerStatus = new MediaPlayerStatus(playerId);
		return playerStatus;
	}
	/**
	 * 
	 * @return
	 */
	public static MediaPlayerStatus getStatus(String[] playerIds) {
		MediaPlayerStatus playerStatus = new MediaPlayerStatus(playerIds);
		return playerStatus;
	}
	/**
	 * 
	 * @return
	 */
	public static JSONMediaPlayerStatus getJSONStatus(String playerId) {
		if(playerId==null) {
			JSONMediaPlayerStatus getJSONStatus = new JSONMediaPlayerStatus(getStatus(Constants.PLAYER_IDS));
			return getJSONStatus;
		}
		JSONMediaPlayerStatus getJSONStatus = new JSONMediaPlayerStatus(getStatus(playerId));
		return getJSONStatus;
	}
	
	public static MediaPlayerStatus next(String playerId) {
		return syncronExecuteCommand(playerId, "next");
	}

	public static MediaPlayerStatus previous(String playerId) {
		return syncronExecuteCommand(playerId, "previous");
	}

	public static MediaPlayerStatus pause(String playerId) {
		return syncronExecuteCommand(playerId, "pause");
	}

	public static MediaPlayerStatus playPause(String playerId) {
		return syncronExecuteCommand(playerId, "playpause");
	}

	public static MediaPlayerStatus stop(String playerId) {
		return syncronExecuteCommand(playerId, "stop");
	}

	public static MediaPlayerStatus play(String playerId) {
		return syncronExecuteCommand(playerId, "play");
	}

/*	
	public static Map<String, MediaPlayerStatus> next(String playerId) {
		return syncronExecuteCommand(playerId, "next");
	}

	public static Map<String, MediaPlayerStatus> previous(String playerId) {
		return syncronExecuteCommand(playerId, "previous");
	}

	public static Map<String, MediaPlayerStatus> pause(String playerId) {
		return syncronExecuteCommand(playerId, "pause");
	}

	public static Map<String, MediaPlayerStatus> playPause(String playerId) {
		return syncronExecuteCommand(playerId, "playpause");
	}

	public static Map<String, MediaPlayerStatus> stop(String playerId) {
		return syncronExecuteCommand(playerId, "stop");
	}

	public static Map<String, MediaPlayerStatus> play(String playerId) {
		return syncronExecuteCommand(playerId, "play");
	}
*/
	public static void seek(long offset) {
		//return syncronExecuteCommand("seek", offset);
	}

	public static void setPosition(DBusInterface TrackId, long Position) {
		//syncronExecuteCommand("seek", offset);
		//player.SetPosition(TrackId, Position);
	}

	public static void openUri(String Uri) {
		//player.OpenUri(Uri);
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
	
	public static String getJSONPlayers() {
		String playerIds = null;
		Map<String, MediaPlayerStatus> statuses = getStatuses();
		
		Iterator<String> keySetIter = statuses.keySet().iterator();
		while (keySetIter.hasNext()) {
			String playerId = (String) keySetIter.next();
			if(playerId != null && !playerId.equals(playerId)) {
				continue;
			}
			MediaPlayerStatus status = statuses.get(playerId);
			if(status.isPlaying()) {
				
			}
		}
		return playerIds;
	}	
}
