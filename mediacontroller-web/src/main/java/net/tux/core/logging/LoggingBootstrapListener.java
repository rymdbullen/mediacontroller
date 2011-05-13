package net.tux.core.logging;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

import net.tux.core.config.EnvironmentAwareProperties;

/**
 * Logging Bootstrap listener initializes a bridge for JULI to log4j by 
 * removing all java.util.logging handlers for root logger and replacing it with {@link JuliToLog4jHandler}s. 
 *
 * <p>This listener should be registered as first listener as it initializes log4j</p>
 *
 * @author epatgot - initial version
 * @author estenja - added setting path, appserver.home.dir
 * 
 */
public class LoggingBootstrapListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		// sets the log related properties, ie catalina home is root directory and log prefix
		//
        setupAppServerProperties();
		setupLogPrefixProperties(sce);
        
		PropertyConfigurator.configure(EnvironmentAwareProperties.getInstance());
		
		java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		rootLogger.addHandler(new JuliToLog4jHandler());
		
		EnvironmentAwareProperties.getInstance().logConfig();
	}
	/**
	 * Setup of properties needed for log file directories
	 */
	private void setupAppServerProperties() {
		String appserverHome = System.getProperty("catalina.home");
        if(appserverHome != null) {
        	System.setProperty("appserver.home.dir", appserverHome.replace('\\', '/'));
        	System.setProperty("appserver.temp.dir", System.getProperty("java.io.tmpdir").replace('\\', '/'));
        } else {
        	System.setProperty("appserver.home.dir", "./target");
        	System.setProperty("appserver.temp.dir", "./target");
        }
	}
	/**
	 * Setup of properties needed for log file directories
	 * @param sce
	 */
	private void setupLogPrefixProperties(ServletContextEvent sce) {
		Object ctxTempdir = sce.getServletContext().getAttribute("javax.servlet.context.tempdir");
        String tempDir = "";
        if(ctxTempdir instanceof File) {
        	File temp = (File)ctxTempdir;
        	tempDir = temp.getName();
        }
        
        System.setProperty("log-prefix", "");
//        if(tempDir.equals("_") || tempDir.equals("ROOT")) {          // web  application
//        	System.setProperty("log-prefix", "");
//        } else if(tempDir.equals("m")) {                             // mobi application
//        	System.setProperty("log-prefix", "m-");
//        } else {
//        	throw new IllegalArgumentException("LoggingBootstrapListener: Could not get application type from: "+ctxTempdir);
//        }
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {		
	}
}
