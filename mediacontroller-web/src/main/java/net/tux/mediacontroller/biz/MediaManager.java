package net.tux.mediacontroller.biz;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import net.tux.mediaplayer.data.DBusMediaPlayer;

/**
 * Maintains an in memory list of players in the system.
 *
 * @author Tim Fennell
 */
public class MediaManager {
    /** Sequence number to use for generating IDs. */
    private static int idSequence = 0;

    /** Storage for all known players. */
    private static Map<Integer,DBusMediaPlayer> players = new TreeMap<Integer,DBusMediaPlayer>();

    static {
        PlayerManager pm = new PlayerManager();

        pm.getPlayers();
    }

    /** Gets the bug with the corresponding ID, or null if it does not exist. */
    public DBusMediaPlayer getPlayer(int id) {
        return players.get(id);
    }

    /** Returns a sorted list of all bugs in the system. */
    public List<DBusMediaPlayer> getAllPlayers() {
        return Collections.unmodifiableList( new ArrayList<DBusMediaPlayer>(players.values()) );
    }
}
