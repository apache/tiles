package org.apache.tiles.request.velocity.extractor;

import java.util.Enumeration;

import org.apache.tiles.request.collection.extractor.AttributeExtractor;
import org.apache.velocity.context.Context;

public class VelocityScopeExtractor implements AttributeExtractor {

    private Context context;

    public VelocityScopeExtractor(Context context) {
        this.context = context;
    }

    @Override
    public void removeValue(String name) {
        context.remove(name);
    }

    @Override
    public Enumeration<String> getKeys() {
        return new KeyEnumeration(context.getKeys());
    }

    @Override
    public Object getValue(String key) {
        return context.get(key);
    }

    @Override
    public void setValue(String key, Object value) {
        context.put(key, value);
    }

    private static class KeyEnumeration implements Enumeration<String> {

        private int index = 0;

        private Object[] keys;

        public KeyEnumeration(Object[] keys) {
            this.keys = keys;
        }

        @Override
        public boolean hasMoreElements() {
            return index < keys.length;
        }

        @Override
        public String nextElement() {
            return (String) keys[index++];
        }
    }
}
