package au.com.sensis.stubby.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import au.com.sensis.stubby.service.JsonServiceInterface;
import au.com.sensis.stubby.service.StubService;
import au.com.sensis.stubby.utils.JsonUtils;

/*
 * Base servlet class that provides access to a 'StubService' instance
 */
@SuppressWarnings("serial")
public abstract class AbstractStubServlet extends HttpServlet {

    public static final String SERVICE_CONTEXT_KEY = "stubby.StubService";

    private ObjectMapper mapper = JsonUtils.defaultMapper();

    protected StubService service() {
        StubService service = (StubService) getServletContext().getAttribute(SERVICE_CONTEXT_KEY);
        if (service == null) {
            throw new IllegalStateException("Service not created");
        }
        return service;
    }

    protected JsonServiceInterface jsonService() {
        return new JsonServiceInterface(service());
    }

    protected void returnOk(final HttpServletResponse response) throws IOException {
        response.setStatus(HttpURLConnection.HTTP_OK);
        response.getOutputStream().close(); // no body
    }

    protected void returnString(final HttpServletResponse response, final String message) throws IOException {
        response.setContentType("text/plain");
        Writer writer = response.getWriter();
        writer.write(message);
        writer.close();
    }

    protected void returnNotFound(final HttpServletResponse response, final String message) throws IOException {
        response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
        returnString(response, message);
    }

    protected void returnError(final HttpServletResponse response, final String message) throws IOException {
        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        returnString(response, message);
    }

    protected void returnJson(final HttpServletResponse response, final Object model) throws IOException {
        response.setStatus(HttpURLConnection.HTTP_OK);
        response.setHeader("Content-Type", "application/json");
        OutputStream stream = response.getOutputStream();
        mapper.writeValue(stream, model);
        stream.close();
    }

    /*
     * Get everything after the servlet path and convert to an integer (eg, '/_control/requests/99' => 99)
     */
    protected int getId(final HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1); // trim slash
        }
        return Integer.parseInt(pathInfo);
    }
}
