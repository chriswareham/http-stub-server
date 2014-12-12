package au.com.sensis.stubby.utils;

public class StringUtils {

    public static boolean isBlank(final String str) {
        return str == null || str.trim().isEmpty();
    }

    private StringUtils() {
        super();
    }
}
