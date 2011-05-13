package net.tux.core.config;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EnvironmentAwarePropertyConfigurer extends PropertyPlaceholderConfigurer {
	
	private Properties props = EnvironmentAwareProperties.getInstance();

	protected Properties mergeProperties() {
		return props;
	}
}
