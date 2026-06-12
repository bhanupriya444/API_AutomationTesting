package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Assured API Client for making HTTP requests and validating responses.
 * Provides methods for GET, POST, PUT, DELETE, and PATCH operations.
 */
public class RestAssuredClient {
    private static final Logger logger = Logger.getLogger(RestAssuredClient.class);
    private String baseURI;
    private RequestSpecification requestSpec;
    private Map<String, String> headers;

    /**
     * Constructor to initialize REST Assured Client with base URI
     *
     * @param baseURI The base URL for API requests
     */
    public RestAssuredClient(String baseURI) {
        this.baseURI = baseURI;
        this.headers = new HashMap<>();
        initializeRequestSpec();
    }

    /**
     * Initialize request specification with base URI
     */
    private void initializeRequestSpec() {
        RestAssured.baseURI = baseURI;
        requestSpec = RestAssured.given().baseUri(baseURI);
        logger.info("REST Assured client initialized with base URI: " + baseURI);
    }

    /**
     * Add a header to the request
     *
     * @param key   Header key
     * @param value Header value
     */
    public RestAssuredClient addHeader(String key, String value) {
        headers.put(key, value);
        requestSpec = requestSpec.header(key, value);
        logger.debug("Header added: " + key + " = " + value);
        return this;
    }

    /**
     * Add multiple headers at once
     *
     * @param headersMap Map of headers
     */
    public RestAssuredClient addHeaders(Map<String, String> headersMap) {
        headersMap.forEach((key, value) -> {
            headers.put(key, value);
            requestSpec = requestSpec.header(key, value);
        });
        logger.debug("Multiple headers added");
        return this;
    }

    /**
     * Add authorization token (Bearer token)
     *
     * @param token The authorization token
     */
    public RestAssuredClient addAuthToken(String token) {
        requestSpec = requestSpec.header("Authorization", "Bearer " + token);
        logger.debug("Bearer token added");
        return this;
    }

    /**
     * Add request body as JSON
     *
     * @param body The request body as string
     */
    public RestAssuredClient setBody(String body) {
        requestSpec = requestSpec.body(body);
        logger.debug("Request body set");
        return this;
    }

    /**
     * Add request body as object (will be serialized to JSON)
     *
     * @param body The request body as object
     */
    public RestAssuredClient setBody(Object body) {
        requestSpec = requestSpec.body(body);
        logger.debug("Request body set from object");
        return this;
    }

    /**
     * Perform GET request
     *
     * @param endpoint The API endpoint path
     * @return Response object
     */
    public Response get(String endpoint) {
        logger.info("Performing GET request to: " + endpoint);
        Response response = requestSpec.when().get(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform POST request
     *
     * @param endpoint The API endpoint path
     * @return Response object
     */
    public Response post(String endpoint) {
        logger.info("Performing POST request to: " + endpoint);
        Response response = requestSpec.when().post(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform PUT request
     *
     * @param endpoint The API endpoint path
     * @return Response object
     */
    public Response put(String endpoint) {
        logger.info("Performing PUT request to: " + endpoint);
        Response response = requestSpec.when().put(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform DELETE request
     *
     * @param endpoint The API endpoint path
     * @return Response object
     */
    public Response delete(String endpoint) {
        logger.info("Performing DELETE request to: " + endpoint);
        Response response = requestSpec.when().delete(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform PATCH request
     *
     * @param endpoint The API endpoint path
     * @return Response object
     */
    public Response patch(String endpoint) {
        logger.info("Performing PATCH request to: " + endpoint);
        Response response = requestSpec.when().patch(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Reset the request specification for next request
     */
    public void reset() {
        headers.clear();
        initializeRequestSpec();
        logger.debug("Request specification reset");
    }

    /**
     * Log response details
     *
     * @param response The response object
     */
    private void logResponse(Response response) {
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.debug("Response Body: " + response.getBody().asString());
    }

    /**
     * Get the current headers map
     *
     * @return Map of headers
     */
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    /**
     * Get base URI
     *
     * @return Base URI
     */
    public String getBaseURI() {
        return baseURI;
    }
}
