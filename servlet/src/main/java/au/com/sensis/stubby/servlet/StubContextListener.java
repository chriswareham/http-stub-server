package au.com.sensis.stubby.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import au.com.sensis.stubby.service.StubService;

/**
 * This class is a context listener that initialises and destroys the stub server.
 */
public class StubContextListener implements ServletContextListener {
    /**
     * The name of the initialisation parameter that holds the exchange filename.
     */
    private static final String RESPONSES_INIT_PARAMETER = "responses";

    /**
     * Initialise the stub server.
     *
     * @param event the servlet context event
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
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

    /**
     * Destroy the stub server.
     *
     * @param event the servlet context event
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.removeAttribute(AbstractStubServlet.SERVICE_CONTEXT_KEY);
        context.removeAttribute(RESPONSES_INIT_PARAMETER);
    }
}
