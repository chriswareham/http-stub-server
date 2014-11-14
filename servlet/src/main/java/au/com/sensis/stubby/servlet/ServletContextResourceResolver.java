package au.com.sensis.stubby.servlet;

import java.io.InputStream;

import javax.servlet.ServletContext;

import au.com.sensis.stubby.utils.ResourceResolver;

/**
 * This class is a resolver for servlet context resources.
 */
public class ServletContextResourceResolver implements ResourceResolver {
    /**
     * The servlet context to resolve resources from.
     */
    private ServletContext servletContext;

    /**
     * Set the servlet context to resolve resources from.
     *
     * @param servletContext the servlet context to resolve resources from
     */
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourcePath(final String name) {
        return servletContext.getRealPath(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getResource(final String name) {
        return servletContext.getResourceAsStream(name);
    }
}
