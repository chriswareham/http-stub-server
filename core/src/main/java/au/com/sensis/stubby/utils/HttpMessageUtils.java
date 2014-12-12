package au.com.sensis.stubby.utils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.sensis.stubby.model.StubMessage;

public class HttpMessageUtils {

    private static final Pattern PATTERN = Pattern.compile("\\-.|^.");

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private static final Pattern CONTENT_TYPE_TEXT = Pattern.compile("text/.+");

    private static final Pattern CONTENT_TYPE_JSON = Pattern.compile("application/json(;.+)?");

    public static String getReasonPhrase(final int statusCode) {
        switch (statusCode) {
        case 200:
            return "OK";
        case 201:
            return "Created";
        case 202:
            return "Accepted";
        case 301:
            return "Moved Permanently";
        case 302:
            return "Found";
        case 304:
            return "Not Modified";
        case 400:
            return "Bad Request";
        case 401:
            return "Unauthorized";
        case 403:
            return "Forbidden";
        case 404:
            return "Not Found";
        case 406:
            return "Not Acceptable";
        case 415:
            return "Unsupported Media Type";
        case 422:
            return "Unprocessable Entity";
        case 500:
            return "Internal Server Error";
        case 503:
            return "Service Unavailable";
        default:
            return null;
        }
    }

    public static String upperCaseHeader(final String name) {
        Matcher matcher = PATTERN.matcher(name);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group().toUpperCase());
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static boolean isText(final StubMessage message) {
        String contentType = message.getHeader(CONTENT_TYPE_HEADER);
        return contentType != null && CONTENT_TYPE_TEXT.matcher(contentType).matches();
    }

    public static boolean isJson(final StubMessage message) {
        String contentType = message.getHeader(CONTENT_TYPE_HEADER);
        return contentType != null && CONTENT_TYPE_JSON.matcher(contentType).matches();
    }

    public static String bodyAsText(final StubMessage message) {
        Object body = message.getBody();
        if (body instanceof String) {
            return (String) body;
        }
        throw new RuntimeException("Unexpected body type: " + body.getClass());
    }

    public static Object bodyAsJson(final StubMessage message) {
        Object body = message.getBody();
        if (body instanceof String) {
            return JsonUtils.deserialize((String) body, Object.class); // support object or array as top-level
        }
        if (body instanceof Map || body instanceof List) {
            return body; // assume already parsed
        }
        throw new RuntimeException("Unexpected body type: " + body.getClass());
    }

    private HttpMessageUtils() {
        super();
    }
}
