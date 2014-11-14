package au.com.sensis.stubby.utils;

import java.io.InputStream;
import java.net.URL;

/**
 * This class is a resolver for class path resources.
 */
public class ClassPathResourceResolver implements ResourceResolver {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourcePath(final String name) {
        URL url = getClass().getResource(name);
        return url != null ? url.getPath() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getResource(final String name) {
        return getClass().getResourceAsStream(name);
    }
}
