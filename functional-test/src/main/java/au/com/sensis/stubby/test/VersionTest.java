package au.com.sensis.stubby.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import au.com.sensis.stubby.test.client.GenericClientResponse;
import au.com.sensis.stubby.test.support.TestBase;

public class VersionTest extends TestBase {
    
    public static final class VersionResponse {
        public String version;
    }
    
    @Test
    public void testGetVersion() {
        GenericClientResponse response = client.executeGet("/_control/version").assertOk();
        VersionResponse version = response.getJson(VersionResponse.class);
        
        assertTrue(version.version.length() > 0);
    }

}
