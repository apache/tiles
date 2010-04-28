package org.apache.tiles.request.portlet.delegate;

import java.util.Map;

public interface RequestDelegate {

    Map<String, String> getParam();

    Map<String, String[]> getParamValues();
}
