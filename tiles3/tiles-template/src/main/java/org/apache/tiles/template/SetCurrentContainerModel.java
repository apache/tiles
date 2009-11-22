package org.apache.tiles.template;

import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.Request;

public class SetCurrentContainerModel {

    public void execute(String containerKey, Request request) {
        TilesAccess.setCurrentContainer(request, containerKey);
    }
}
