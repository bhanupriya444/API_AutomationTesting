package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Utility class for JSON parsing and manipulation.
 * Supports both Jackson and Gson libraries for flexibility.
 */
public class JSONParser {
    private static final Logger logger = Logger.getLogger(JSONParser.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Parse JSON string using Gson
     *
     * @param jsonString The JSON string to parse
     * @return JsonElement parsed from the string
     */
    public static JsonElement parseJSON(String jsonString) {
        try {
            JsonElement element = JsonParser.parseString(jsonString);
            logger.debug("JSON parsed successfully using Gson");
            return element;
        } catch (Exception e) {
            logger.error("Error parsing JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parse JSON string to JsonNode using Jackson
     *
     * @param jsonString The JSON string to parse
     * @return JsonNode parsed from the string
     */
    public static JsonNode parseJsonNode(String jsonString) {
        try {
            JsonNode node = objectMapper.readTree(jsonString);
            logger.debug("JSON parsed to JsonNode successfully");
            return node;
        } catch (IOException e) {
            logger.error("Error parsing JSON to JsonNode: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert JSON string to pretty formatted string
     *
     * @param jsonString The JSON string to format
     * @return Pretty formatted JSON string
     */
    public static String prettyPrintJSON(String jsonString) {
        try {
            JsonElement element = JsonParser.parseString(jsonString);
            return gson.toJson(element);
        } catch (Exception e) {
            logger.error("Error pretty printing JSON: " + e.getMessage());
            return jsonString;
        }
    }

    /**
     * Extract value from JSON using key path (nested keys separated by dot)
     * Example: "data.user.name"
     *
     * @param jsonString The JSON string
     * @param keyPath    The key path to extract
     * @return The extracted value or null if not found
     */
    public static Object extractValue(String jsonString, String keyPath) {
        try {
            JsonNode node = parseJsonNode(jsonString);
            String[] keys = keyPath.split("\\.");
            for (String key : keys) {
                if (node == null) {
                    return null;
                }
                node = node.get(key);
            }
            return node != null ? node.asText() : null;
        } catch (Exception e) {
            logger.error("Error extracting value from JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if JSON contains a specific key
     *
     * @param jsonString The JSON string
     * @param key        The key to check
     * @return true if key exists, false otherwise
     */
    public static boolean containsKey(String jsonString, String key) {
        try {
            JsonNode node = parseJsonNode(jsonString);
            return node != null && node.has(key);
        } catch (Exception e) {
            logger.error("Error checking if JSON contains key: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if JSON contains a specific value
     *
     * @param jsonString The JSON string
     * @param value      The value to check
     * @return true if value exists, false otherwise
     */
    public static boolean containsValue(String jsonString, String value) {
        try {
            return jsonString.contains("\"" + value + "\"") || jsonString.contains(": " + value);
        } catch (Exception e) {
            logger.error("Error checking if JSON contains value: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convert Java object to JSON string
     *
     * @param object The object to convert
     * @return JSON string representation
     */
    public static String toJSON(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Error converting object to JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert JSON string to Map
     *
     * @param jsonString The JSON string
     * @return Map representation of JSON
     */
    public static Map<String, Object> toMap(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            logger.error("Error converting JSON to Map: " + e.getMessage());
            return null;
        }
    }

    /**
     * Merge two JSON objects
     *
     * @param json1 First JSON string
     * @param json2 Second JSON string
     * @return Merged JSON string
     */
    public static String mergeJSON(String json1, String json2) {
        try {
            JsonNode node1 = parseJsonNode(json1);
            JsonNode node2 = parseJsonNode(json2);

            if (node1.isObject() && node2.isObject()) {
                ObjectNode merged = (ObjectNode) node1;
                merged.setAll((ObjectNode) node2);
                return objectMapper.writeValueAsString(merged);
            }
            return json1;
        } catch (Exception e) {
            logger.error("Error merging JSON objects: " + e.getMessage());
            return json1;
        }
    }

    /**
     * Get all keys from a JSON object
     *
     * @param jsonString The JSON string
     * @return List of all keys
     */
    public static List<String> getAllKeys(String jsonString) {
        List<String> keys = new ArrayList<>();
        try {
            JsonNode node = parseJsonNode(jsonString);
            if (node.isObject()) {
                Iterator<String> fieldNames = node.fieldNames();
                fieldNames.forEachRemaining(keys::add);
            }
        } catch (Exception e) {
            logger.error("Error getting all keys from JSON: " + e.getMessage());
        }
        return keys;
    }

    /**
     * Get all values from a JSON array
     *
     * @param jsonString The JSON array string
     * @return List of all values
     */
    public static List<String> getAllValues(String jsonString) {
        List<String> values = new ArrayList<>();
        try {
            JsonNode node = parseJsonNode(jsonString);
            if (node.isArray()) {
                node.forEach(item -> values.add(item.asText()));
            }
        } catch (Exception e) {
            logger.error("Error getting all values from JSON: " + e.getMessage());
        }
        return values;
    }

    /**
     * Remove a key from JSON object
     *
     * @param jsonString The JSON string
     * @param key        The key to remove
     * @return Updated JSON string without the key
     */
    public static String removeKey(String jsonString, String key) {
        try {
            JsonNode node = parseJsonNode(jsonString);
            if (node.isObject()) {
                ((ObjectNode) node).remove(key);
                return objectMapper.writeValueAsString(node);
            }
            return jsonString;
        } catch (Exception e) {
            logger.error("Error removing key from JSON: " + e.getMessage());
            return jsonString;
        }
    }

    /**
     * Add or update a key-value pair in JSON object
     *
     * @param jsonString The JSON string
     * @param key        The key to add/update
     * @param value      The value to set
     * @return Updated JSON string
     */
    public static String putKeyValue(String jsonString, String key, Object value) {
        try {
            JsonNode node = parseJsonNode(jsonString);
            if (node.isObject()) {
                ObjectNode objectNode = (ObjectNode) node;
                objectNode.putPOJO(key, value);
                return objectMapper.writeValueAsString(objectNode);
            }
            return jsonString;
        } catch (Exception e) {
            logger.error("Error adding/updating key in JSON: " + e.getMessage());
            return jsonString;
        }
    }

    /**
     * Validate if a string is valid JSON
     *
     * @param jsonString The string to validate
     * @return true if valid JSON, false otherwise
     */
    public static boolean isValidJSON(String jsonString) {
        try {
            parseJsonNode(jsonString);
            return true;
        } catch (Exception e) {
            logger.warn("Invalid JSON string: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get JSON array size
     *
     * @param jsonString The JSON array string
     * @return Size of the array, -1 if not an array
     */
    public static int getArraySize(String jsonString) {
        try {
            JsonNode node = parseJsonNode(jsonString);
            if (node.isArray()) {
                return node.size();
            }
            return -1;
        } catch (Exception e) {
            logger.error("Error getting array size: " + e.getMessage());
            return -1;
        }
    }
}
