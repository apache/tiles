package org.apache.tiles.request.portlet.extractor;

import java.util.Enumeration;

import javax.portlet.PortletRequest;

import org.apache.tiles.request.attribute.HasKeys;

public class ParameterExtractor implements HasKeys<String> {

    private PortletRequest request;

    public ParameterExtractor(PortletRequest request) {
        this.request = request;
    }

    @Override
    public Enumeration<String> getKeys() {
        return request.getParameterNames();
    }

    @Override
    public String getValue(String key) {
        return request.getParameter(key);
    }
}
