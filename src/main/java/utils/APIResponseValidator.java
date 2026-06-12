package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Utility class for validating API responses.
 * Provides methods for validating status codes, headers, and response body content.
 */
public class APIResponseValidator {
    private static final Logger logger = Logger.getLogger(APIResponseValidator.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Validate REST Assured response status code
     *
     * @param response   The Response object
     * @param statusCode Expected status code
     * @return true if status code matches, false otherwise
     */
    public static boolean validateStatusCode(Response response, int statusCode) {
        int actualCode = response.getStatusCode();
        boolean isValid = actualCode == statusCode;
        logValidation("Status Code", statusCode, actualCode, isValid);
        return isValid;
    }

    /**
     * Validate HttpClient response status code
     *
     * @param response   The HttpResponse object
     * @param statusCode Expected status code
     * @return true if status code matches, false otherwise
     */
    public static boolean validateStatusCode(HttpResponse response, int statusCode) {
        int actualCode = response.getStatusLine().getStatusCode();
        boolean isValid = actualCode == statusCode;
        logValidation("Status Code", statusCode, actualCode, isValid);
        return isValid;
    }

    /**
     * Validate response header presence and value
     *
     * @param response    The Response object
     * @param headerName  The header name
     * @param headerValue Expected header value (can be null to just check presence)
     * @return true if header is present with correct value, false otherwise
     */
    public static boolean validateHeader(Response response, String headerName, String headerValue) {
        String actualValue = response.getHeader(headerName);
        boolean isValid;

        if (headerValue == null) {
            isValid = actualValue != null;
        } else {
            isValid = headerValue.equals(actualValue);
        }

        logValidation("Header [" + headerName + "]", headerValue, actualValue, isValid);
        return isValid;
    }

    /**
     * Validate response contains a specific JSON path with expected value
     *
     * @param response      The Response object
     * @param jsonPath      The JSON path to validate (e.g., "data.user.id")
     * @param expectedValue Expected value
     * @return true if JSON path value matches expected value, false otherwise
     */
    public static boolean validateJsonPath(Response response, String jsonPath, Object expectedValue) {
        try {
            Object actualValue = response.jsonPath().get(jsonPath);
            boolean isValid = expectedValue.equals(actualValue);
            logValidation("JSON Path [" + jsonPath + "]", expectedValue, actualValue, isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating JSON path: " + jsonPath, e);
            return false;
        }
    }

    /**
     * Validate response contains a JSON field
     *
     * @param response The Response object
     * @param jsonPath The JSON path to check
     * @return true if JSON path exists, false otherwise
     */
    public static boolean validateJsonPathExists(Response response, String jsonPath) {
        try {
            Object value = response.jsonPath().get(jsonPath);
            boolean isValid = value != null;
            logValidation("JSON Path Exists [" + jsonPath + "]", true, isValid, isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating JSON path existence: " + jsonPath, e);
            return false;
        }
    }

    /**
     * Validate response body contains a specific string
     *
     * @param response   The Response object
     * @param searchText Text to search for in response body
     * @return true if text is found, false otherwise
     */
    public static boolean validateBodyContains(Response response, String searchText) {
        String body = response.getBody().asString();
        boolean isValid = body.contains(searchText);
        logValidation("Body Contains", searchText, "Found: " + isValid, isValid);
        return isValid;
    }

    /**
     * Validate response body does not contain a specific string
     *
     * @param response   The Response object
     * @param searchText Text to search for in response body
     * @return true if text is not found, false otherwise
     */
    public static boolean validateBodyNotContains(Response response, String searchText) {
        String body = response.getBody().asString();
        boolean isValid = !body.contains(searchText);
        logValidation("Body Not Contains", searchText, "Not Found: " + isValid, isValid);
        return isValid;
    }

    /**
     * Validate response is valid JSON
     *
     * @param response The Response object
     * @return true if response body is valid JSON, false otherwise
     */
    public static boolean validateIsValidJson(Response response) {
        try {
            String body = response.getBody().asString();
            objectMapper.readTree(body);
            logValidation("Valid JSON", true, true, true);
            return true;
        } catch (IOException e) {
            logger.error("Response body is not valid JSON", e);
            logValidation("Valid JSON", true, false, false);
            return false;
        }
    }

    /**
     * Validate response time is within acceptable limit
     *
     * @param response      The Response object
     * @param maxTimeMillis Maximum acceptable response time in milliseconds
     * @return true if response time is within limit, false otherwise
     */
    public static boolean validateResponseTime(Response response, long maxTimeMillis) {
        long responseTime = response.getTime();
        boolean isValid = responseTime <= maxTimeMillis;
        logValidation("Response Time (ms)", maxTimeMillis, responseTime, isValid);
        return isValid;
    }

    /**
     * Validate Content-Type header
     *
     * @param response      The Response object
     * @param expectedType  Expected content type (e.g., "application/json")
     * @return true if content type matches, false otherwise
     */
    public static boolean validateContentType(Response response, String expectedType) {
        String actualType = response.getContentType();
        boolean isValid = actualType != null && actualType.contains(expectedType);
        logValidation("Content-Type", expectedType, actualType, isValid);
        return isValid;
    }

    /**
     * Validate response body is not empty
     *
     * @param response The Response object
     * @return true if body is not empty, false otherwise
     */
    public static boolean validateBodyNotEmpty(Response response) {
        String body = response.getBody().asString();
        boolean isValid = body != null && !body.trim().isEmpty();
        logValidation("Body Not Empty", true, isValid, isValid);
        return isValid;
    }

    /**
     * Validate response body matches exact JSON
     *
     * @param response The Response object
     * @param json     Expected JSON string
     * @return true if response body matches expected JSON, false otherwise
     */
    public static boolean validateJsonEqual(Response response, String json) {
        try {
            String responseBody = response.getBody().asString();
            JsonNode expectedNode = objectMapper.readTree(json);
            JsonNode actualNode = objectMapper.readTree(responseBody);
            boolean isValid = expectedNode.equals(actualNode);
            logValidation("JSON Equality", "Expected JSON", "Actual JSON", isValid);
            return isValid;
        } catch (IOException e) {
            logger.error("Error comparing JSON", e);
            return false;
        }
    }

    /**
     * Log validation result
     *
     * @param validationType The type of validation
     * @param expected       The expected value
     * @param actual         The actual value
     * @param isValid        Whether validation passed
     */
    private static void logValidation(String validationType, Object expected, Object actual, boolean isValid) {
        String status = isValid ? "PASSED" : "FAILED";
        logger.info("[" + status + "] " + validationType + " - Expected: " + expected + ", Actual: " + actual);
    }

    /**
     * Pretty print a Response body
     *
     * @param response The Response object
     */
    public static void prettyPrintResponse(Response response) {
        logger.info("===== Response Details =====");
        logger.info("Status Code: " + response.getStatusCode());
        logger.info("Content-Type: " + response.getContentType());
        logger.info("Response Body: " + response.getBody().prettyPrint());
        logger.info("===========================");
    }
}
