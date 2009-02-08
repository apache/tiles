package org.apache.tiles.freemarker.context;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.io.NullWriter;
import org.apache.tiles.impl.NoSuchContainerException;
import org.apache.tiles.servlet.context.ServletUtil;

import freemarker.core.Environment;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

public class FreeMarkerUtil {

    public static final String COMPOSE_STACK_ATTRIBUTE_NAME = "org.apache.tiles.template.COMPOSE_STACK";

    private FreeMarkerUtil() {
    }

    /**
     * Returns true if forced include of the result is needed.
     * 
     * @param request The HTTP request.
     * @return If <code>true</code> the include operation must be forced.
     * @since 2.0.6
     */
    public static boolean isForceInclude(Environment env) {
        return ServletUtil
                .isForceInclude(getRequestHashModel(env).getRequest());
    }

    /**
     * Sets the option that enables the forced include of the response.
     * 
     * @param request The HTTP request.
     * @param forceInclude If <code>true</code> the include operation must be
     * forced.
     */
    public static void setForceInclude(Environment env, boolean forceInclude) {
        ServletUtil.setForceInclude(getRequestHashModel(env).getRequest(),
                forceInclude);
    }

    /**
     * Returns a specific Tiles container.
     *
     * @param context The page context to use.
     * @param key The key under which the container is stored. If null, the
     * default container will be returned.
     * @return The requested Tiles container.
     * @since 2.1.2
     */
    public static TilesContainer getContainer(Environment env, String key) {
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }
        return (TilesContainer) getServletContextHashModel(env).getServlet()
                .getServletContext().getAttribute(key);
    }

    /**
     * Sets the current container to use in web pages.
     * 
     * @param request The request to use.
     * @param context The servlet context to use.
     * @param key The key under which the container is stored.
     */
    public static void setCurrentContainer(Environment env, String key) {
        TilesContainer container = getContainer(env, key);
        if (container != null) {
            getServletContextHashModel(env).getServlet().getServletContext()
                    .setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                            container);
        } else {
            throw new NoSuchContainerException("The container with the key '"
                    + key + "' cannot be found");
        }
    }

    /**
     * Sets the current container to use in web pages.
     * 
     * @param request The request to use.
     * @param context The servlet context to use.
     * @param container The container to use as the current container.
     */
    public static void setCurrentContainer(Environment env,
            TilesContainer container) {
        ServletUtil.setCurrentContainer(getRequestHashModel(env).getRequest(),
                getServletContextHashModel(env).getServlet()
                        .getServletContext(), container);
    }

    /**
     * Returns the current container that has been set, or the default one.
     * 
     * @param request The request to use.
     * @param context The servlet context to use.
     * @return The current Tiles container to use in web pages.
     */
    public static TilesContainer getCurrentContainer(Environment env) {
        return ServletUtil.getCurrentContainer(getRequestHashModel(env)
                .getRequest(), getServletContextHashModel(env).getServlet()
                .getServletContext());
    }

    public static HttpRequestHashModel getRequestHashModel(Environment env) {
        try {
            return (HttpRequestHashModel) env.getDataModel().get(
                    FreemarkerServlet.KEY_REQUEST);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException(
                    "Exception got when obtaining the request hash model", e);
        }
    }

    public static ServletContextHashModel getServletContextHashModel(
            Environment env) {
        try {
            return (ServletContextHashModel) env.getDataModel().get(
                    FreemarkerServlet.KEY_APPLICATION);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException(
                    "Exception got when obtaining the application hash model",
                    e);
        }
    }

    public static String getAsString(TemplateModel model) {
        try {
            return (String) DeepUnwrap.unwrap(model);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException("Cannot unwrap a model", e);
        }
    }

    public static boolean getAsBoolean(TemplateModel model, boolean defaultValue) {
        try {
            Boolean retValue = (Boolean) DeepUnwrap.unwrap(model);
            return retValue != null ? retValue : defaultValue;
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException("Cannot unwrap a model", e);
        }
    }

    public static Object getAsObject(TemplateModel model) {
        try {
            return DeepUnwrap.unwrap(model);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException("Cannot unwrap a model", e);
        }
    }

    public static void setAttribute(Environment env, String name, Object obj,
            String scope) {
        if (scope == null) {
            scope = "page";
        }
        if ("page".equals(scope)) {
            try {
                TemplateModel model = env.getObjectWrapper().wrap(obj);
                env.setVariable(name, model);
            } catch (TemplateModelException e) {
                throw new FreeMarkerTilesException(
                        "Error when wrapping an object", e);
            }
        } else if ("request".equals(scope)) {
            getRequestHashModel(env).getRequest().setAttribute(name, obj);
        } else if ("session".equals(scope)) {
            getRequestHashModel(env).getRequest().getSession().setAttribute(
                    name, obj);
        } else if ("application".equals("scope")) {
            getServletContextHashModel(env).getServlet().getServletContext()
                    .setAttribute(name, obj);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Stack<Object> getComposeStack(Environment env) {
        HttpServletRequest request = getRequestHashModel(env).getRequest();
        Stack<Object> composeStack = (Stack<Object>) request
                .getAttribute(COMPOSE_STACK_ATTRIBUTE_NAME);
        if (composeStack == null) {
            composeStack = new Stack<Object>();
            request.setAttribute(COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        }
        return composeStack;
    }

    public static void evaluateBody(TemplateDirectiveBody body)
            throws TemplateException, IOException {
        if (body != null) {
            NullWriter writer = new NullWriter();
            try {
                body.render(writer);
            } finally {
                writer.close();
            }
        }
    }

    public static String renderAsString(TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String bodyString = null;
        if (body != null) {
            StringWriter stringWriter = new StringWriter();
            try {
                body.render(stringWriter);
            } finally {
                stringWriter.close();
            }
            bodyString = stringWriter.toString();
        }
        return bodyString;
    }
}
