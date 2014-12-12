package au.com.sensis.stubby.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CopyUtils {

    @SuppressWarnings("unchecked")
    public static <T> T copy(T src) {
        if (src == null) {
            return null;
        }

        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            new ObjectOutputStream(outStream).writeObject(src);
            ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
            return (T) new ObjectInputStream(inStream).readObject();
        } catch (IOException e) {
            throw new RuntimeException("Copy failed", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found", e);
        }
    }

    private CopyUtils() {
        super();
    }
}
