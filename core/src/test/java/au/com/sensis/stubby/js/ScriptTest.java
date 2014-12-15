package au.com.sensis.stubby.js;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.model.StubResponse;
import au.com.sensis.stubby.script.Script;
import au.com.sensis.stubby.script.ScriptWorld;

public class ScriptTest {
    /**
     * Test bean - needs to be public for script access.
     */
    @SuppressWarnings("serial")
    public static class TestBean implements Serializable {
        private List<String> items = new ArrayList<String>();
        public List<String> getItems() {
            return items;
        }
    }

    private StubExchange exchange;
    private StubRequest request;
    private StubResponse response;
    private TestBean testBean;
    private ScriptWorld world;

    @Before
    public void before() {
        givenTestBean();
        givenDefaultRequest();
        givenDefaultResponse();
        givenDefaultExchange();
    }

    private void givenTestBean() {
        testBean = new TestBean();
        testBean.getItems().add("one");
        testBean.getItems().add("two");
    }

    private void givenDefaultRequest() {
        request = new StubRequest();
        request.setMethod("POST");
        request.setPath("/request/path");
        request.setParam("foo", "bar");
        request.setHeader("Content-Type", "text/plain");
        request.setBody("request body");
    }

    private void givenJsonRequestBody() {
        request.setBody(testBean);
    }

    private void givenJsonResponseBody() {
        response.setBody(testBean);
    }

    private void givenDefaultResponse() {
        response = new StubResponse();
        response.setHeader("Content-Type", "application/json");
        response.setBody("response body");
        response.setStatus(200);
    }

    private void givenDefaultExchange() {
        exchange = new StubExchange();
        exchange.setRequest(request);
        exchange.setResponse(response);
        exchange.setDelay(1234L);
    }

    private Object execute(String script) {
        world = new ScriptWorld(request, null, exchange);
        return new Script(script).execute(world);
    }

    @Test
    public void testEmptyScript() {
        execute("");
    }

    @Test
    public void testSimpleExpr() {
        Assert.assertEquals(Double.valueOf(3.0), execute("var a = 1; var b = 2; a + b;"));
    }

    @Test
    public void testGetDelay() {
        Assert.assertEquals(Long.valueOf(1234L), execute("exchange.delay"));
    }

    @Test
    public void setSetDelay() {
        execute("exchange.delay = 666");
        Assert.assertEquals(Long.valueOf(666L), world.getDelay());
    }

    @Test
    public void testGetRequestFields() {
        Assert.assertEquals("POST", execute("exchange.request.method"));
        Assert.assertEquals("/request/path", execute("exchange.request.path"));
        Assert.assertEquals("bar", execute("exchange.request.getParams('foo').get(0)"));
        Assert.assertEquals("bar", execute("exchange.request.getParam('foo')"));
        Assert.assertEquals("text/plain", execute("exchange.request.getHeader('content-type')")); // ensure case insensitive
        Assert.assertEquals("request body", execute("exchange.request.body"));
    }

    @Test
    public void testGetResponseFields() {
        Assert.assertEquals(Integer.valueOf(200), execute("exchange.response.status"));
        Assert.assertEquals("application/json", execute("exchange.response.getHeader('content-type')")); // ensure case insensitive
        Assert.assertEquals("response body", execute("exchange.response.body"));
    }

    @Test
    public void testSetResponseFields() {
        execute("exchange.response.status = 501");
        Assert.assertEquals(Integer.valueOf(501), world.getResponse().getStatus());

        execute("exchange.response.removeHeaders('Content-Type'); exchange.response.setHeader('Content-Type', 'text/xml')");
        Assert.assertEquals("text/xml", world.getResponse().getHeader("content-type")); // ensure case insensitive

        execute("exchange.response.removeHeaders('Content-Type')");
        Assert.assertNull(world.getResponse().getHeader("content-type"));

        execute("exchange.response.body = 'foo'");
        Assert.assertEquals("foo", world.getResponse().getBody());
    }

    @Test
    public void testGetRequestJsonBody() {
        givenJsonRequestBody();
        Assert.assertEquals("one", execute("exchange.request.body.items.get(0)"));
        Assert.assertEquals("two", execute("exchange.request.body.items.get(1)"));
    }

    @Test
    public void testSetRequestJsonBody() {
        givenJsonRequestBody();
        execute("exchange.request.body.items.add('three')");
        Assert.assertEquals(2, testBean.getItems().size()); // assert original not changed
        Assert.assertEquals(3, ((TestBean) world.getRequest().getBody()).getItems().size());
    }

    @Test
    public void testGetResponseJsonBody() {
        givenJsonResponseBody();
        Assert.assertEquals("one", execute("exchange.response.body.items.get(0)"));
        Assert.assertEquals("two", execute("exchange.response.body.items.get(1)"));
    }

    @Test
    public void testSetResponseJsonBody() {
        givenJsonResponseBody();
        execute("exchange.response.body.items.add('three')");
        Assert.assertEquals(2, testBean.getItems().size()); // assert original not changed
        Assert.assertEquals(3, ((TestBean) world.getResponse().getBody()).getItems().size());
    }

    @Test
    public void testGroovyGetRequestFields() {
        exchange.setScriptType("groovy");
        Assert.assertEquals("POST", execute("exchange.request.method"));
        Assert.assertEquals("/request/path", execute("exchange.request.path"));
        Assert.assertEquals("bar", execute("exchange.request.getParams('foo').get(0)"));
        Assert.assertEquals("bar", execute("exchange.request.getParam('foo')"));
        Assert.assertEquals("text/plain", execute("exchange.request.getHeader('content-type')")); // ensure case insensitive
        Assert.assertEquals("request body", execute("exchange.request.body"));
    }

    @Test
    public void testGroovyGetResponseFields() {
        exchange.setScriptType("groovy");
        Assert.assertEquals(Integer.valueOf(200), execute("exchange.response.status"));
        Assert.assertEquals("application/json", execute("exchange.response.getHeader('content-type')")); // ensure case insensitive
        Assert.assertEquals("response body", execute("exchange.response.body"));
    }
}
