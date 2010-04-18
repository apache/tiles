package org.apache.tiles.request.servlet.extractor;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.apache.tiles.request.collection.extractor.AttributeExtractor;

public class ApplicationScopeExtractor implements AttributeExtractor {

    private ServletContext context;

    public ApplicationScopeExtractor(ServletContext context) {
        this.context = context;
    }

    @Override
    public void setValue(String name, Object value) {
        context.setAttribute(name, value);
    }

    @Override
    public void removeValue(String name) {
        context.removeAttribute(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getKeys() {
        return context.getAttributeNames();
    }

    @Override
    public Object getValue(String key) {
        return context.getAttribute(key);
    }
}
