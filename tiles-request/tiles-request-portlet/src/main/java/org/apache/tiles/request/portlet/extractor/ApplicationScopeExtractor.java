package org.apache.tiles.request.portlet.extractor;

import java.util.Enumeration;

import javax.portlet.PortletContext;

import org.apache.tiles.request.attribute.AttributeExtractor;

public class ApplicationScopeExtractor implements AttributeExtractor {

    private PortletContext context;

    public ApplicationScopeExtractor(PortletContext context) {
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

    @Override
    public Enumeration<String> getKeys() {
        return context.getAttributeNames();
    }

    @Override
    public Object getValue(String key) {
        return context.getAttribute(key);
    }
}
