package org.apache.tiles.request.attribute;

public interface HasRemovableKeys<V> extends HasKeys<V> {
    void removeValue(String name);
}
