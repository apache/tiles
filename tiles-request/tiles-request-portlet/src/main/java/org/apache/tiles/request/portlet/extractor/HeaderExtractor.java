package org.apache.tiles.request.portlet.extractor;

import java.util.Enumeration;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.tiles.request.attribute.EnumeratedValuesExtractor;

public class HeaderExtractor implements EnumeratedValuesExtractor {

    private PortletRequest request;

    private PortletResponse response;

    public HeaderExtractor(PortletRequest request,
            PortletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Enumeration<String> getKeys() {
        return request.getPropertyNames();
   }

    @Override
    public String getValue(String key) {
        return request.getProperty(key);
    }

    @Override
    public Enumeration<String> getValues(String key) {
        return request.getProperties(key);
    }

    @Override
    public void setValue(String key, String value) {
        response.setProperty(key, value);
    }
}
