package au.com.sensis.stubby.test;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Test;

import au.com.sensis.stubby.test.model.JsonPair;
import au.com.sensis.stubby.test.model.JsonRequest;
import au.com.sensis.stubby.test.model.JsonRequestList;
import au.com.sensis.stubby.test.support.TestBase;

public class ControlRequestTest extends TestBase {

    @Test
    public void testGetRequestOrder() {
        client.executeGet("/test/1");
        client.executeGet("/test/2");

        Assert.assertEquals("/test/2", client.getRequest(0).path);
        Assert.assertEquals("/test/1", client.getRequest(1).path);
    }

    @Test
    public void testGetRequestsOrder() {
        client.executeGet("/test/1");
        client.executeGet("/test/2");

        JsonRequestList requests = client.getRequests();

        Assert.assertEquals(2, requests.size());
        Assert.assertEquals("/test/2", requests.get(0).path);
        Assert.assertEquals("/test/1", requests.get(1).path);
    }

    @Test
    public void testGetRequestNotFound() {
        client.executeGet("/test/1");

        Assert.assertEquals(200, client.executeGet("/_control/requests/0").getStatus());
        Assert.assertEquals(404, client.executeGet("/_control/requests/1").getStatus());
    }

    @Test
    public void testDeleteRequests() {
        client.executeGet("/test/1");
        Assert.assertEquals(1, client.getRequests().size());

        client.deleteRequests();
        Assert.assertEquals(0, client.getRequests().size());
    }

    @Test
    public void testRequestDetails() {
        HttpPost post = new HttpPost(client.makeUri("/test/path?foo=bar1&foo=bar2"));
        post.addHeader("X-Foo", "bar1");
        post.addHeader("X-Foo", "bar2");
        post.setEntity(new StringEntity("Testing content", ContentType.TEXT_PLAIN));
        client.execute(post);

        JsonRequest request = client.getRequest(0);

        Assert.assertEquals("POST", request.method);
        Assert.assertEquals("/test/path", request.path);

        Assert.assertEquals(2, request.params.size());
        Assert.assertTrue(request.params.contains(new JsonPair("foo", "bar1")));
        Assert.assertTrue(request.params.contains(new JsonPair("foo", "bar2")));

        assertHasHeader(request, "X-Foo", "bar1");
        assertHasHeader(request, "X-Foo", "bar2");

        Assert.assertEquals("Testing content", request.body);
    }
}
