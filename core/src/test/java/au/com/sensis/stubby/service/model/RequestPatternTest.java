package au.com.sensis.stubby.service.model;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import au.com.sensis.stubby.model.StubParam;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.utils.ClassPathResourceResolver;
import au.com.sensis.stubby.utils.ResourceResolver;

public class RequestPatternTest {

    private static final List<StubParam> EMPTY_PARAMS = Collections.emptyList();

    private StubRequest stubbedRequest;
    private StubRequest incomingRequest;

    private ResourceResolver resolver;
    private RequestPattern instance1;
    private RequestPattern instance2;

    @Before
    public void before() {
        stubbedRequest = new StubRequest();
        stubbedRequest.setMethod("PO.*");
        stubbedRequest.setPath("/request/.*");
        stubbedRequest.setParams(Arrays.asList(new StubParam("foo", "b.r")));
        stubbedRequest.setHeaders(Arrays.asList(new StubParam("Content-Type", "text/plain; .+")));
        stubbedRequest.setBody("body .*");

        incomingRequest = new StubRequest();
        incomingRequest.setMethod("POST");
        incomingRequest.setPath("/request/path");
        incomingRequest.setParams(Arrays.asList(new StubParam("foo", "bar")));
        incomingRequest.setHeaders(Arrays.asList(new StubParam("Content-Type", "text/plain; charset=UTF-8")));
        incomingRequest.setBody("body pattern");

        resolver = new ClassPathResourceResolver();
        instance1 = new RequestPattern(resolver, stubbedRequest);
        instance2 = new RequestPattern(resolver, stubbedRequest);
    }

    private void assertNotFound(MatchResult result, FieldType type, String name, String expected) {
        MatchField expectedField = new MatchField(type, name, expected).asNotFound();

        assertFalse(result.matches());
        assertThat(result.getFields(), hasItem(expectedField));
    }

    private void assertMatchFailure(MatchResult result, FieldType type, String name, String expected, String actual) {
        MatchField expectedField = new MatchField(type, name, expected).asMatchFailure(actual);

        assertFalse(result.matches());
        assertThat(result.getFields(), hasItem(expectedField));
    }

    private void assertMatchSuccess(MatchResult result, FieldType type, String name, String expected, String actual) {
        MatchField expectedField = new MatchField(type, name, expected).asMatch(actual);

        assertTrue(result.matches());
        assertThat(result.getFields(), hasItem(expectedField));
    }

    @Test
    public void testEquality() {
        assertEquals(instance1, instance2);
    }

    @Test
    public void testHashCode() {
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testFromPattern() {
        assertEquals("PO.*", instance1.getMethod().pattern());
        assertEquals("/request/.*", instance1.getPath().pattern());

        assertEquals(1, instance1.getParams().size());
        assertEquals("foo", instance1.getParams().iterator().next().getName());
        assertEquals("b.r", instance1.getParams().iterator().next().getPattern().pattern());

        assertEquals(1, instance1.getHeaders().size());
        assertEquals("Content-Type", instance1.getHeaders().iterator().next().getName());
        assertEquals("text/plain; .+", instance1.getHeaders().iterator().next().getPattern().pattern());

        assertEquals(new TextBodyPattern("body .*"), instance1.getBody());
    }

    @Test
    public void testFromEmptyPattern() {
        instance1 = new RequestPattern(resolver, new StubRequest());

        assertEquals(".*", instance1.getMethod().pattern());
        assertEquals(".*", instance1.getPath().pattern());
        assertNull(instance1.getBody());
    }

    @Test
    public void testJsonBodyPatternObject() {
        Map<String,String> pattern = new HashMap<String,String>();
        pattern.put("foo", "bar");
        stubbedRequest.setBody(pattern);

        instance1 = new RequestPattern(resolver, stubbedRequest);

        assertEquals(new JsonBodyPattern(pattern), instance1.getBody());
    }

    @Test
    public void testJsonBodyPatternList() {
        List<String> pattern = Arrays.asList("foo", "bar");
        stubbedRequest.setBody(pattern);

        instance1 = new RequestPattern(resolver, stubbedRequest);

        assertEquals(new JsonBodyPattern(pattern), instance1.getBody());
    }

    @Test
    public void testMatches() {
        MatchResult result = instance1.match(incomingRequest);

        assertTrue(result.matches());

        assertMatchSuccess(result, FieldType.METHOD, "method", "PO.*", "POST");
        assertMatchSuccess(result, FieldType.PATH, "path", "/request/.*", "/request/path");
        assertMatchSuccess(result, FieldType.BODY, "body", "body .*", "body pattern");
        assertMatchSuccess(result, FieldType.QUERY_PARAM, "foo", "b.r", "bar");
        assertMatchSuccess(result, FieldType.HEADER, "Content-Type", "text/plain; .+", "text/plain; charset=UTF-8");
    }

    @Test
    public void testMatchesExtraParams() {
        incomingRequest.setParams(Arrays.asList(
                new StubParam("foo", "bar"),
                new StubParam("foo", "asdfasdf")));

        MatchResult result = instance1.match(incomingRequest);

        assertTrue(result.matches());
    }

    @Test
    public void testNoMatchWrongParams() {
        incomingRequest.setParams(Arrays.asList(new StubParam("foo", "asdf")));

        MatchResult result = instance1.match(incomingRequest);

        assertMatchFailure(result, FieldType.QUERY_PARAM, "foo", "b.r", "asdf");
    }

    @Test
    public void testNoMatchNoParams() {
        incomingRequest.setParams(EMPTY_PARAMS);

        MatchResult result = instance1.match(incomingRequest);

        assertNotFound(result, FieldType.QUERY_PARAM, "foo", "b.r");
    }

    @Test
    public void testMatchesExtraHeaders() {
        incomingRequest.setHeaders(Arrays.asList(
                new StubParam("Content-Type", "text/plain; .+"),
                new StubParam("Content-Type", "application/json")));

        MatchResult result = instance1.match(incomingRequest);

        assertTrue(result.matches());
    }

    @Test
    public void testNoMatchWrongHeaders() {
        incomingRequest.setHeaders(Arrays.asList(new StubParam("Content-Type", "image/gif")));

        MatchResult result = instance1.match(incomingRequest);

        assertMatchFailure(result, FieldType.HEADER, "Content-Type", "text/plain; .+", "image/gif");
    }

    @Test
    public void testNoMatchNoHeaders() {
        incomingRequest.setHeaders(EMPTY_PARAMS);

        MatchResult result = instance1.match(incomingRequest);

        assertNotFound(result, FieldType.HEADER, "Content-Type", "text/plain; .+");
    }

    @Test
    public void testNoMatchWrongBody() {
        incomingRequest.setBody("wrong body");

        MatchResult result = instance1.match(incomingRequest);

        assertMatchFailure(result, FieldType.BODY, "body", "body .*", "wrong body");
    }

    @Test
    public void testNoMatchNoBody() {
        incomingRequest.setBody(null); // no body

        MatchResult result = instance1.match(incomingRequest);

        assertNotFound(result, FieldType.BODY, "body", "<pattern>");
    }

    @Test
    public void testNoMatchWrongMethod() {
        incomingRequest.setMethod("HEAD");

        MatchResult result = instance1.match(incomingRequest);

        assertMatchFailure(result, FieldType.METHOD, "method", "PO.*", "HEAD");
    }

}
