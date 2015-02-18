package au.com.sensis.stubby.test;

import org.junit.Assert;
import org.junit.Test;

import au.com.sensis.stubby.test.client.GenericClientResponse;
import au.com.sensis.stubby.test.support.TestBase;

public class ScriptTest extends TestBase {

    private static final String TEST_SCRIPT =
            "if (request.getParam('run') == 'true') { exchange.response.status = 202; exchange.response.body = exchange.request.getParam('run'); }";

    private void givenTestScript() {
        builder().setRequestPath("/script/bar").setResponseStatus(201).setResponseBody("original").setScript(TEST_SCRIPT).stub();
    }

    @Test
    public void testRunFalse() {
        givenTestScript();

        GenericClientResponse result = client.executeGet("/script/bar?run=false");

        Assert.assertEquals(201, result.getStatus());
        Assert.assertEquals("original", result.getText());
    }

    @Test
    public void testRunTrue() {
        givenTestScript();

        GenericClientResponse result = client.executeGet("/script/bar?run=true");

        Assert.assertEquals(202, result.getStatus());
        Assert.assertEquals("true", result.getText());
    }
}
