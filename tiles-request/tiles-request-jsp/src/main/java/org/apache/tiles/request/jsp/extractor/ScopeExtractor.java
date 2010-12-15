package org.apache.tiles.request.jsp.extractor;

import java.util.Enumeration;

import javax.servlet.jsp.JspContext;

import org.apache.tiles.request.attribute.AttributeExtractor;

public class ScopeExtractor implements AttributeExtractor {

    private JspContext context;

    private int scope;

    public ScopeExtractor(JspContext context, int scope) {
        this.context = context;
        this.scope = scope;
    }

    @Override
    public void removeValue(String name) {
        context.removeAttribute(name, scope);
    }

    @Override
    public Enumeration<String> getKeys() {
        return context.getAttributeNamesInScope(scope);
    }

    @Override
    public Object getValue(String key) {
        return context.getAttribute(key, scope);
    }

    @Override
    public void setValue(String key, Object value) {
        context.setAttribute(key, value, scope);
    }
}
