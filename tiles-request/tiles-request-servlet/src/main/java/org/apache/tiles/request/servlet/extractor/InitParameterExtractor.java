package org.apache.tiles.request.servlet.extractor;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.apache.tiles.request.attribute.HasKeys;

public class InitParameterExtractor implements HasKeys<String> {

    private ServletContext context;

    public InitParameterExtractor(ServletContext context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getKeys() {
        return context.getInitParameterNames();
    }

    @Override
    public String getValue(String key) {
        return context.getInitParameter(key);
    }

}
