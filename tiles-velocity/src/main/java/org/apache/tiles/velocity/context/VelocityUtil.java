package org.apache.tiles.velocity.context;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Renderable;

public class VelocityUtil {
    
    public static final Renderable EMPTY_RENDERABLE;
    
    static {
        EMPTY_RENDERABLE = new Renderable() {

            @Override
            public String toString() {
                return "";
            }

            public boolean render(InternalContextAdapter context, Writer writer)
                    throws IOException, MethodInvocationException,
                    ParseErrorException, ResourceNotFoundException {
                // Does nothing, really!
                return true;
            }
        };
    }
    
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
            scope = "request"; // FIXME Use "page"
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
