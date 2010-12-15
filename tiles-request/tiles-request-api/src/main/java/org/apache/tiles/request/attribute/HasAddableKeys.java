package org.apache.tiles.request.attribute;

public interface HasAddableKeys<V> extends HasKeys<V> {
    void setValue(String key, V value);
}
