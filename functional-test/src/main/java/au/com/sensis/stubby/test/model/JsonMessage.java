package au.com.sensis.stubby.test.model;

public abstract class JsonMessage {

    public JsonPairList headers;
    public Object body;

    public void setHeader(final String name, final String value) {
        if (headers == null) {
            headers = new JsonPairList();
        }
        headers.setIgnoreCase(name, value); // header names are case insensitive
    }

    public void addHeader(final String name, final String value) {
        if (headers == null) {
            headers = new JsonPairList();
        }
        headers.add(name, value);
    }
}
