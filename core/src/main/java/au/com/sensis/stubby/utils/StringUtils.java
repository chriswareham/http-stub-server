package au.com.sensis.stubby.utils;

public class StringUtils {
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
