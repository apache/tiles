package org.apache.tiles.request.velocity.render;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.collection.IteratorEnumeration;
import org.apache.tiles.request.servlet.ServletUtil;
import org.apache.velocity.tools.view.JeeConfig;

/**
 * Implements JeeConfig to use parameters set through
 * {@link VelocityRenderer#setParameter(String, String)}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class ApplicationContextJeeConfig implements JeeConfig {

    /**
     * The application context.
     */
    private ApplicationContext applicationContext;

    /**
     * The initialization parameters for VelocityView.
     */
    private Map<String, String> params;

    public ApplicationContextJeeConfig(ApplicationContext applicationContext, Map<String, String> params) {
        this.applicationContext = applicationContext;
        this.params = new HashMap<String, String>(params);
    }

    /** {@inheritDoc} */
    public String getInitParameter(String name) {
        return params.get(name);
    }

    /** {@inheritDoc} */
    public String findInitParameter(String key) {
        return params.get(key);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Enumeration getInitParameterNames() {
        return new IteratorEnumeration(params.keySet().iterator());
    }

    /** {@inheritDoc} */
    public String getName() {
        return "Application Context JEE Config";
    }

    /** {@inheritDoc} */
    public ServletContext getServletContext() {
        return ServletUtil.getServletContext(applicationContext);
    }
}