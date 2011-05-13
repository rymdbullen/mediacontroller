package net.tux.core.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class EnvironmentAwareProperties extends Properties {
	
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -5563564153639974606L;

	private final static Logger logger = Logger.getLogger(EnvironmentAwareProperties.class);
	
	/** the static singleton instance */
	private static EnvironmentAwareProperties environmentAwareProperties_s = null;
	
	private static final String CURRENTENV_PROPERTIES_FILE_NAME = "/allconfig-currentenv.properties";	

	private static final String ALLCONFIG_PROPERTIES_FILE_NAME = "/allconfig.properties";

	private static final String ALLCONFIG_LOCAL_PROPERTIES_FILE_NAME = "/allconfig-local.properties";

	private static final String ENVIRONMENT_PROPERTY_PREFIX = "environment";

	private static final String DEFAULT_ENVIRONMENT = "default";

	private static String currentEnvironmentName;

	private Map<String, EnvironmentConfig> environmentByName = new HashMap<String, EnvironmentConfig>();
	
	private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(.*?)?\\}");
	
	/**
	 * Default constructor
	 */
	protected EnvironmentAwareProperties() {
		// Exists only to defeat instantiation.
	}
	/**
	 * Returns the singleton property manager.
	 * @return the singleton property manager.
	 */
	public static EnvironmentAwareProperties getInstance() {
		if(environmentAwareProperties_s == null) {
			environmentAwareProperties_s = new EnvironmentAwareProperties();
			environmentAwareProperties_s.init();
		}
		return environmentAwareProperties_s;
	}
	
	protected void init() {
		// create a new instance
		Properties standardProps = getStandardProperties();

		// get current environment
		currentEnvironmentName = getCurrentEnvironmentName();
		
		setup(standardProps);
		
		if(null == getEnvironmentConfig(currentEnvironmentName, false)) {
			throw new IllegalArgumentException("Non Existing environment '"+currentEnvironmentName+"' specified in file: "+CURRENTENV_PROPERTIES_FILE_NAME);
		}
		if ("true".equals(getProperty("allow-local-override"))) {
			Properties localProps = getPropertiesFromClassPath(ALLCONFIG_LOCAL_PROPERTIES_FILE_NAME);
			setup(localProps);	
		}
	}
	private void setup(Properties standardProps) {
		for (Iterator<Map.Entry<Object,Object>> iter = standardProps.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Object,Object> entry = (Map.Entry<Object,Object>) iter.next();
			if (((String) entry.getKey()).startsWith(ENVIRONMENT_PROPERTY_PREFIX)) {
				//if(logger.isTraceEnabled()) { logger.trace("Parse Environment "+(String) entry.getValue()); };
				parseEnvironmentDefinition((String) entry.getValue());
			}
		}

		for (Iterator<Map.Entry<Object,Object>> iter = standardProps.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Object,Object> entry = (Map.Entry<Object,Object>) iter.next();
			if (!((String) entry.getKey()).startsWith(ENVIRONMENT_PROPERTY_PREFIX)) {
				parsePropertyDefinition((String) entry.getKey(), (String) entry.getValue());
			}
		}
	}
	
	public void logConfig() {		
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n*****************************************************************************\r\n");
		sb.append(addStarAtEnd("*"));
		sb.append(addStarAtEnd("*          EIPA INITIALIZING - '"+currentEnvironmentName+"'"));
		sb.append(addStarAtEnd("*"));
		sb.append(dumpCurrentConfig(true));
		sb.append(addStarAtEnd("*"));
		sb.append(addStarAtEnd("*          EIPA INITIALIZED  - '"+currentEnvironmentName+"'"));
		sb.append(addStarAtEnd("*"));
		sb.append("*****************************************************************************");
		
		logger.info(sb.toString());
	}
	
	public String getCurrentEnvironmentName() {
		if (currentEnvironmentName == null) {
			Properties currentEnvProps = getPropertiesFromClassPath(CURRENTENV_PROPERTIES_FILE_NAME);
			currentEnvironmentName = currentEnvProps.getProperty("environment");
			if(currentEnvironmentName==null) {
				currentEnvironmentName = DEFAULT_ENVIRONMENT;
			}
			if (System.getProperty("allconfig-currentenv-override") != null) {
				currentEnvironmentName = System.getProperty("allconfig-currentenv-override");
			}
		}
		
		return currentEnvironmentName;
	}

	private String addStarAtEnd(String debugString) {
		int length = debugString.length();
		int targetLength = 65;
		if(length>targetLength) {
			return debugString+"\r\n";
		}
		int diff = targetLength-length;
		int index = 0;
		StringBuilder sb = new StringBuilder(debugString);
		while (index<=diff) {
			index++;
			sb.append(" ");
		}
		sb.append("*");
		return sb.toString()+"\r\n";
	}
	private static Properties getPropertiesFromClassPath(String path) {
		Properties props = new Properties();
		InputStream inputStream = null;
		try {
			try {
				inputStream = EnvironmentAwareProperties.class.getResourceAsStream(path);
				if (inputStream != null) {
					props.load(inputStream);
				}

			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		} catch (IOException e) {
			System.err.println("Could not load '" + path + "' from classpath");
		}

		return props;
	}

	protected Properties getStandardProperties() {
		Properties propertiesFromClassPath = getPropertiesFromClassPath(ALLCONFIG_PROPERTIES_FILE_NAME);
		Properties overridePropertiesFromClassPath = getPropertiesFromClassPath(ALLCONFIG_LOCAL_PROPERTIES_FILE_NAME);
		propertiesFromClassPath.putAll(overridePropertiesFromClassPath);
		
		return propertiesFromClassPath;
	}

	private class EnvironmentConfig {
		private String name;

		private List<EnvironmentConfig> superConfigs = new ArrayList<EnvironmentConfig>();

		private Map<String, String> propertyValues = new HashMap<String, String>();

		public void addSuperConfig(EnvironmentConfig envConfig) {
			superConfigs.add(envConfig);
		}

		public void addProperty(String name, String value) {
			propertyValues.put(name, value);
		}

		/**
		 * Fetch a property using the current <em>environment</em> chain.
		 * 
		 * @param name
		 * @return The value of the property, trimmed, or <code>null</code> if
		 *         the property does not exist.
		 */
		public String getProperty(String name) {
			String value = (String) propertyValues.get(name);
			if (value != null) {
				return value.trim();
			}

			for (Iterator<EnvironmentConfig> iter = superConfigs.iterator(); iter.hasNext();) {
				EnvironmentConfig envConfig = (EnvironmentConfig) iter.next();
				value = envConfig.getProperty(name);
				if (value != null) {
					return value.trim();
				}
			}

			return null;
		}

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}

		public EnvironmentConfig(String name) {
			this.name = name;
		}

		private Set<String> _propNames = null;

		public Set<String> getAllPropertyNames() {			
			if (_propNames == null) {
				Set<String> temp = new HashSet<String>();
				
				temp.addAll(propertyValues.keySet());
				
				for (EnvironmentConfig superConfig : superConfigs) {				
					temp.addAll(superConfig.getAllPropertyNames());
				}
				
				_propNames = temp;
			}
			return _propNames;
		}
	}

	private boolean hasEnvironmentConfig(String name) {
		return environmentByName.containsKey(name);
	}

	private EnvironmentConfig getEnvironmentConfig(String name, boolean create) {
		EnvironmentConfig envConfig = (EnvironmentConfig) environmentByName.get(name);
		if (envConfig == null && create) {
			envConfig = new EnvironmentConfig(name);
			environmentByName.put(name, envConfig);
		}

		return envConfig;
	}

	private EnvironmentConfig getEnvironmentConfig(String name) {
		return getEnvironmentConfig(name, true);
	}

	private String cleanPropertyName(String propertyName) {
		StringTokenizer tokenizer = new StringTokenizer(propertyName, ". \t", true);
		StringBuffer cleanPropertyName = new StringBuffer();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			cleanPropertyName.append(token.trim());
		}

		return cleanPropertyName.toString();
	}

	private void parseEnvironmentDefinition(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, ":, \t");
		String environmentName = tokenizer.nextToken();

		EnvironmentConfig envConfig = getEnvironmentConfig(environmentName);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if(logger.isTraceEnabled()) { logger.trace("Environment: ["+envConfig.name+"] inherits ["+token+"]"); }
			envConfig.addSuperConfig(getEnvironmentConfig(token));
		}
	}

	private void parsePropertyDefinition(String name, String value) {
		name = cleanPropertyName(name);
		int firstDotIndex = name.indexOf(".");

		String environmentName = null;
		if (firstDotIndex != -1) {
			environmentName = name.substring(0, firstDotIndex);
		}
		if (environmentName != null && hasEnvironmentConfig(environmentName)) {
			String propertyName = name.substring(firstDotIndex + 1);
			value = filterValue(value);
			getEnvironmentConfig(environmentName).addProperty(propertyName, value);
		} else {
			logger.error("Property '" + name + "' lacks environment prefix, dropped");
			System.err.println("Property '" + name + "' lacks environment prefix, dropped");
		}
	}
	/**
	 * Filters placeholders with System properties.
	 * @param value the value to filter
	 * @return a filtered value 
	 */
	String filterValue(String value) {
		String thisValue = value;
		Matcher placeholderMatcher = PLACEHOLDER_PATTERN.matcher(thisValue);
		String replaceValue = null;
		String keyToReplace = null;
		String filteredValue = null;
		while (placeholderMatcher.find()) 
		{
			keyToReplace = placeholderMatcher.group(1);
			replaceValue = getStandardProperty(keyToReplace);
			if(replaceValue==null) {
				replaceValue = System.getProperty(keyToReplace);
			}
			if(replaceValue!=null) {
				filteredValue = thisValue.replaceAll("\\$\\{"+keyToReplace+"\\}", replaceValue);
				System.out.println("EnvironmentAwareProperties: Converted ["+thisValue+"] to ["+filteredValue+"]");
				thisValue = filteredValue;
			}
		}
		if(filteredValue!=null) {
			return filteredValue;
		}
		if(replaceValue==null && keyToReplace!=null) {
			System.err.println("EnvironmentAwareProperties: Failed to find value for placeholder ${"+keyToReplace+"} in ["+thisValue+"]");
		}
		return thisValue;
	}
	/**
	 * Returns the value from original StandardProperties for the supplied property name. 
     * <p>Tries to get property from already loaded properties and if that is unsuccessful then 
     * </p> 
	 * @param propertyName the property to value for
	 * @return the value for the supplied property name
	 */
	public String getStandardProperty(String propertyName) {
		String value = getStandardProperties().getProperty(propertyName);
		if(value==null) {
			value = getStandardProperties().getProperty(currentEnvironmentName+"."+propertyName);
			if(value==null) {
				value = getStandardProperties().getProperty(DEFAULT_ENVIRONMENT+"."+propertyName);
			}
			if(value!=null) {
				return value;
			}
		}
		return null;
	}
	/**
	 * Returns the value for the supplied property name
	 * @param propertyName the property to value for
	 * @return the value for the supplied property name
	 */
	public String getProperty(String propertyName) {
		String value = null;
		EnvironmentConfig config = getEnvironmentConfig(currentEnvironmentName, false);
		if (config == null) {
			throw new IllegalArgumentException("Non Existing environment '"+currentEnvironmentName+"' specified in file: "+CURRENTENV_PROPERTIES_FILE_NAME);
		} 
		value = config.getProperty(propertyName);
		return value;
	}
	/**
	 * Returns a string with all propertys and correlating values 
	 * @param useAsciiArt adds some fancy ascii art decorations
	 * @return a string with all propertys and correlating values
	 */
	public StringBuilder dumpCurrentConfig(boolean useAsciiArt) {
		StringBuilder sb = new StringBuilder();
		List<String> propertyNameList = getCurrentPropertyNamesSorted();
		for (int nameIdx = 0; nameIdx < propertyNameList.size(); nameIdx++) {
			String name = propertyNameList.get(nameIdx);
			String value = this.getProperty(name);
			String message;
			if(useAsciiArt) {
				message = "* "+name+":"+value;
				message = addStarAtEnd(message);
			} else {
				message = name+":"+value;
			}
			sb.append(message);
		}
		return sb;
	}
	/**
	 * Returns a sorted list of property names
	 * @return a sorted list of property names
	 */
	public List<String> getCurrentPropertyNamesSorted() {
		EnvironmentConfig config = getEnvironmentConfig(currentEnvironmentName, false);
		if(config==null) {
			throw new IllegalArgumentException("Failed to initiate '"+currentEnvironmentName+"'");
		}
		List<String> propertyNameList = new ArrayList<String>(config.getAllPropertyNames());
		Collections.sort(propertyNameList);
		return propertyNameList;
	}
	/**
	 * 
	 * @param writer
	 */
	public void dumpAllConfig(PrintWriter writer) {
		Set<String> allEnvNames = new HashSet<String>(environmentByName.keySet());
		Set<String> printedProperties = new HashSet<String>();
		try {
			InputStream inputStream = null;
			try {
				inputStream = EnvironmentAwareProperties.class.getResourceAsStream(ALLCONFIG_PROPERTIES_FILE_NAME);

				if (inputStream == null) {
					writer.write("Could not load '"
							+ ALLCONFIG_PROPERTIES_FILE_NAME
							+ "' from classpath. Missing?");
				} else {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));

					String configLine = reader.readLine();
					while (configLine != null) {
						configLine = configLine.trim();

						if (configLine.startsWith("#")
								|| configLine.length() == 0
								|| configLine.startsWith(ENVIRONMENT_PROPERTY_PREFIX)) {
							writer.write(configLine);
						} else {
							int firstDotIndex = configLine.indexOf(".");
							if (firstDotIndex == -1) {
								writer.write("### IGNORED (missing environment prefix): " + configLine);
							} else {
								String envName = configLine.substring(0,
										firstDotIndex);
								if (allEnvNames.contains(envName)) {
									StringTokenizer tokenizer = new StringTokenizer(
											configLine, "=");
									String propertyName = tokenizer.nextToken()
											.substring(firstDotIndex + 1)
											.trim();
									if (!printedProperties
											.contains(propertyName)) {
										writer.write(propertyName);
										writer.write("=");
										if (propertyName.toLowerCase().indexOf(
												"password") == -1) {
											writer.write(getProperty(propertyName));
										} else {
											writer.write("****");
										}
										printedProperties.add(propertyName);
									}
								} else {
									writer
											.write("### IGNORED (undeclared environment prefix): "
													+ configLine);
								}
							}
						}

						configLine = reader.readLine();
					}
				}

			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		} catch (IOException e) {
			writer.write("Could not load '" + ALLCONFIG_PROPERTIES_FILE_NAME
					+ "' from classpath: " + e.getClass().getName() + ": "
					+ e.getMessage());
		}
	}
	/**
	 * Fetches all property names for the current environment, recursively, from
	 * the environment itself, and all super configurations.
	 */
	public Enumeration<String> propertyNames() {
		EnvironmentConfig config = getEnvironmentConfig(currentEnvironmentName, false);
		if (config == null) {
			logger.warn("No properties for the current environment, " + currentEnvironmentName);
			return Collections.enumeration(Collections.<String>emptySet());
		}
		return Collections.enumeration(config.getAllPropertyNames());
	}

	public boolean containsKey(Object propertyName) {
		EnvironmentConfig config = getEnvironmentConfig(currentEnvironmentName,
				false);
		return config.getAllPropertyNames().contains(propertyName);
	}
}
