package org.apache.tiles.request.portlet.extractor;

import java.util.Enumeration;

import javax.portlet.PortletContext;

import org.apache.tiles.request.collection.extractor.HasKeys;

public class InitParameterExtractor implements HasKeys<String> {

    private PortletContext context;

    public InitParameterExtractor(PortletContext context) {
        this.context = context;
    }

    @Override
    public Enumeration<String> getKeys() {
        return context.getInitParameterNames();
    }

    @Override
    public String getValue(String key) {
        return context.getInitParameter(key);
    }

}
