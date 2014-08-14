package au.com.sensis.stubby.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class provides utilities for reading the contents of files to strings.
 */
public class FileUtils {
    /**
     * Read the contents of a file to a string.
     *
     * @param path the path of the file to read the contents of
     * @return the contents of the file
     * @throws RuntimeException if the file cannot be read
     */
    public static String read(String path) throws RuntimeException {
        FileInputStream in = null;
        try {
            File file = new File(path);
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
            throw new RuntimeException("Failed to read file " + path, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
    }
}
