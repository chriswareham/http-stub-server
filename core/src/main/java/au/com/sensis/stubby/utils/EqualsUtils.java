package au.com.sensis.stubby.utils;

public class EqualsUtils {

    public static boolean equals(final Object a, final Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private EqualsUtils() {
        super();
    }
}
