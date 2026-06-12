package utils;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for reading configuration properties from config.properties file.
 * Simplifies access to configuration values throughout the test suite.
 */
public class ConfigReader {
    private static final Logger logger = Logger.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        loadProperties();
    }

    /**
     * Load properties from config file
     */
    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fileInputStream);
            logger.info("Configuration loaded successfully from: " + CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Error loading configuration file: " + e.getMessage());
        }
    }

    /**
     * Get property value by key
     *
     * @param key The property key
     * @return The property value, or null if not found
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: " + key);
        }
        return value;
    }

    /**
     * Get property value by key with default value
     *
     * @param key          The property key
     * @param defaultValue Default value if key not found
     * @return The property value, or defaultValue if not found
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get workflow API root URL
     *
     * @return Workflow API root URL
     */
    public static String getWorkflowAPIRoot() {
        return getProperty("workflow_api_root");
    }

    /**
     * Get data API root URL
     *
     * @return Data API root URL
     */
    public static String getDataAPIRoot() {
        return getProperty("data_api_root");
    }

    /**
     * Get application development URL
     *
     * @return Application development URL
     */
    public static String getApplicationDevURL() {
        return getProperty("application_dev_url");
    }

    /**
     * Get API authentication token
     *
     * @return API authentication token
     */
    public static String getAPIToken() {
        return getProperty("api_token");
    }

    /**
     * Get timeout value in milliseconds
     *
     * @return Timeout value, defaults to 5000ms if not configured
     */
    public static int getTimeoutInMillis() {
        String timeout = getProperty("timeout_ms", "5000");
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            logger.warn("Invalid timeout value, using default: 5000ms");
            return 5000;
        }
    }

    /**
     * Check if a property exists
     *
     * @param key The property key
     * @return true if property exists, false otherwise
     */
    public static boolean propertyExists(String key) {
        return properties.containsKey(key);
    }

    /**
     * Get all properties
     *
     * @return Properties object
     */
    public static Properties getAllProperties() {
        return properties;
    }

    /**
     * Reload properties from file
     */
    public static void reloadProperties() {
        loadProperties();
        logger.info("Configuration reloaded");
    }
}
