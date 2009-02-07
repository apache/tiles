package org.apache.tiles.template;

import java.util.Stack;

public class ComposeStackUtil {

    private ComposeStackUtil() {
        
    }

    public static Object findAncestorWithClass(Stack<Object> composeStack, Class<?> clazz) {
        Object retValue = null;
        for (int i = composeStack.size() - 1; i >= 0 && retValue == null; i--) {
            Object obj = composeStack.get(i);
            if (clazz.isAssignableFrom(obj.getClass())) {
                retValue = obj;
            }
        }
        
        return retValue;
    }
}
