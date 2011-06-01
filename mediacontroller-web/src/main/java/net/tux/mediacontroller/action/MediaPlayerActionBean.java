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
import net.tux.mediaplayer.data.DBusMediaPlayer;
import net.tux.mediaplayer.data.JSONMediaPlayerStatus;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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
public class MediaPlayerActionBean implements ActionBean, ValidationErrorHandler {
	private static Logger logger = Logger.getLogger(MediaPlayerActionBean.class);
    private ActionBeanContext context;
    @Validate(required=true) private String playerId;

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
    public Resolution status() {
    	JSONMediaPlayerStatus jsonStatus = DBusMediaPlayer.getJSONStatus(playerId);
        String jsonText = jsonStatus.JSONStatus().toString();
		return new StreamingResolution("text", new StringReader(jsonText));
    }

    /** Handles the 'playPause' event and returns the result. */
    public Resolution playPause() {
    	JSONMediaPlayerStatus status = new JSONMediaPlayerStatus(DBusMediaPlayer.playPause(playerId));
		String jsonText = status.JSONStatus();
    	return new StreamingResolution("text", new StringReader(jsonText));
    }

    /** Handles the 'play' event and returns the result. */
    public Resolution play() {
    	JSONMediaPlayerStatus status = new JSONMediaPlayerStatus(DBusMediaPlayer.play(playerId));
		String jsonText = status.JSONStatus();
    	return new StreamingResolution("text", new StringReader(jsonText));
    }
    
    /** Handles the 'stop' event and returns the result. */
    public Resolution stop() {
    	JSONMediaPlayerStatus status = new JSONMediaPlayerStatus(DBusMediaPlayer.stop(playerId));
		String jsonText = status.JSONStatus();
    	return new StreamingResolution("text", new StringReader(jsonText));
    }
    
    /** Handles the 'next' event and returns the result. */
    public Resolution next() {
    	JSONMediaPlayerStatus status = new JSONMediaPlayerStatus(DBusMediaPlayer.next(playerId));
		String jsonText = status.JSONStatus();
    	return new StreamingResolution("text", new StringReader(jsonText));    
    }
    
    /** Handles the 'previous' event and returns the result. */
    public Resolution previous() {
    	JSONMediaPlayerStatus status = new JSONMediaPlayerStatus(DBusMediaPlayer.previous(playerId));
		String jsonText = status.JSONStatus();
    	return new StreamingResolution("text", new StringReader(jsonText));    
    }
    // Standard getter and setter methods
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { logger.info(playerId); this.playerId = playerId; }
}
