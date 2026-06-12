package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient Manager for making HTTP requests using Apache HttpClient.
 * Provides a wrapper around HttpClient for easier API testing.
 */
public class HttpClientManager {
    private static final Logger logger = Logger.getLogger(HttpClientManager.class);
    private HttpClient httpClient;
    private String baseURL;
    private Map<String, String> defaultHeaders;

    /**
     * Constructor to initialize HttpClientManager
     *
     * @param baseURL The base URL for API requests
     */
    public HttpClientManager(String baseURL) {
        this.baseURL = baseURL;
        this.httpClient = HttpClientBuilder.create().build();
        this.defaultHeaders = new HashMap<>();
        logger.info("HttpClientManager initialized with base URL: " + baseURL);
    }

    /**
     * Add a default header that will be included in all requests
     *
     * @param key   Header key
     * @param value Header value
     */
    public void addDefaultHeader(String key, String value) {
        defaultHeaders.put(key, value);
        logger.debug("Default header added: " + key + " = " + value);
    }

    /**
     * Add multiple default headers
     *
     * @param headers Map of headers
     */
    public void addDefaultHeaders(Map<String, String> headers) {
        defaultHeaders.putAll(headers);
        logger.debug("Multiple default headers added");
    }

    /**
     * Perform GET request
     *
     * @param endpoint The endpoint path
     * @return HttpResponse object
     * @throws IOException if an I/O error occurs
     */
    public HttpResponse get(String endpoint) throws IOException {
        String url = buildURL(endpoint);
        HttpGet getRequest = new HttpGet(url);
        applyHeaders(getRequest);
        logger.info("Performing GET request to: " + url);
        return executeRequest(getRequest);
    }

    /**
     * Perform GET request with query parameters
     *
     * @param endpoint The endpoint path
     * @param params   Query parameters as Map
     * @return HttpResponse object
     * @throws IOException if an I/O error occurs
     */
    public HttpResponse getWithParams(String endpoint, Map<String, String> params) throws IOException {
        String url = buildURL(endpoint) + buildQueryString(params);
        HttpGet getRequest = new HttpGet(url);
        applyHeaders(getRequest);
        logger.info("Performing GET request to: " + url);
        return executeRequest(getRequest);
    }

    /**
     * Perform POST request
     *
     * @param endpoint The endpoint path
     * @param body     The request body
     * @return HttpResponse object
     * @throws IOException if an I/O error occurs
     */
    public HttpResponse post(String endpoint, String body) throws IOException {
        String url = buildURL(endpoint);
        HttpPost postRequest = new HttpPost(url);
        applyHeaders(postRequest);
        postRequest.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        logger.info("Performing POST request to: " + url);
        return executeRequest(postRequest);
    }

    /**
     * Perform PUT request
     *
     * @param endpoint The endpoint path
     * @param body     The request body
     * @return HttpResponse object
     * @throws IOException if an I/O error occurs
     */
    public HttpResponse put(String endpoint, String body) throws IOException {
        String url = buildURL(endpoint);
        HttpPut putRequest = new HttpPut(url);
        applyHeaders(putRequest);
        putRequest.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        logger.info("Performing PUT request to: " + url);
        return executeRequest(putRequest);
    }

    /**
     * Perform DELETE request
     *
     * @param endpoint The endpoint path
     * @return HttpResponse object
     * @throws IOException if an I/O error occurs
     */
    public HttpResponse delete(String endpoint) throws IOException {
        String url = buildURL(endpoint);
        HttpDelete deleteRequest = new HttpDelete(url);
        applyHeaders(deleteRequest);
        logger.info("Performing DELETE request to: " + url);
        return executeRequest(deleteRequest);
    }

    /**
     * Perform PATCH request
     *
     * @param endpoint The endpoint path
     * @param body     The request body
     * @return HttpResponse object
     * @throws IOException if an I/O error occurs
     */
    public HttpResponse patch(String endpoint, String body) throws IOException {
        String url = buildURL(endpoint);
        HttpPatch patchRequest = new HttpPatch(url);
        applyHeaders(patchRequest);
        patchRequest.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        logger.info("Performing PATCH request to: " + url);
        return executeRequest(patchRequest);
    }

    /**
     * Execute the HTTP request
     *
     * @param request The HTTP request
     * @return HttpResponse object
     * @throws IOException if an I/O error occurs
     */
    private HttpResponse executeRequest(HttpRequestBase request) throws IOException {
        HttpResponse response = httpClient.execute(request);
        logger.info("Response Status Code: " + response.getStatusLine().getStatusCode());
        return response;
    }

    /**
     * Apply default headers to the request
     *
     * @param request The HTTP request
     */
    private void applyHeaders(HttpRequestBase request) {
        defaultHeaders.forEach((key, value) -> request.setHeader(key, value));
    }

    /**
     * Build the complete URL
     *
     * @param endpoint The endpoint path
     * @return Complete URL
     */
    private String buildURL(String endpoint) {
        if (endpoint.startsWith("/")) {
            return baseURL + endpoint;
        } else {
            return baseURL + "/" + endpoint;
        }
    }

    /**
     * Build query string from parameters
     *
     * @param params Map of query parameters
     * @return Query string
     */
    private String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder queryString = new StringBuilder("?");
        params.forEach((key, value) -> queryString.append(key).append("=").append(value).append("&"));
        return queryString.toString().replaceAll("&$", "");
    }

    /**
     * Get response body as string
     *
     * @param response The HttpResponse object
     * @return Response body as string
     * @throws IOException if an I/O error occurs
     */
    public static String getResponseBodyAsString(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

    /**
     * Get response status code
     *
     * @param response The HttpResponse object
     * @return Status code
     */
    public static int getResponseStatusCode(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    /**
     * Shutdown the client
     */
    public void shutdown() {
        try {
            httpClient.getConnectionManager().shutdown();
            logger.info("HttpClient shutdown completed");
        } catch (Exception e) {
            logger.error("Error shutting down HttpClient: " + e.getMessage());
        }
    }

    /**
     * Get the base URL
     *
     * @return Base URL
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * Get default headers
     *
     * @return Map of default headers
     */
    public Map<String, String> getDefaultHeaders() {
        return new HashMap<>(defaultHeaders);
    }
}
