package au.com.sensis.stubby.test;

import org.junit.BeforeClass;

import au.com.sensis.stubby.test.support.TestServer;

public class StandaloneSuiteTest extends AllTests {

    @BeforeClass
    public static void setUp() {
        if (!TestServer.isRunning()) { // keep running for all tests
            TestServer.start();
        }
        System.setProperty("test.server",
                String.format("http://localhost:%d", TestServer.getPort()));
    }

}
