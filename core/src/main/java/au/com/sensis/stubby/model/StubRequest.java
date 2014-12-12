package au.com.sensis.stubby.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * This class describes a stubbed request.
 */
public class StubRequest extends StubMessage {
    /**
     * The request method.
     */
    private String method;
    /**
     * The request path.
     */
    private String path;
    /**
     * The request parameters.
     */
    private List<StubParam> params;

    /**
     * Default constructor.
     */
    public StubRequest() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param request the request to copy
     */
    public StubRequest(final StubRequest request) {
        super(request);
        this.method = request.method;
        this.path = request.path;
        if (request.params != null) {
            params = new ArrayList<StubParam>();
            for (StubParam param : request.params) {
                params.add(new StubParam(param));
            }
        }
    }

    /**
     * Get the request method.
     *
     * @return the request method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Set the request method.
     *
     * @param method the request method
     */
    public void setMethod(final String method) {
        this.method = method;
    }

    /**
     * Get the request path.
     *
     * @return the request path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the request path.
     *
     * @param path the request path
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Get the request parameters.
     *
     * @return the request parameters
     */
    public List<StubParam> getParams() {
        return params != null ? Collections.unmodifiableList(params) : null;
    }

    /**
     * Set the request parameters.
     *
     * @param params the request parameters
     */
    public void setParams(final List<StubParam> params) {
        if (params == null) {
            this.params = null;
        } else {
            this.params = new ArrayList<StubParam>();
            for (StubParam param : params) {
                this.params.add(new StubParam(param));
            }
        }
    }

    /**
     * Set a parameter.
     *
     * @param name the parameter name
     * @param value the parameter value
     */
    @JsonIgnore
    public void setParam(final String name, final String value) {
        if (params == null) {
            params = new ArrayList<StubParam>();
        }
        params.add(new StubParam(name, value));
    }

    /**
     * Get the value of the first parameter with a specific name.
     *
     * @param name the parameter name (case-insensitive)
     * @return the value of the first parameter with the name, or null if there are none
     */
     @JsonIgnore
    public String getParam(final String name) {
        if (params != null) {
            for (StubParam param : params) {
                if (param.getName().equals(name)) {
                    return param.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Get the values of all parameters with a specific name.
     *
     * @param name the parameter name (case-insensitive)
     * @return the values of all the parameters with the name
     */
    @JsonIgnore
    public List<String> getParams(final String name) {
        List<String> l = new ArrayList<String>();
        if (params != null) {
            for (StubParam param : params) {
                if (param.getName().equals(name)) {
                    l.add(param.getValue());
                }
            }
        }
        return l;
    }

    /**
     * Remove all parameters with a specific name.
     *
     * @param name the parameter name (case-insensitive)
     */
    @JsonIgnore
    public void removeParams(final String name) {
        if (params != null) {
            for (Iterator<StubParam> i = params.iterator(); i.hasNext();) {
                if (i.next().getName().equalsIgnoreCase(name)) {
                    i.remove();
                }
            }
        }
    }
}
