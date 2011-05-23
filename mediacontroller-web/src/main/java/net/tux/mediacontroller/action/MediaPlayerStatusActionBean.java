package net.tux.mediacontroller.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.tux.mediaplayer.config.Constants;
import net.tux.mediaplayer.data.DBusMediaPlayer;
import net.tux.mediaplayer.data.MediaPlayerStatus;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A very simple calculator action that is designed to work with an ajax front end.
 * Handles 'add' and 'divide' events just like the non-ajax calculator. Each event
 * calculates the result, and then "streams" it back to the browser. Implements the
 * ValidationErrorHandler interface to intercept any validation errors, convert them
 * to an HTML message and stream the back to the browser for display.
 *
 * @author Tim Fennell
 */
@UrlBinding("/action/MediaPlayer.action")
public class MediaPlayerStatusActionBean implements ActionBean, ValidationErrorHandler {
	private static Logger logger = Logger.getLogger(MediaPlayerStatusActionBean.class);
    private ActionBeanContext context;
    @Validate(required=true) private String activePlayerId;
    private ArrayList<String> availablePlayerIds = new ArrayList<String>(0);

    public ActionBeanContext getContext() { return context; }
    public void setContext(ActionBeanContext context) { this.context = context; }

    /** Converts errors to HTML and streams them back to the browser. */
    public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
        StringBuilder message = new StringBuilder();

        for (List<ValidationError> fieldErrors : errors.values()) {
            for (ValidationError error : fieldErrors) {
                message.append("<div style=\"color: firebrick;\">");
                message.append(error.getMessage(getContext().getLocale()));
                message.append("</div>");
            }
        }

        return new StreamingResolution("text/html", new StringReader(message.toString()));
    }

    /** Handles the 'status' event and returns the result. */
    @DefaultHandler 
    public Resolution getActivePlayers() {
        String jsonText = getJSONStatuses();
		return new StreamingResolution("text", new StringReader(jsonText));
    }

    private String getJSONStatuses() {
		Map<String, MediaPlayerStatus> statuses = DBusMediaPlayer.getStatuses();
		if(statuses==null) {
			logger.debug("no og");
			return "{\"playerId\", \"no player\"}";
		}
		JSONObject jsonObj = new JSONObject();
		String key = null;
		try {
			// create a JSONObject to hold relevant info for each item in cart and
			// stuff all of these objects in a JSONArray
	
			boolean isPlaying = false;
			JSONArray itemList = new JSONArray();
			Iterator<String> keySetIter = statuses.keySet().iterator();
			while (keySetIter.hasNext()) {
				key = (String) keySetIter.next();
				MediaPlayerStatus status = statuses.get(key);
				JSONObject ci = new JSONObject();
				ci.put("playerId", status.getId());
				ci.put("playing", status.isPlaying());
				isPlaying = status.isPlaying() || isPlaying;
				if(isPlaying) {
					activePlayerId = key;
				}
				if(Constants.DEBUG) {
					logger.debug("playerId: " + status.getId() + ", isPlaying: " + isPlaying);
				}
				itemList.put(ci);
				availablePlayerIds.add(status.getId());
			}
			// create a JSONObject to hold relevant info for the status of the player
			jsonObj.put("numPlayers", statuses.keySet().size());
			jsonObj.put("isPlaying", isPlaying);
			jsonObj.put("itemList", itemList);
		} catch (JSONException e) {
			logger.debug("Failed to create JSON object for: " + key);
		}
		// {"numPlayers": 2, "playing" : true, "itemList": [{"playerId" : "player1", "playing" : false}, {"playerId" : "player2", "playing" : true}]}
		return jsonObj.toString();
	}
    
    // Standard getter and setter methods
	public String getActivePlayerId() {
		return activePlayerId;
	}
	public void setActivePlayerId(String activePlayerId) {
		this.activePlayerId = activePlayerId;
	}
	public ArrayList<String> getAvailablePlayerIds() {
		return availablePlayerIds;
	}
	public void setAvailablePlayerIds(ArrayList<String> availablePlayerIds) {
		this.availablePlayerIds = availablePlayerIds;
	}
}
