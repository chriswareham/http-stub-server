package au.com.sensis.stubby.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class StubRequest extends StubMessage {

    private String method;

    private String path;

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

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public List<StubParam> getParams() {
        return params != null ? Collections.unmodifiableList(params) : null;
    }

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

    @JsonIgnore
    public String getParam(final String name) { // get first, case sensitive lookup
        if (params != null) {
            for (StubParam param : params) {
                if (param.getName().equals(name)) {
                    return param.getValue();
                }
            }
        }
        return null;
    }

    @JsonIgnore
    public List<String> getParams(final String name) { // get all, case sensitive lookup
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
}
