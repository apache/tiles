package org.apache.tiles.request.servlet.extractor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.collection.extractor.HeaderExtractor;

public class ServletHeaderExtractor implements HeaderExtractor {

    private HttpServletRequest request;

    private HttpServletResponse response;

    public ServletHeaderExtractor(HttpServletRequest request,
            HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getKeys() {
        return request.getHeaderNames();
   }

    @Override
    public String getValue(String key) {
        return request.getHeader(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getValues(String key) {
        return request.getHeaders(key);
    }

    @Override
    public void setValue(String key, String value) {
        response.setHeader(key, value);
    }
}
