package au.com.sensis.stubby.test;

import org.junit.Assert;
import org.junit.Test;

import au.com.sensis.stubby.test.client.MessageBuilder;
import au.com.sensis.stubby.test.model.JsonExchange;
import au.com.sensis.stubby.test.model.JsonPair;
import au.com.sensis.stubby.test.model.JsonRequest;
import au.com.sensis.stubby.test.model.JsonResponse;
import au.com.sensis.stubby.test.model.JsonStubbedExchangeList;
import au.com.sensis.stubby.test.support.TestBase;

public class ControlResponseTest extends TestBase {

    private MessageBuilder builder1() {
        return builder().setRequestPath("/test/1").setResponseStatus(200);
    }

    private MessageBuilder builder2() {
        return builder()
            .setRequestMethod("PUT")
            .setRequestPath("/test/2")
            .addRequestParam("foo", "bar1")
            .addRequestParam("foo", "bar2")
            .addRequestHeader("X-Request-Foo", "bar1")
            .addRequestHeader("X-Request-Foo", "bar2")
            .setRequestBody("request body")
            .addResponseHeader("X-Response-Foo", "bar1")
            .addResponseHeader("X-Response-Foo", "bar2")
            .setResponseStatus(201)
            .setResponseBody("response body")
            .setScript("script();")
            .setDelay(1234L);
    }

    @Test
    public void testGetPatternDetails() {
        builder2().stub();

        JsonExchange exchange = client.getResponse(0).exchange;
        JsonRequest pattern = exchange.request;

        Assert.assertEquals("PUT", pattern.method);
        Assert.assertEquals("/test/2", pattern.path);
        Assert.assertTrue(pattern.params.contains(new JsonPair("foo", "bar1")));
        Assert.assertTrue(pattern.params.contains(new JsonPair("foo", "bar2")));
        assertHasHeader(pattern, "X-Request-Foo", "bar1");
        assertHasHeader(pattern, "X-Request-Foo", "bar2");
        Assert.assertEquals("request body", pattern.body);
    }

    @Test
    public void testGetResponseDetails() {
        builder2().stub();

        JsonExchange exchange = client.getResponse(0).exchange;
        JsonResponse response = exchange.response;

        Assert.assertEquals(Integer.valueOf(201), response.status);
        assertHasHeader(response, "X-Response-Foo", "bar1");
        assertHasHeader(response, "X-Response-Foo", "bar2");
        Assert.assertEquals("response body", response.body);
    }

    @Test
    public void testGetOtherDetails() {
        builder2().stub();

        JsonExchange exchange = client.getResponse(0).exchange;

        Assert.assertEquals(Long.valueOf(1234L), exchange.delay);
        Assert.assertEquals("script();", exchange.script);
    }

    @Test
    public void testGetResponseOrder() {
        builder1().stub();
        builder2().stub();

        Assert.assertEquals("/test/2", client.getResponse(0).exchange.request.path);
        Assert.assertEquals("/test/1", client.getResponse(1).exchange.request.path);
    }

    @Test
    public void testGetResponsesOrder() {
        builder1().stub();
        builder2().stub();

        JsonStubbedExchangeList responses = client.getResponses();

        Assert.assertEquals(2, responses.size());
        Assert.assertEquals("/test/2", responses.get(0).exchange.request.path);
        Assert.assertEquals("/test/1", responses.get(1).exchange.request.path);
    }

    @Test
    public void testGetResponseNotFound() {
        builder1().stub();

        Assert.assertEquals(200, client.executeGet("/_control/responses/0").getStatus());
        Assert.assertEquals(404, client.executeGet("/_control/responses/1").getStatus());
    }

    @Test
    public void testDeleteResponses() {
        builder1().stub();
        Assert.assertEquals(1, client.getResponses().size());

        client.deleteResponses();
        Assert.assertEquals(0, client.getResponses().size());
    }

    /*
    @Test
    public void testDeleteSingleResponse() {
        builder1().stub();
        builder2().stub();

        Assert.assertEquals("/test/2", client.getResponse(0).exchange.request.path);

        client.deleteResponse(0);
        Assert.assertEquals("/test/1", client.getResponse(1).exchange.request.path);

        client.deleteResponse(0);
        Assert.assertEquals(0, client.getResponses().size());
    }

    @Test
    public void testDeleteNotFound() {
        builder1().stub();

        Assert.assertEquals(404, client.executeDelete("/_control/responses/1").getStatus());
        Assert.assertEquals(200, client.executeDelete("/_control/responses/0").getStatus());
    }
    */
}
