package org.apache.tiles.request.servlet.extractor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.request.collection.extractor.AttributeExtractor;

public class RequestScopeExtractor implements AttributeExtractor {

    private HttpServletRequest request;

    public RequestScopeExtractor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setValue(String name, Object value) {
        request.setAttribute(name, value);
    }

    @Override
    public void removeValue(String name) {
        request.removeAttribute(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getKeys() {
        return request.getAttributeNames();
    }

    @Override
    public Object getValue(String key) {
        return request.getAttribute(key);
    }
}
