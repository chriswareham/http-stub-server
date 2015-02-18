package au.com.sensis.stubby.test.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public abstract class GenericClient {

    private static final int MAX_CONNECTIONS = 10;

    private URI baseUri;
    private HttpClient httpClient;

    public GenericClient(final String baseUri) {
        try {
            this.baseUri = new URI(baseUri);
            this.httpClient = new DefaultHttpClient(createConnectionManager());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI", e);
        }
    }

    public void close() {
        HttpClientUtils.closeQuietly(httpClient);
    }

    public String makeUri(final String path) {
        return baseUri.resolve(path).toString();
    }

    public GenericClientResponse executePost(final String path, final String body, final ContentType contentType) {
        HttpPost request = new HttpPost(makeUri(path));
        request.setEntity(new StringEntity(body, contentType));
        return execute(request);
    }

    public GenericClientResponse executeDelete(final String path) {
        HttpDelete request = new HttpDelete(makeUri(path));
        return execute(request);
    }

    public GenericClientResponse executeGet(final String path) {
        HttpGet request = new HttpGet(makeUri(path));
        return execute(request);
    }

    public GenericClientResponse execute(final HttpUriRequest request) {
        try {
           return new GenericClientResponse(httpClient.execute(request)); // consumes & releases connection
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error executing '%s' to '%s'", request.getMethod(), request.getURI()), e);
        }
    }

    private static ClientConnectionManager createConnectionManager() {
        PoolingClientConnectionManager manager = new PoolingClientConnectionManager();
        manager.setDefaultMaxPerRoute(MAX_CONNECTIONS);
        manager.setMaxTotal(MAX_CONNECTIONS);
        return manager;
    }
}
