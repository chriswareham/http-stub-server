package au.com.sensis.stubby.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ControlRequestTest.class,
        ControlResponseTest.class,
        DelayTest.class,
        DuplicateRequestPatternTest.class,
        FindRequestTest.class,
        JsonBodyPatternTest.class,
        LoadTest.class,
        MatchingTest.class,
        MatchResponseTest.class,
        ScriptTest.class,
        TextBodyPatternTest.class,
        VersionTest.class
})
public class AllTests {

}
