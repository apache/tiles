package org.apache.tiles.template;

import java.util.Map;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.request.Request;

public final class TilesTemplateUtil {

    /**
     * The name of the attribute that holds the compose stack.
     */
    public static final String COMPOSE_STACK_ATTRIBUTE_NAME = "org.apache.tiles.template.COMPOSE_STACK";

    private TilesTemplateUtil() {
    }


    /**
     * Returns the current compose stack, or creates a new one if not present.
     *
     * @param env The current FreeMarker environment.
     * @return The compose stack.
     * @since 2.2.0
     */
    @SuppressWarnings("unchecked")
    public static ArrayStack<Object> getComposeStack(Request request) {
        Map<String, Object> requestScope = request.getRequestScope();
        ArrayStack<Object> composeStack = (ArrayStack<Object>) requestScope
                .get(COMPOSE_STACK_ATTRIBUTE_NAME);
        if (composeStack == null) {
            composeStack = new ArrayStack<Object>();
            requestScope.put(COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        }
        return composeStack;
    }
}
