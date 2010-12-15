package org.apache.tiles.request.jsp.extractor;

import java.util.Enumeration;

import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.attribute.AttributeExtractor;

public class SessionScopeExtractor implements AttributeExtractor {

    private PageContext context;

    public SessionScopeExtractor(PageContext context) {
        this.context = context;
    }

    @Override
    public void removeValue(String name) {
        if (context.getSession() == null) {
            return;
        }
        context.removeAttribute(name, PageContext.SESSION_SCOPE);
    }

    @Override
    public Enumeration<String> getKeys() {
        if (context.getSession() == null) {
            return null;
        }
        return context.getAttributeNamesInScope(PageContext.SESSION_SCOPE);
    }

    @Override
    public Object getValue(String key) {
        if (context.getSession() == null) {
            return null;
        }
        return context.getAttribute(key, PageContext.SESSION_SCOPE);
    }

    @Override
    public void setValue(String key, Object value) {
        if (context.getSession() == null) {
            return;
        }
        context.setAttribute(key, value, PageContext.SESSION_SCOPE);
    }
}
