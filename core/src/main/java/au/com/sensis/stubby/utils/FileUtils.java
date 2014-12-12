package au.com.sensis.stubby.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This class provides utilities for reading the contents of files to strings.
 */
public final class FileUtils {
    public static final String ENCODING = "UTF-8";
    public static final int BUFSIZ = 8192;

    /**
     * Read the contents of a file to a string.
     *
     * @param path the path of the file to read the contents of
     * @return the contents of the file
     */
    public static String read(String path) {
        try {
            return readInternal(new FileInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + path, e);
        }
    }

    /**
     * Read the contents of a file to a string.
     *
     * @param file the file to read the contents of
     * @return the contents of the file
     */
    public static String read(final File file) {
        try {
            return readInternal(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + file.getName(), e);
        }
    }

    /**
     * Read the contents of an input stream to a string.
     *
     * @param inputStream the input stream to read the contents of
     * @return the contents of the input stream
     */
    public static String read(final InputStream inputStream) {
        try {
            return readInternal(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input stream", e);
        }
    }

    /**
     * Close a source or destination of data, quietly ignoring any exception.
     *
     * @param closeable the source or destination of data to close
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignored
            }
        }
    }

    private static String readInternal(final InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, ENCODING);
        char[] buf = new char[BUFSIZ];
        try {
            for (int n = in.read(buf, 0, BUFSIZ); n != -1; n = in.read(buf, 0, BUFSIZ)) {
                sb.append(buf, 0, n);
            }
        } finally {
            close(in);
        }
        return sb.toString();
    }

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private FileUtils() {
        super();
    }
}
