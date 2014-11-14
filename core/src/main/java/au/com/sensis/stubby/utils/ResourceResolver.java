package au.com.sensis.stubby.utils;

import java.io.InputStream;

/**
 * This interface is implemented by classes that can resolve resource names to
 * an input stream.
 */
public interface ResourceResolver {
    /**
     * Get the path of a resource.
     *
     * @param name the name of the resource
     * @return a path, or null if the resource cannot be found
     */
    String getResourcePath(String name);

    /**
     * Get a resource as an input stream.
     *
     * @param name the name of the resource
     * @return an input stream, or null if the resource cannot be found
     */
    InputStream getResource(String name);
}
