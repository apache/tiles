package org.apache.tiles.request.portlet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class StateAwareParameterMap implements Map<String, String[]> {

    private Map<String, String[]> requestMap;

    private Map<String, String[]> responseMap;

    public StateAwareParameterMap(Map<String, String[]> requestMap,
            Map<String, String[]> responseMap) {
        this.requestMap = requestMap;
        this.responseMap = responseMap;
    }

    @Override
    public void clear() {
        responseMap.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return requestMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return requestMap.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<String, String[]>> entrySet() {
        return requestMap.entrySet();
    }

    @Override
    public String[] get(Object key) {
        return requestMap.get(key);
    }

    @Override
    public boolean isEmpty() {
        return requestMap.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return requestMap.keySet();
    }

    @Override
    public String[] put(String key, String[] value) {
        return responseMap.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String[]> m) {
        responseMap.putAll(m);
    }

    @Override
    public String[] remove(Object key) {
        return responseMap.remove(key);
    }

    @Override
    public int size() {
        return requestMap.size();
    }

    @Override
    public Collection<String[]> values() {
        return requestMap.values();
    }
}
