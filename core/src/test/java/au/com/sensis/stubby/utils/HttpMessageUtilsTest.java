package au.com.sensis.stubby.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import au.com.sensis.stubby.model.StubMessage;

public class HttpMessageUtilsTest {

    @Test
    public void testUpperCaseHeader() {
        Assert.assertEquals("Header", HttpMessageUtils.upperCaseHeader("header"));
        Assert.assertEquals("Header-Name", HttpMessageUtils.upperCaseHeader("header-name"));
        Assert.assertEquals("X-Header-Name", HttpMessageUtils.upperCaseHeader("x-header-name"));
        Assert.assertEquals("-X-Header-Name", HttpMessageUtils.upperCaseHeader("-x-header-name"));
    }

    @Test
    public void testIsText() {
        StubMessage message = new StubMessage() { };

        message.setHeader("Content-Type", "text/plain");
        Assert.assertTrue(HttpMessageUtils.isText(message));

        message.removeHeaders("Content-Type");

        message.setHeader("Content-Type", "text/anything; charset=UTF-8");
        Assert.assertTrue(HttpMessageUtils.isText(message));
    }

    @Test
    public void testIsNotText() {
        StubMessage message = new StubMessage() { };

        message.setHeader("Content-Type", "application/xml");
        Assert.assertFalse(HttpMessageUtils.isText(message));
    }

    @Test
    public void testIsNotTextHeader() {
        StubMessage message = new StubMessage() { };

        Assert.assertFalse(HttpMessageUtils.isText(message));
    }

    @Test
    public void testIsJson() {
        StubMessage message = new StubMessage() { };

        message.setHeader("Content-Type", "application/json");
        Assert.assertTrue(HttpMessageUtils.isJson(message));

        message.removeHeaders("Content-Type");

        message.setHeader("Content-Type", "application/json; charset=UTF-8");
        Assert.assertTrue(HttpMessageUtils.isJson(message));
    }

    @Test
    public void testIsNotJson() {
        StubMessage message = new StubMessage() { };

        message.setHeader("Content-Type", "application/xml");
        Assert.assertFalse(HttpMessageUtils.isJson(message));
    }

    @Test
    public void testIsNotJsonHeader() {
        StubMessage message = new StubMessage() { };

        Assert.assertFalse(HttpMessageUtils.isJson(message));
    }

    @Test
    public void testBodyAsText() {
        StubMessage message = new StubMessage() { };
        message.setBody("text");
        Assert.assertEquals("text", HttpMessageUtils.bodyAsText(message));
    }

    @Test(expected=RuntimeException.class)
    public void testBodyAsText_unknown() {
        StubMessage message = new StubMessage() { };
        message.setBody(Arrays.asList("foo"));

        HttpMessageUtils.bodyAsText(message);
    }

    @Test
    public void testBodyAsJson_string() {
        StubMessage message = new StubMessage() { };
        message.setBody("[\"foo\",\"bar\"]");
        Assert.assertEquals(Arrays.asList("foo", "bar"), HttpMessageUtils.bodyAsJson(message));
    }

    @Test
    public void testBodyAsJson_list() {
        StubMessage message = new StubMessage() { };
        message.setBody(Arrays.asList("foo", "bar"));
        Assert.assertEquals(Arrays.asList("foo", "bar"), HttpMessageUtils.bodyAsJson(message));
    }

    @Test
    public void testBodyAsJson_map() {
        Map<String,String> map = new HashMap<String,String>();
        map.put("foo", "bar");

        StubMessage message = new StubMessage() { };
        message.setBody(map);

        Assert.assertEquals(new HashMap<String,String>(map), HttpMessageUtils.bodyAsJson(message));
    }

    @Test(expected=RuntimeException.class)
    public void testBodyAsJson_unknown() {
        StubMessage message = new StubMessage() { };
        message.setBody(Integer.valueOf(666));

        HttpMessageUtils.bodyAsJson(message);
    }
}
