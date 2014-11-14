package au.com.sensis.stubby.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * This class provides utilities for serialising and deserialising JSON data.
 */
public final class JsonUtils {
    /**
     * Get an object mapper for serialising and deserialising JSON data.
     *
     * @return an object mapper for serialising and deserialising JSON data
     */
    public static ObjectMapper mapper() {
        return new ObjectMapper();
    }

    /**
     * Get an object mapper for serialising and deserialising JSON data.
     *
     * @return an object mapper for serialising and deserialising JSON data
     */
    public static ObjectMapper defaultMapper() {
        ObjectMapper result = mapper();
        result.enable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        result.enable(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS); // for 'exact' floating-point matches
        result.setSerializationInclusion(Inclusion.NON_NULL);
        return result;
    }

    /**
     * Get an object writer for pretty-printing JSON data.
     *
     * @return an object writer for pretty-printing JSON data
     */
    public static ObjectWriter prettyWriter() {
        return defaultMapper().writerWithDefaultPrettyPrinter();
    }

    /**
     * Pretty-print an object as JSON data.
     *
     * @param value the object to pretty-print as JSON data
     * @return the object pretty-printed as JSON data
     * @throws RuntimeException if the object cannot be pretty-printed as JSON data
     */
    public static String prettyPrint(final Object value) {
        try {
            return prettyWriter().writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException("Error serializing JSON", e);
        }
    }

    /**
     * Serialise an object as JSON data.
     *
     * @param object the object to serialise as JSON data
     * @return the object serialised as JSON data
     * @throws RuntimeException if the object cannot be serialised as JSON data
     */
    public static String serialize(final Object object) {
        try {
            return defaultMapper().writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Error serializing JSON", e);
        }
    }

    /**
     * Serialise an object as JSON data.
     *
     * @param stream the output stream to serialise an object to
     * @param object the object to serialise as JSON data
     * @throws RuntimeException if the object cannot be serialised as JSON data
     */
    public static void serialize(final OutputStream stream, final Object object) {
        try {
            defaultMapper().writeValue(stream, object);
        } catch (IOException e) {
            throw new RuntimeException("Error serializing JSON", e);
        }
    }

    /**
     * Deserialise an object from JSON data.
     *
     * @param <T> the type of object to deserialise from JSON data
     * @param json the JSON data to deserialise an object from
     * @param type the type of object to deserialise from JSON data
     * @return the object deserialised from JSON data
     * @throws RuntimeException if the object cannot be deserialised from JSON data
     */
    public static <T> T deserialize(final String json, final Class<T> type) {
        try {
            return defaultMapper().readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing JSON", e);
        }
    }

    /**
     * Deserialise an object from JSON data.
     *
     * @param <T> the type of object to deserialise from JSON data
     * @param stream the input stream to deserialise an object from
     * @param type the type of object to deserialise from JSON data
     * @return the object deserialised from JSON data
     * @throws RuntimeException if the object cannot be deserialised from JSON data
     */
    public static <T> T deserialize(final InputStream stream, final Class<T> type) {
        try {
            return defaultMapper().readValue(stream, type);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing JSON", e);
        }
    }

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private JsonUtils() {
        super();
    }
}
