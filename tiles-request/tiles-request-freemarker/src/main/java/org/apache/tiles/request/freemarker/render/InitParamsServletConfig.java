package org.apache.tiles.request.freemarker.render;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.collection.IteratorEnumeration;

/**
 * Implements {@link ServletConfig} to initialize the internal servlet using parameters
 * set through {@link FreemarkerRenderer#setParameter(String, String)}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class InitParamsServletConfig implements ServletConfig {

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

    public InitParamsServletConfig(Map<String, String> params, ApplicationContext applicationContext) {
        this.params = params;
        this.applicationContext = applicationContext;
    }

    /** {@inheritDoc} */
    public String getInitParameter(String name) {
        return params.get(name);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Enumeration getInitParameterNames() {
        return new IteratorEnumeration(params.keySet().iterator());
    }

    /** {@inheritDoc} */
    public ServletContext getServletContext() {
        return org.apache.tiles.request.servlet.ServletUtil.getServletContext(applicationContext);
    }

    /** {@inheritDoc} */
    public String getServletName() {
        return "FreeMarker Attribute Renderer";
    }
}