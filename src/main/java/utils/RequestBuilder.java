package utils;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Request Builder for constructing API requests with fluent interface.
 * Simplifies building complex requests with multiple parameters, headers, and body.
 */
public class RequestBuilder {
    private static final Logger logger = Logger.getLogger(RequestBuilder.class);
    private String endpoint;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private String body;
    private String contentType;

    /**
     * Constructor for RequestBuilder
     */
    public RequestBuilder() {
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.contentType = "application/json";
    }

    /**
     * Set the endpoint path
     *
     * @param endpoint The API endpoint
     */
    public RequestBuilder setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        logger.debug("Endpoint set: " + endpoint);
        return this;
    }

    /**
     * Set HTTP method
     *
     * @param method The HTTP method (GET, POST, PUT, DELETE, PATCH)
     */
    public RequestBuilder setMethod(String method) {
        this.method = method;
        logger.debug("Method set: " + method);
        return this;
    }

    /**
     * Add a header
     *
     * @param key   Header key
     * @param value Header value
     */
    public RequestBuilder addHeader(String key, String value) {
        headers.put(key, value);
        logger.debug("Header added: " + key);
        return this;
    }

    /**
     * Add multiple headers
     *
     * @param headerMap Map of headers
     */
    public RequestBuilder addHeaders(Map<String, String> headerMap) {
        headers.putAll(headerMap);
        logger.debug("Multiple headers added");
        return this;
    }

    /**
     * Add query parameter
     *
     * @param key   Parameter key
     * @param value Parameter value
     */
    public RequestBuilder addQueryParam(String key, String value) {
        queryParams.put(key, value);
        logger.debug("Query param added: " + key);
        return this;
    }

    /**
     * Add multiple query parameters
     *
     * @param paramsMap Map of parameters
     */
    public RequestBuilder addQueryParams(Map<String, String> paramsMap) {
        queryParams.putAll(paramsMap);
        logger.debug("Multiple query params added");
        return this;
    }

    /**
     * Set request body
     *
     * @param body The request body as string
     */
    public RequestBuilder setBody(String body) {
        this.body = body;
        logger.debug("Request body set");
        return this;
    }

    /**
     * Set Content-Type header
     *
     * @param contentType The content type (e.g., "application/json")
     */
    public RequestBuilder setContentType(String contentType) {
        this.contentType = contentType;
        headers.put("Content-Type", contentType);
        logger.debug("Content-Type set: " + contentType);
        return this;
    }

    /**
     * Add authorization token
     *
     * @param token The authorization token
     */
    public RequestBuilder addAuthToken(String token) {
        headers.put("Authorization", "Bearer " + token);
        logger.debug("Authorization token added");
        return this;
    }

    /**
     * Build the request and return request details
     *
     * @return RequestDetails object
     */
    public RequestDetails build() {
        RequestDetails details = new RequestDetails();
        details.setEndpoint(endpoint);
        details.setMethod(method);
        details.setHeaders(new HashMap<>(headers));
        details.setQueryParams(new HashMap<>(queryParams));
        details.setBody(body);
        details.setContentType(contentType);
        logger.info("Request built - Method: " + method + ", Endpoint: " + endpoint);
        return details;
    }

    /**
     * Get all headers
     *
     * @return Map of headers
     */
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    /**
     * Get all query parameters
     *
     * @return Map of query parameters
     */
    public Map<String, String> getQueryParams() {
        return new HashMap<>(queryParams);
    }

    /**
     * Inner class to hold request details
     */
    public static class RequestDetails {
        private String endpoint;
        private String method;
        private Map<String, String> headers;
        private Map<String, String> queryParams;
        private String body;
        private String contentType;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public Map<String, String> getQueryParams() {
            return queryParams;
        }

        public void setQueryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public String toString() {
            return "RequestDetails{" +
                    "endpoint='" + endpoint + '\'' +
                    ", method='" + method + '\'' +
                    ", headers=" + headers +
                    ", queryParams=" + queryParams +
                    ", body='" + body + '\'' +
                    ", contentType='" + contentType + '\'' +
                    '}';
        }
    }
}
