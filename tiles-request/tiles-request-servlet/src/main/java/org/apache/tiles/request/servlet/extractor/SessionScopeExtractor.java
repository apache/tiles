package org.apache.tiles.request.servlet.extractor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tiles.request.collection.extractor.AttributeExtractor;

public class SessionScopeExtractor implements AttributeExtractor {

    private HttpServletRequest request;

    public SessionScopeExtractor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setValue(String name, Object value) {
        request.getSession().setAttribute(name, value);
    }

    @Override
    public void removeValue(String name) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(name);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getKeys() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttributeNames();
        }
        return null;
    }

    @Override
    public Object getValue(String key) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }
}
