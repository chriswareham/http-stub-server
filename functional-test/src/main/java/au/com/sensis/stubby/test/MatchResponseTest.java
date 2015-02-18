package au.com.sensis.stubby.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import au.com.sensis.stubby.test.client.GenericClientResponse;
import au.com.sensis.stubby.test.support.TestBase;

public class MatchResponseTest extends TestBase {

    @Test
    public void testAllFields() {
        builder()
            .setRequestPath("/.*")
            .setResponseStatus(201)
            .addResponseHeader("X-Foo", "bar1")
            .addResponseHeader("X-Foo", "bar2") // two values for single name
            .addResponseHeader("x-foo", "bar3; bar4") // check case-insensitivity
            .setResponseBody("response body")
            .stub();

        GenericClientResponse response = client.executeGet("/test");

        Assert.assertEquals(201, response.getStatus());
        Assert.assertEquals(Arrays.asList("bar1", "bar2", "bar3; bar4"), response.getHeaders("X-Foo"));
        Assert.assertEquals("response body", response.getText());
    }

    @Test
    public void testMinimalResponse() {
        builder()
            .setRequestPath("/test")
            .setResponseStatus(202)
            .stub();

        GenericClientResponse response = client.executeGet("/test");

        Assert.assertEquals(202, response.getStatus());
    }
}
