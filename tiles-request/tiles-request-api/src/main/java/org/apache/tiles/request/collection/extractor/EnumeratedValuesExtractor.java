package org.apache.tiles.request.collection.extractor;

import java.util.Enumeration;

public interface EnumeratedValuesExtractor extends HasAddableKeys<String> {

    Enumeration<String> getValues(String key);
}
