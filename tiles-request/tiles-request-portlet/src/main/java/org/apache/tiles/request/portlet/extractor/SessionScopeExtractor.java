package org.apache.tiles.request.portlet.extractor;

import java.util.Enumeration;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.tiles.request.attribute.AttributeExtractor;

public class SessionScopeExtractor implements AttributeExtractor {

    private PortletRequest request;

    private int scope;

    public SessionScopeExtractor(PortletRequest request, int scope) {
        this.request = request;
        if (scope != PortletSession.APPLICATION_SCOPE
                && scope != PortletSession.PORTLET_SCOPE) {
            throw new IllegalArgumentException(
                    "The scope must be either APPLICATION_SCOPE or PORTLET_SCOPE");
        }
        this.scope = scope;
    }

    @Override
    public void setValue(String name, Object value) {
        request.getPortletSession().setAttribute(name, value, scope);
    }

    @Override
    public void removeValue(String name) {
        PortletSession session = request.getPortletSession(false);
        if (session != null) {
            session.removeAttribute(name, scope);
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        PortletSession session = request.getPortletSession(false);
        if (session != null) {
            return session.getAttributeNames(scope);
        }
        return null;
    }

    @Override
    public Object getValue(String key) {
        PortletSession session = request.getPortletSession(false);
        if (session != null) {
            return session.getAttribute(key, scope);
        }
        return null;
    }
}
