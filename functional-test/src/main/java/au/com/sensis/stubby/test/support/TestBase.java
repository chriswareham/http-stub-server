package au.com.sensis.stubby.test.support;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;

import au.com.sensis.stubby.test.client.Client;
import au.com.sensis.stubby.test.client.GenericClientResponse;
import au.com.sensis.stubby.test.client.MessageBuilder;
import au.com.sensis.stubby.test.model.JsonMessage;
import au.com.sensis.stubby.test.model.JsonPair;
import au.com.sensis.stubby.test.model.JsonStubbedExchangeList;

public abstract class TestBase {

    private static final String P_TEST_SERVER = "test.server"; // server to test against

    protected Client client;

    @Before
    public void before() {
        String testServer = System.getProperty(P_TEST_SERVER);

        client = new Client(testServer);
        client.reset();
    }

    @After
    public void after() {
        client.close();
    }

    protected void postFile(final String filename) {
        String path = "/tests/" + filename;
        InputStream resource = getClass().getResourceAsStream(path);
        if (resource != null) {
            try {
                client.postMessage(IOUtils.toString(resource));
            } catch (IOException e) {
                throw new RuntimeException("Error posting file", e);
            }
        } else {
            throw new RuntimeException("Resource not found: " + path);
        }
    }

    protected String makeUri(final String path) {
        return client.makeUri(path);
    }

    protected void close(final HttpResponse response) {
        HttpClientUtils.closeQuietly(response);
    }

    protected GenericClientResponse execute(final HttpUriRequest request) {
        return client.execute(request);
    }

    protected Client client() {
        return client;
    }

    protected JsonStubbedExchangeList responses() {
        return client.getResponses();
    }

    protected void assertOk(final GenericClientResponse response) {
        assertStatus(HttpStatus.SC_OK, response);
    }

    protected void assertNotFound(final GenericClientResponse response) {
        assertStatus(HttpStatus.SC_NOT_FOUND, response);
    }

    protected void assertStatus(final int status, final GenericClientResponse response) {
        Assert.assertEquals(status, response.getStatus());
    }

    protected MessageBuilder builder() {
        return new MessageBuilder(client);
    }

    protected void assertHasHeader(final JsonMessage request, final String name, final String value) {
        for (JsonPair header : request.headers) {
            if (header.name.equalsIgnoreCase(name)
                    && header.value.equals(value)) {
                return;
            }
        }
        Assert.fail();
    }

    protected void assumeNotTravisCi() {
        Assume.assumeFalse("Running as Travis CI", isTravisCi());
    }

    protected boolean isTravisCi() { // set when running under Travis CI (travis-ci.org)
        return "true".equals(System.getenv("TRAVIS"));
    }

    protected void assertTimeTaken(final long started, final long ended, final long expected) {
        double tolerance = 500; // 1/2 second tolerance

        if (!isTravisCi()) { // don't assert timing if running on Travis-CI (it's quite slow...)
            Assert.assertEquals(expected, Math.abs(ended - started), tolerance); // assert delay within tolerance
        }
    }

    protected long now() {
        return System.currentTimeMillis();
    }
}
