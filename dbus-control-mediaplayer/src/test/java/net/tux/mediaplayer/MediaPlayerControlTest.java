package net.tux.mediaplayer;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import net.tux.mediaplayer.config.Constants;
import net.tux.mediaplayer.data.DBusMediaPlayer;
import net.tux.mediaplayer.data.JSONMediaPlayerStatus;

public class MediaPlayerControlTest {
	Logger logger = Logger.getLogger(MediaPlayerControlTest.class);
	
    @BeforeClass
    public static void setUp() {
        Properties props = new Properties();
        props.put("log4j.appender.unit-test", "org.apache.log4j.ConsoleAppender");
        props.put("log4j.appender.unit-test.layout", "org.apache.log4j.PatternLayout");
        props.put("log4j.appender.unit-test.layout.ConversionPattern", "%-5p %c{2} - %m%n");
        props.put("log4j.rootLogger", "DEBUG, unit-test");

        PropertyConfigurator.configure(props);
    }
    @Test
	public void testGetJSONStatuses() {
		Map<String, JSONMediaPlayerStatus> jsonStatuses = DBusMediaPlayer.getJSONStatuses();
		Iterator<String> iter = jsonStatuses.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			JSONMediaPlayerStatus status = jsonStatuses.get(key);
			logger.debug("status: "+status.JSONStatus());
		}
	}
/*
    @Test
    public void testGetJSONStatus() {
    	String serviceBusName = Constants.DBUS_SERVICE_BUS_NAME_CLEMENTINE;
    	JSONMediaPlayerStatus jsonStatus = DBusMediaPlayer.getJSONStatus(serviceBusName);
    	logger.debug("status: "+jsonStatus.JSONStatus());
    	serviceBusName = Constants.DBUS_SERVICE_BUS_NAME_SPOTIFY;
    	jsonStatus = DBusMediaPlayer.getJSONStatus(serviceBusName);
    	logger.debug("status: "+jsonStatus.JSONStatus());
    }
    */
    /*
    @Test
    public void testStartClementine() {
    	DBusMediaPlayer.playPause(Constants.DBUS_SERVICE_BUS_NAME_CLEMENTINE);
    }
 */  
}