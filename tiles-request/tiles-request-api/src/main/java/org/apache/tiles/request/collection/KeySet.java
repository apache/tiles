package org.apache.tiles.request.collection;

import static org.apache.tiles.request.util.RequestUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.tiles.request.attribute.HasKeys;

public class KeySet implements Set<String> {

    private HasKeys<?> request;

    public KeySet(HasKeys<?> request) {
        this.request = request;
    }

    @Override
    public boolean add(String e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        return request.getValue(key(o)) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsAll(Collection<?> c) {
        Collection<String> realCollection = (Collection<String>) c;
        for (String key : realCollection) {
            if (request.getValue(key(key)) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return !request.getKeys().hasMoreElements();
    }

    @Override
    public Iterator<String> iterator() {
        return new ServletHeaderKeySetIterator();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return enumerationSize(request.getKeys());
    }

    @Override
    public Object[] toArray() {
        return toList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return toList().toArray(a);
    }

    private List<String> toList() {
        List<String> entries = new ArrayList<String>();
        Enumeration<String> names = request.getKeys();
        while (names.hasMoreElements()) {
            entries.add(names.nextElement());
        }
        return entries;
    }

    private class ServletHeaderKeySetIterator implements Iterator<String> {

        private Enumeration<String> namesEnumeration = request.getKeys();

        @Override
        public boolean hasNext() {
            return namesEnumeration.hasMoreElements();
        }

        @Override
        public String next() {
            return namesEnumeration.nextElement();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
