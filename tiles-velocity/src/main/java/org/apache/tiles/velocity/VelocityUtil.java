package org.apache.tiles.velocity;

import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.context.Context;

public class VelocityUtil {
    
    private final static String PARAMETER_MAP_STACK_KEY = "org.apache.tiles.velocity.PARAMETER_MAP_STACK"; 
    
    private VelocityUtil() {
    }
    
    public static boolean toSimpleBoolean(Boolean obj, boolean defaultValue) {
        return obj != null ? obj : defaultValue;
    }
    
    @SuppressWarnings("unchecked")
    public static Stack<Map<String, Object>> getParameterStack(Context context) {
        Stack<Map<String, Object>> stack = (Stack<Map<String, Object>>) context
                .get(PARAMETER_MAP_STACK_KEY);
        if (stack == null) {
            stack = new Stack<Map<String,Object>>();
            context.put(PARAMETER_MAP_STACK_KEY, stack);
        }
        return stack;
    }

    public static void setAttribute(Context velocityContext,
            HttpServletRequest request, ServletContext servletContext,
            String name, Object obj, String scope) {
        if (scope == null) {
            scope = "page";
        }
        if ("page".equals(scope)) {
            velocityContext.put(name, obj);
        } else if ("request".equals(scope)) {
            request.setAttribute(name, obj);
        } else if ("session".equals(scope)) {
            request.getSession().setAttribute(name, obj);
        } else if ("application".equals("scope")) {
            servletContext.setAttribute(name, obj);
        }
    }
}
