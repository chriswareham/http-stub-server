package au.com.sensis.stubby.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.sensis.stubby.model.StubParam;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.utils.RequestFilterBuilder;

/*
 * Handles operations on requets collection (eg, 'DELETE /_control/requests')
 */
@SuppressWarnings("serial")
public class RequestsServlet extends AbstractStubServlet {

    private StubRequest createFilter(final HttpServletRequest request) {
        List<StubParam> params = Transformer.fromServletParams(request);
        return new RequestFilterBuilder().fromParams(params).getFilter();
    }

    private Long getWaitParam(final HttpServletRequest request) {
        String waitStr = request.getParameter("wait");
        if (waitStr != null) {
            return Long.parseLong(waitStr);
        } else {
            return null; // not found
        }
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        StubRequest filter = createFilter(request);
        Long wait = getWaitParam(request);
        if (wait != null && wait > 0) { // don't allow zero wait
            returnJson(response, service().findRequests(filter, wait));
        } else {
            returnJson(response, service().findRequests(filter));
        }
    }

    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        service().deleteRequests();
        returnOk(response);
    }
}
