package net.tux.mediacontroller.action;

import java.util.Properties;

import net.tux.mediaplayer.data.DBusMediaPlayer;
import net.tux.mediaplayer.data.JSONMediaPlayerStatus;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class MediaPlayerActionBeanTest {
	Logger logger = Logger.getLogger(MediaPlayerActionBeanTest.class);
	
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
	public void testStatus() {
		String playerId = null;
		JSONMediaPlayerStatus jsonStatus = DBusMediaPlayer.getJSONStatus(playerId);
    	if(jsonStatus==null) {
    		System.out.println("no og");
    	}
    	System.out.println(jsonStatus.JSONStatus());
	}
}
