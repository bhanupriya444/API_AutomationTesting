package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.RequestBuilder;

/**
 * Sample API Test Class demonstrating usage of all utility classes.
 * This example shows how to use the API automation framework.
 */
public class SampleAPITest extends BaseTest {

    /**
     * Test: Get API health check
     * Demonstrates a simple GET request and response validation
     */
    @Test(description = "Test API Health Check")
    public void testAPIHealthCheck() {
        logStep("Make GET request to health endpoint");
        
        Response response = restAssuredClient.get("/health");
        
        logStep("Validate response status code");
        Assert.assertTrue(responseValidator.validateStatusCode(response, 200),
                "Expected status code 200 but got " + response.getStatusCode());
        
        logStep("Validate response is valid JSON");
        Assert.assertTrue(responseValidator.validateIsValidJson(response),
                "Response body is not valid JSON");
        
        logTestResult("testAPIHealthCheck", true);
    }

    /**
     * Test: Create data with POST request
     * Demonstrates POST request with request body and response validation
     */
    @Test(description = "Test Create Data")
    public void testCreateData() {
        logStep("Create request body");
        String requestBody = "{\n" +
                "  \"name\": \"Test Data\",\n" +
                "  \"description\": \"Sample test data\",\n" +
                "  \"status\": \"active\"\n" +
                "}";
        
        logStep("Make POST request with request body");
        Response response = restAssuredClient
                .setBody(requestBody)
                .post("/data");
        
        logStep("Validate response status code");
        Assert.assertTrue(responseValidator.validateStatusCode(response, 201),
                "Expected status code 201 but got " + response.getStatusCode());
        
        logStep("Validate response contains expected fields");
        Assert.assertTrue(responseValidator.validateJsonPathExists(response, "id"),
                "Response does not contain 'id' field");
        
        logStep("Print response for verification");
        printResponse(response.getBody().asString());
        
        logTestResult("testCreateData", true);
    }

    /**
     * Test: Get data with query parameters
     * Demonstrates using RequestBuilder for complex requests
     */
    @Test(description = "Test Get Data with Filters")
    public void testGetDataWithFilters() {
        logStep("Build request with query parameters");
        RequestBuilder requestBuilder = createRequestBuilder()
                .setEndpoint("/data")
                .setMethod("GET")
                .addQueryParam("status", "active")
                .addQueryParam("limit", "10");
        
        RequestBuilder.RequestDetails requestDetails = requestBuilder.build();
        
        logStep("Make GET request with query parameters");
        Response response = restAssuredClient.getRestAssuredClient()
                .given()
                .queryParam("status", "active")
                .queryParam("limit", "10")
                .when()
                .get("/data");
        
        logStep("Validate response status code");
        Assert.assertTrue(responseValidator.validateStatusCode(response, 200),
                "Expected status code 200 but got " + response.getStatusCode());
        
        logStep("Validate response contains data");
        Assert.assertTrue(responseValidator.validateBodyNotEmpty(response),
                "Response body is empty");
        
        logTestResult("testGetDataWithFilters", true);
    }

    /**
     * Test: Update data with PUT request
     * Demonstrates PUT request and JSON validation
     */
    @Test(description = "Test Update Data")
    public void testUpdateData() {
        logStep("Create update request body");
        String updateBody = "{\n" +
                "  \"name\": \"Updated Data\",\n" +
                "  \"status\": \"inactive\"\n" +
                "}";
        
        logStep("Make PUT request to update data");
        Response response = restAssuredClient
                .setBody(updateBody)
                .put("/data/1");
        
        logStep("Validate response status code");
        Assert.assertTrue(responseValidator.validateStatusCode(response, 200),
                "Expected status code 200 but got " + response.getStatusCode());
        
        logStep("Validate updated field value");
        Assert.assertTrue(responseValidator.validateJsonPath(response, "status", "inactive"),
                "Status field was not updated correctly");
        
        logTestResult("testUpdateData", true);
    }

    /**
     * Test: Delete data with DELETE request
     * Demonstrates DELETE request and response validation
     */
    @Test(description = "Test Delete Data")
    public void testDeleteData() {
        logStep("Make DELETE request");
        Response response = restAssuredClient.delete("/data/1");
        
        logStep("Validate response status code");
        Assert.assertTrue(responseValidator.validateStatusCode(response, 204),
                "Expected status code 204 but got " + response.getStatusCode());
        
        logTestResult("testDeleteData", true);
    }

    /**
     * Test: Validate response time
     * Demonstrates performance testing capability
     */
    @Test(description = "Test Response Time Performance")
    public void testResponseTimePerformance() {
        logStep("Make API request");
        Response response = restAssuredClient.get("/data");
        
        logStep("Validate response time is within acceptable limit");
        int maxTimeMs = getTimeoutInMillis();
        Assert.assertTrue(responseValidator.validateResponseTime(response, maxTimeMs),
                "Response time exceeded limit of " + maxTimeMs + "ms");
        
        logTestResult("testResponseTimePerformance", true);
    }

    /**
     * Test: Validate response headers
     * Demonstrates header validation
     */
    @Test(description = "Test Response Headers")
    public void testResponseHeaders() {
        logStep("Make API request");
        Response response = restAssuredClient.get("/data");
        
        logStep("Validate Content-Type header");
        Assert.assertTrue(responseValidator.validateContentType(response, "application/json"),
                "Content-Type header is not application/json");
        
        logStep("Validate custom header presence");
        Assert.assertTrue(responseValidator.validateHeader(response, "X-Request-ID", null),
                "X-Request-ID header not present");
        
        logTestResult("testResponseHeaders", true);
    }

    /**
     * Test: JSON parsing and manipulation
     * Demonstrates JSON utility functions
     */
    @Test(description = "Test JSON Parsing")
    public void testJSONParsing() {
        logStep("Create sample JSON");
        String sampleJson = "{\n" +
                "  \"user\": {\n" +
                "    \"id\": 123,\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"email\": \"john@example.com\"\n" +
                "  }\n" +
                "}";
        
        logStep("Validate JSON is valid");
        Assert.assertTrue(jsonParser.isValidJSON(sampleJson),
                "Sample JSON is not valid");
        
        logStep("Extract value from JSON");
        Object userName = jsonParser.extractValue(sampleJson, "user.name");
        Assert.assertEquals(userName, "John Doe", "User name extraction failed");
        
        logStep("Check if JSON contains key");
        Assert.assertTrue(jsonParser.containsKey(sampleJson, "user"),
                "JSON does not contain 'user' key");
        
        logStep("Pretty print JSON");
        printResponse(sampleJson);
        
        logTestResult("testJSONParsing", true);
    }
}
