package org.apache.tiles.request.freemarker.render;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.freemarker.FreemarkerRequestException;

public class FreemarkerRendererBuilder {

    /**
     * The initialization parameters.
     */
    private Map<String, String> params = new HashMap<String, String>();

    /**
     * The application context.
     *
     * @since 3.0.0
     */
    private ApplicationContext applicationContext;

    private FreemarkerRendererBuilder() {
    }

    public static FreemarkerRendererBuilder createInstance() {
        return new FreemarkerRendererBuilder();
    }

    /**
     * Sets a parameter for the internal servlet.
     *
     * @param key The name of the parameter.
     * @param value The value of the parameter.
     * @since 2.2.0
     */
    public FreemarkerRendererBuilder setParameter(String key, String value) {
        params.put(key, value);
        return this;
    }

    /**
     * Sets the application context.
     *
     * @param applicationContext The application context.
     * @since 3.0.0
     */
    public FreemarkerRendererBuilder setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        return this;
    }

    public FreemarkerRenderer build() {
        AttributeValueFreemarkerServlet servlet = new AttributeValueFreemarkerServlet();
        try {
            servlet.init(new InitParamsServletConfig(params, applicationContext));
            return new FreemarkerRenderer(servlet);
        } catch (ServletException e) {
            throw new FreemarkerRequestException(
                    "Cannot initialize internal servlet", e);
        }

    }

}
