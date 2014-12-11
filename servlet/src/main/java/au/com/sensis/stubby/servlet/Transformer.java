package au.com.sensis.stubby.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import au.com.sensis.stubby.model.StubParam;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.model.StubResponse;
import au.com.sensis.stubby.utils.JsonUtils;

/*
 * Transform between stubby & Servlet HTTP structures
 */
public class Transformer {

    @SuppressWarnings("unchecked")
    public static List<StubParam> fromServletHeaders(final HttpServletRequest request) {
        List<StubParam> stubParams = new  ArrayList<StubParam>();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                stubParams.add(new StubParam(headerName, headerValues.nextElement()));
            }
        }
        return stubParams;
    }

    @SuppressWarnings("unchecked")
    public static List<StubParam> fromServletParams(final HttpServletRequest request) {
        List<StubParam> stubParams = new ArrayList<StubParam>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            for (String paramValue : request.getParameterValues(paramName)) {
                stubParams.add(new StubParam(paramName, paramValue));
            }
        }
        return stubParams;
    }

    public static StubRequest fromServletRequest(final HttpServletRequest request) {
        StubRequest stubRequest = new StubRequest();
        try {
            stubRequest.setPath(new URI(request.getRequestURL().toString()).getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String method = request.getMethod().toUpperCase(); // method should always be upper-case
        stubRequest.setMethod(method);
        if (!"GET".equals(method)) { // GET requests don't (shouldn't) have a body or content type
            try {
                stubRequest.setBody(IOUtils.toString(request.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        stubRequest.setParams(fromServletParams(request));
        stubRequest.setHeaders(fromServletHeaders(request));
        return stubRequest;
    }

    public static void populateServletResponse(final StubResponse stubResponse, final HttpServletResponse response) throws IOException {
        if (stubResponse.getHeaders() != null) {
            for (StubParam header : stubResponse.getHeaders()) {
                response.addHeader(header.getName(), header.getValue());
            }
        }
        response.setStatus(stubResponse.getStatus());
        if (stubResponse.getBody() instanceof String) {
            IOUtils.write(stubResponse.getBody().toString(), response.getOutputStream());
        } else {
            JsonUtils.serialize(response.getOutputStream(), stubResponse.getBody()); // assume deserialised JSON (ie, a Map)
        }
    }

    private Transformer() {
        super();
    }
}
