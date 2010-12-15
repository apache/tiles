package org.apache.tiles.request.portlet.extractor;

import javax.portlet.PortletRequest;
import javax.portlet.StateAwareResponse;

import org.apache.tiles.request.attribute.HasAddableKeys;

public class StateAwareParameterExtractor extends ParameterExtractor implements HasAddableKeys<String> {

    private StateAwareResponse response;

    public StateAwareParameterExtractor(PortletRequest request, StateAwareResponse response) {
        super(request);
        this.response = response;
    }

    @Override
    public void setValue(String key, String value) {
        response.setRenderParameter(key, value);
    }
}
