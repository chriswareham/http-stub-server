package au.com.sensis.stubby.test;

import org.junit.Assert;
import org.junit.Test;

import au.com.sensis.stubby.test.client.GenericClientResponse;
import au.com.sensis.stubby.test.support.TestBase;

public class VersionTest extends TestBase {

    @Test
    public void testGetVersion() {
        GenericClientResponse response = client.executeGet("/_control/version").assertOk();
        VersionResponse version = response.getJson(VersionResponse.class);

        Assert.assertTrue(version.version.length() > 0);
    }

    private static final class VersionResponse {
        public String version;
    }
}
