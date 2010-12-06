package org.apache.tiles.autotag.plugin.internal;

import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.apache.tiles.request.Request;

/**
 * Example start/stop template.
 *
 * @version $Rev$ $Date$
 */
public class AnnotatedExampleModel {

    /**
     * It starts.
     *
     * @param one Parameter one.
     * @param two Parameter two.
     * @param request The request.
     * @param modelBody The model body.
     */
    public void execute(
            @Parameter(defaultValue = "hello", name = "alternateOne", required = true) String one,
            int two, Request request, ModelBody modelBody) {
        // Does nothing.
    }
}
