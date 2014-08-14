package au.com.sensis.stubby.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StubExchangeTest {

    private StubRequest request;
    private StubResponse response;
    private StubExchange exchange;

    @Before
    public void before() {
        request = new StubRequest();
        request.setMethod("ABC");

        response = new StubResponse();
        response.setStatus(1234);

        exchange = new StubExchange();
        exchange.setRequest(request);
        exchange.setResponse(response);
        exchange.setDelay(5000L);
        exchange.setScript("script();");
        exchange.setScriptType("JavaScript");
    }

    @Test
    public void testCopyConstructor() {
        StubExchange copy = new StubExchange(exchange);

        assertEquals(Long.valueOf(5000L), copy.getDelay());
        assertEquals("script();", copy.getScript());
        assertEquals("JavaScript", copy.getScriptType());
        assertEquals("ABC", copy.getRequest().getMethod());
        assertEquals(Integer.valueOf(1234), copy.getResponse().getStatus());

        assertTrue(copy.getRequest() != request); // assert deep copy
        assertTrue(copy.getResponse() != response);
    }

    @Test
    public void testJsonSerialize() {
        // TODO: assert Jackson serializes/deserializes correctly
    }

}
