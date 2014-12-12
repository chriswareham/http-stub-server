package au.com.sensis.stubby.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualsUtilsTest {

    @Test
    public void testSafeEquals() {
        assertTrue(EqualsUtils.equals(null, null));
        assertTrue(EqualsUtils.equals("foo", "foo"));
        assertFalse(EqualsUtils.equals("foo", null));
        assertFalse(EqualsUtils.equals(null, "foo"));
        assertFalse(EqualsUtils.equals("foo", "bar"));
    }

}
