package au.com.sensis.stubby.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class provides utilities for reading the contents of files to strings.
 */
public final class FileUtils {
    /**
     * Read the contents of a file to a string.
     *
     * @param path the path of the file to read the contents of
     * @return the contents of the file
     * @throws RuntimeException if the file cannot be read
     */
    public static String read(String path) throws RuntimeException {
        return read(new File(path));
    }

    /**
     * Read the contents of a file to a string.
     *
     * @param file the file to read the contents of
     * @return the contents of the file
     * @throws RuntimeException if the file cannot be read
     */
    public static String read(File file) throws RuntimeException {
        FileInputStream in = null;
        try {
            if (file.length() > Integer.MAX_VALUE) {
                throw new IOException("File too large");
            }
            in = new FileInputStream(file);
            byte[] b = new byte[(int) file.length()];
            for (int count = 0; count < b.length;) {
                int n = in.read(b, count, b.length - count);
                if (n == -1) {
                    break;
                }
                count += n;
            }
            return new String(b , "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + file.getName(), e);
        } finally {
            close(in);
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

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private FileUtils() {
        super();
    }
}
