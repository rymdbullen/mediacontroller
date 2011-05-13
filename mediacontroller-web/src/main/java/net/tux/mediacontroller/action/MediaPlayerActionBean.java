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
import net.tux.data.DBusMediaPlayer;
import net.tux.data.JSONMediaPlayerStatus;

import java.io.StringReader;
import java.util.List;

import org.json.JSONException;

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
    private ActionBeanContext context;

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

    /** Handles the 'divide' event, divides number two by oneand returns the result. */
    @DefaultHandler 
    public Resolution status() {
        String jsonText = getStatus();
		return new StreamingResolution("text", new StringReader(jsonText));
    }

    /** Handles the 'add' event, adds the two numbers and returns the result. */
    public Resolution playPause() {
    	String jsonText = DBusMediaPlayer.playPause().JSONStatus();
        return new StreamingResolution("text", new StringReader(jsonText));
    }

    private String getStatus() {
		JSONMediaPlayerStatus jsonStatus = DBusMediaPlayer.getJSONStatus();
		if(jsonStatus==null) {
			System.out.println("no og");
			return "{\"playerId\", \"no player\"}";
		}
		return jsonStatus.JSONStatus().toString();
	}
}
