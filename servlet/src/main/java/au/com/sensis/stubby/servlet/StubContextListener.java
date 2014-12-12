package au.com.sensis.stubby.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import au.com.sensis.stubby.service.StubService;
import au.com.sensis.stubby.service.StubServiceImpl;

/**
 * This class provides a context listener that adds and removes the stub server.
 */
public class StubContextListener implements ServletContextListener {
    /**
     * The name of the initialisation parameter that holds the name of the file
     * to load a list of stubbed exchanges from.
     */
    public static final String RESPONSES_INIT_PARAMETER = "responses";
    /**
     * The name of the attribute holding the stub service.
     */
    public static final String STUB_SERVICE_ATTRIBUTE = "stubService";

    /**
     * Add the stub server to servlet context.
     *
     * @param event the servlet context event
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        ServletContextResourceResolver resolver = new ServletContextResourceResolver();
        resolver.setServletContext(context);

        StubService stubService = new StubServiceImpl(resolver);
        context.setAttribute(STUB_SERVICE_ATTRIBUTE, stubService);

        String filename = context.getInitParameter(RESPONSES_INIT_PARAMETER);
        if (filename != null) {
            stubService.loadStubbedExchanges(filename);
        }
    }

    /**
     * Remove the stub server from the servlet content.
     *
     * @param event the servlet context event
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.removeAttribute(STUB_SERVICE_ATTRIBUTE);
    }
}
