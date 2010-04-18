package org.apache.tiles.request.collection.extractor;

import java.util.Enumeration;

public interface HasKeys<V> {

    Enumeration<String> getKeys();

    V getValue(String key);
}
