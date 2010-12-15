package org.apache.tiles.request.servlet.extractor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.request.attribute.HasKeys;

public class ParameterExtractor implements HasKeys<String> {

    private HttpServletRequest request;

    public ParameterExtractor(HttpServletRequest request) {
        this.request = request;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getKeys() {
        return request.getParameterNames();
    }

    @Override
    public String getValue(String key) {
        return request.getParameter(key);
    }
}
