package org.apache.tiles.request.portlet.extractor;

import java.util.Enumeration;

import javax.portlet.PortletRequest;

import org.apache.tiles.request.collection.extractor.AttributeExtractor;

public class RequestScopeExtractor implements AttributeExtractor {

    private PortletRequest request;

    public RequestScopeExtractor(PortletRequest request) {
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

    @Override
    public Enumeration<String> getKeys() {
        return request.getAttributeNames();
    }

    @Override
    public Object getValue(String key) {
        return request.getAttribute(key);
    }
}
