package au.com.sensis.stubby.test;

import org.junit.BeforeClass;

public class ServletSuiteIT extends AllTests {

    @BeforeClass
    public static void setUp() {
        System.setProperty("test.server", String.format("http://localhost:9090")); // Tomcat server port (see pom.xml)
    }

}
