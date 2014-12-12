package au.com.sensis.stubby.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import au.com.sensis.stubby.utils.CopyUtils;

/**
 * This class provides a superclass for stubbed requests and responses.
 */
public abstract class StubMessage {
    /**
     * The message headers.
     */
    private List<StubParam> headers;
    /**
     * The message body.
     */
    private Object body;
    /**
     * The message filename.
     */
    private String file;

    /**
     * Default constructor.
     */
    protected StubMessage() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param message the message to copy
     */
    protected StubMessage(final StubMessage message) {
        if (message.headers != null) {
            headers = new ArrayList<StubParam>();
            for (StubParam header : message.headers) {
                headers.add(new StubParam(header));
            }
        }
        body = CopyUtils.copy(message.body);
        file = message.file;
    }

    /**
     * Get the message headers.
     *
     * @return the message headers
     */
    public List<StubParam> getHeaders() {
        return headers != null ? Collections.unmodifiableList(headers) : null;
    }

    /**
     * Set the message headers.
     *
     * @param headers the message headers
     */
    public void setHeaders(final List<StubParam> headers) {
        if (headers == null) {
            this.headers = null;
        } else {
            this.headers = new ArrayList<StubParam>();
            for (StubParam header : headers) {
                this.headers.add(new StubParam(header));
            }
        }
    }

    /**
     * Get the message body.
     *
     * @return the message body
     */
    public Object getBody() {
        return body;
    }

    /**
     * Set the message body.
     *
     * @param body the message body
     */
    public void setBody(final Object body) {
        this.body = CopyUtils.copy(body);
    }

    /**
     * Get the message filename.
     *
     * @return the message filename
     */
    public String getFile() {
        return file;
    }

    /**
     * Set the message file.
     *
     * @param file the message file
     */
    public void setFile(final String file) {
        this.file = file;
    }

    /**
     * Set a header.
     *
     * @param name the header name
     * @param value the header value
     */
    @JsonIgnore
    public void setHeader(final String name, final String value) {
        if (headers == null) {
            headers = new ArrayList<StubParam>();
        }
        headers.add(new StubParam(name, value));
    }

    /**
     * Get the value of the first header with a specific name.
     *
     * @param name the header name (case-insensitive)
     * @return the value of the first header with the name, or null if there are none
     */
    @JsonIgnore
    public String getHeader(final String name) {
        if (headers != null) {
            for (StubParam header : headers) {
                if (header.getName().equalsIgnoreCase(name)) {
                    return header.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Get the values of all headers with a specific name.
     *
     * @param name the header name (case-insensitive)
     * @return the values of all the headers with the name
     */
    @JsonIgnore
    public List<String> getHeaders(final String name) {
        List<String> l = new ArrayList<String>();
        if (headers != null) {
            for (StubParam header : headers) {
                if (header.getName().equalsIgnoreCase(name)) {
                    l.add(header.getValue());
                }
            }
        }
        return l;
    }

    /**
     * Remove all headers with a specific name.
     *
     * @param name the header name (case-insensitive)
     */
    @JsonIgnore
    public void removeHeaders(final String name) {
        if (headers != null) {
            for (Iterator<StubParam> i = headers.iterator(); i.hasNext();) {
                if (i.next().getName().equalsIgnoreCase(name)) {
                    i.remove();
                }
            }
        }
    }
}
