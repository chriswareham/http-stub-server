package au.com.sensis.stubby.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Handles operations on response collection (eg, 'POST /_control/responses')
 */
@SuppressWarnings("serial")
public class ResponsesServlet extends AbstractStubServlet {

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        returnJson(response, service().getResponses());
    }

    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        service().deleteResponses();
        returnOk(response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        jsonService().addResponse(request.getInputStream());
        returnOk(response);
    }
}
