package au.com.sensis.stubby.test.model;

import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("serial")
public class JsonPairList extends ArrayList<JsonPair> {

    public void add(final String name, final String value) {
        add(new JsonPair(name, value));
    }

    public void set(final String name, final String value) {
        delete(name);
        add(name, value);
    }

    public void setIgnoreCase(final String name, final String value) {
        deleteIgnoreCase(name);
        add(name, value);
    }

    public void delete(final String name) {
        Iterator<JsonPair> iterator = iterator();
        while (iterator.hasNext() && iterator.next().name.equals(name)) {
            iterator.remove();
        }
    }

    public void deleteIgnoreCase(final String name) {
        Iterator<JsonPair> iterator = iterator();
        while (iterator.hasNext() && iterator.next().name.equalsIgnoreCase(name)) {
            iterator.remove();
        }
    }
}
