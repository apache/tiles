package org.apache.tiles.request.collection.extractor;

public interface HasRemovableKeys<V> extends HasKeys<V> {
    void removeValue(String name);
}
