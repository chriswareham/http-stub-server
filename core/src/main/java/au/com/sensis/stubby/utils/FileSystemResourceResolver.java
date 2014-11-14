package au.com.sensis.stubby.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This class is a resolver for class path resources.
 */
public class FileSystemResourceResolver implements ResourceResolver {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourcePath(final String name) {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getResource(final String name) {
        try {
            return new FileInputStream(name);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File " + name + " not found", e);
        }
    }
}
