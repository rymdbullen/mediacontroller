package net.tux.mediacontroller.action;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class MediaPlayerStatusActionBeanTest {
	Logger logger = Logger.getLogger(MediaPlayerStatusActionBeanTest.class);
	
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
	public void testGetActivePlayers() {
		MediaPlayerStatusActionBean bean = new MediaPlayerStatusActionBean();
		bean.getActivePlayers();
		//logger.debug(bean.getActivePlayers());
		logger.debug("Player Playing: "+bean.getActivePlayerId());
		ArrayList<String> availablePlayerIds = bean.getAvailablePlayerIds();
		for (int i = 0; i < availablePlayerIds.size(); i++) {
			String availablePlayerId = availablePlayerIds.get(i);			
			logger.debug("ActivePlayer: "+availablePlayerId);
		}
	}
}
