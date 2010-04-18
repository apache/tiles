package org.apache.tiles.request.collection.extractor;

import java.util.Enumeration;

public interface HeaderExtractor extends HasAddableKeys<String> {

    Enumeration<String> getValues(String key);
}
