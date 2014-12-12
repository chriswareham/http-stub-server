package au.com.sensis.stubby.js;

import au.com.sensis.stubby.script.ScriptWorld;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.model.StubResponse;

public class ScriptWorldTest {
    private static final String PATH = "/request/path";
    private static final String SCRIPT_TYPE = "JavaScript";
    private static final Long DELAY = 123L;

    private StubRequest request;
    private StubExchange exchange;

    @Before
    public void before() {
        request = new StubRequest();
        request.setPath(PATH);

        exchange = new StubExchange();
        exchange.setScriptType(SCRIPT_TYPE);
        exchange.setRequest(new StubRequest());
        exchange.setResponse(new StubResponse());
        exchange.setDelay(DELAY);
    }

    @Test
    public void testCopiesFields() {
        ScriptWorld world = new ScriptWorld(request, null, exchange);

        // should be different instances
        Assert.assertNotSame(exchange.getRequest(), world.getRequest());
        Assert.assertNotSame(exchange.getResponse(), world.getResponse());

        // ensure we use the actual request, not request pattern
        Assert.assertEquals(PATH, world.getRequest().getPath());

        Assert.assertEquals(SCRIPT_TYPE, world.getScriptType());

        // immutable anyway...
        Assert.assertEquals(DELAY, world.getDelay());
    }
}
