package au.com.sensis.stubby.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import au.com.sensis.stubby.service.StubService;

public class StubContextListener implements ServletContextListener {
    private static final String RESPONSES_INIT_PARAMETER = "responses";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        StubService stubService = new StubService();

        ServletContext context = event.getServletContext();
        context.setAttribute(AbstractStubServlet.SERVICE_CONTEXT_KEY, stubService);
        ServletContextResourceResolver resolver = new ServletContextResourceResolver();
        resolver.setServletContext(context);
        String filename = context.getInitParameter(RESPONSES_INIT_PARAMETER);
        if (filename != null) {
            stubService.loadResponses(resolver, filename);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.removeAttribute(AbstractStubServlet.SERVICE_CONTEXT_KEY);
        context.removeAttribute(RESPONSES_INIT_PARAMETER);
    }
}
