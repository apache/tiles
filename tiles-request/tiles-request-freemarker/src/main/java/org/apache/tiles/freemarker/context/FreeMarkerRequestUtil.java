package org.apache.tiles.freemarker.context;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.servlet.ServletUtil;

import freemarker.core.Environment;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.TemplateModelException;

public final class FreeMarkerRequestUtil {

    private FreeMarkerRequestUtil() {
    }

    /**
     * Returns the HTTP request hash model.
     *
     * @param env The current FreeMarker environment.
     * @return The request hash model.
     * @since 2.2.0
     */
    public static HttpRequestHashModel getRequestHashModel(Environment env) {
        try {
            return (HttpRequestHashModel) env.getDataModel().get(
                    FreemarkerServlet.KEY_REQUEST);
        } catch (TemplateModelException e) {
            throw new NotAvailableFreemarkerServletException(
                    "Exception got when obtaining the request hash model", e);
        }
    }

    /**
     * Returns the servlet context hash model.
     *
     * @param env The current FreeMarker environment.
     * @return The servlet context hash model.
     * @since 2.2.0
     */
    public static ServletContextHashModel getServletContextHashModel(
            Environment env) {
        try {
            return (ServletContextHashModel) env.getDataModel().get(
                    FreemarkerServlet.KEY_APPLICATION);
        } catch (TemplateModelException e) {
            throw new NotAvailableFreemarkerServletException(
                    "Exception got when obtaining the application hash model",
                    e);
        }
    }

    public static ApplicationContext getApplicationContext(
            Environment env) {
        return ServletUtil
                .getApplicationContext(getServletContextHashModel(env)
                        .getServlet().getServletContext());
    }

}
