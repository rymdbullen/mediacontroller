package net.tux.mediaplayer.data;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSONMediaPlayerStatusTest {
	Logger logger = Logger.getLogger(JSONMediaPlayerStatusTest.class);
	
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
	public void testGetNowPlaying() {
		Map<String, JSONMediaPlayerStatus> jsonStatuses = DBusMediaPlayer.getJSONStatuses();
		Iterator<String> iter = jsonStatuses.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			JSONMediaPlayerStatus status = jsonStatuses.get(key);
			logger.debug("Status: "+status.JSONStatus());
			logger.debug("NowPlaying: "+status.getPlayerStatus().getNowPlaying());
		}
	}
}
