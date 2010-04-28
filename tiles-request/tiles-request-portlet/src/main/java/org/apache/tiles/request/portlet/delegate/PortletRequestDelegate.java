package org.apache.tiles.request.portlet.delegate;

import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.tiles.request.collection.ReadOnlyEnumerationMap;
import org.apache.tiles.request.portlet.extractor.ParameterExtractor;

public class PortletRequestDelegate implements RequestDelegate {

    private PortletRequest request;

    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-value.</p>
     */
    private Map<String, String> param = null;

    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-values.</p>
     */
    private Map<String, String[]> paramValues = null;

    public PortletRequestDelegate(PortletRequest request) {
        this.request = request;
    }

    /** {@inheritDoc} */
    public Map<String, String> getParam() {
        if ((param == null) && (request != null)) {
            param = new ReadOnlyEnumerationMap<String>(new ParameterExtractor(request));
        }
        return (param);
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getParamValues() {
        if ((paramValues == null) && (request != null)) {
            paramValues = request.getParameterMap();
        }
        return (paramValues);
    }
}
