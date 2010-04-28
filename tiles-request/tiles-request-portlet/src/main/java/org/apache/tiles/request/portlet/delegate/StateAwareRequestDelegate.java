package org.apache.tiles.request.portlet.delegate;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.StateAwareResponse;

import org.apache.tiles.request.collection.AddableParameterMap;
import org.apache.tiles.request.portlet.StateAwareParameterMap;
import org.apache.tiles.request.portlet.extractor.StateAwareParameterExtractor;

public class StateAwareRequestDelegate implements RequestDelegate {

    private PortletRequest request;

    private StateAwareResponse response;

    public StateAwareRequestDelegate(PortletRequest request,
            StateAwareResponse response) {
        this.request = request;
        this.response = response;
    }

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

    /** {@inheritDoc} */
    public Map<String, String> getParam() {
        if ((param == null) && (request != null)) {
            param = new AddableParameterMap(new StateAwareParameterExtractor(
                    request, response));
        }
        return (param);
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getParamValues() {
        if ((paramValues == null) && (request != null)) {
            paramValues = new StateAwareParameterMap(request.getParameterMap(),
                    response.getRenderParameterMap());
        }
        return (paramValues);
    }
}
