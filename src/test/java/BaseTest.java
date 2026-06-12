package utils;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;

/**
 * Base Test Class for API test cases.
 * Provides common setup and utility methods for all API test classes.
 * Handles client initialization and common configurations.
 */
public class BaseTest {
    protected static final Logger logger = Logger.getLogger(BaseTest.class);

    protected RestAssuredClient restAssuredClient;
    protected HttpClientManager httpClientManager;
    protected ConfigReader configReader;
    protected JSONParser jsonParser;
    protected APIResponseValidator responseValidator;

    /**
     * Initialize common resources for test execution
     */
    @BeforeClass
    public void setUp() {
        logger.info("========== Setting up test environment ==========");

        // Initialize configuration reader
        configReader = new ConfigReader();

        // Get API base URL from configuration
        String apiBaseURL = configReader.getWorkflowAPIRoot();
        if (apiBaseURL == null) {
            logger.error("API base URL not configured in config.properties");
            throw new RuntimeException("API base URL is required for API testing");
        }

        // Initialize REST Assured client
        restAssuredClient = new RestAssuredClient(apiBaseURL);
        restAssuredClient.addHeader("Content-Type", "application/json");

        // Initialize HttpClient manager
        httpClientManager = new HttpClientManager(apiBaseURL);
        httpClientManager.addDefaultHeader("Content-Type", "application/json");

        // Add API token if configured
        String apiToken = configReader.getAPIToken();
        if (apiToken != null && !apiToken.isEmpty()) {
            restAssuredClient.addAuthToken(apiToken);
            httpClientManager.addDefaultHeader("Authorization", "Bearer " + apiToken);
            logger.info("API token configured for authentication");
        }

        // Initialize utility classes
        jsonParser = new JSONParser();
        responseValidator = new APIResponseValidator();

        logger.info("Test setup completed successfully");
    }

    /**
     * Get REST Assured client
     *
     * @return RestAssuredClient instance
     */
    public RestAssuredClient getRestAssuredClient() {
        return restAssuredClient;
    }

    /**
     * Get HttpClient manager
     *
     * @return HttpClientManager instance
     */
    public HttpClientManager getHttpClientManager() {
        return httpClientManager;
    }

    /**
     * Get configuration reader
     *
     * @return ConfigReader instance
     */
    public ConfigReader getConfigReader() {
        return configReader;
    }

    /**
     * Get JSON parser
     *
     * @return JSONParser instance
     */
    public JSONParser getJsonParser() {
        return jsonParser;
    }

    /**
     * Get API response validator
     *
     * @return APIResponseValidator instance
     */
    public APIResponseValidator getResponseValidator() {
        return responseValidator;
    }

    /**
     * Log a test step
     *
     * @param stepDescription Description of the test step
     */
    protected void logStep(String stepDescription) {
        logger.info("STEP: " + stepDescription);
    }

    /**
     * Log test result
     *
     * @param testName The test name
     * @param passed   Whether the test passed
     */
    protected void logTestResult(String testName, boolean passed) {
        String status = passed ? "PASSED" : "FAILED";
        logger.info("Test [" + testName + "] " + status);
    }

    /**
     * Print response details (for debugging)
     *
     * @param responseBody The response body to print
     */
    protected void printResponse(String responseBody) {
        logger.info("===== Response Details =====");
        logger.info(jsonParser.prettyPrintJSON(responseBody));
        logger.info("=============================");
    }

    /**
     * Get timeout value from configuration
     *
     * @return Timeout in milliseconds
     */
    protected int getTimeoutInMillis() {
        return configReader.getTimeoutInMillis();
    }

    /**
     * Create a new request builder
     *
     * @return RequestBuilder instance
     */
    protected RequestBuilder createRequestBuilder() {
        return new RequestBuilder();
    }
}
