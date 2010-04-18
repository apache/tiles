package org.apache.tiles.request.collection.extractor;

public interface HasAddableKeys<V> extends HasKeys<V> {
    void setValue(String key, V value);
}
