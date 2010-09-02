package org.apache.tiles.request.velocity.render;

import java.util.HashMap;
import java.util.Map;


import org.apache.tiles.request.ApplicationContext;
import org.apache.velocity.tools.view.VelocityView;

public class VelocityRendererBuilder {

    /**
     * The initialization parameters for VelocityView.
     */
    private Map<String, String> params = new HashMap<String, String>();

    /**
     * The application context.
     */
    private ApplicationContext applicationContext;

    private VelocityRendererBuilder() {
    }

    public static VelocityRendererBuilder createInstance() {
        return new VelocityRendererBuilder();
    }

    /**
     * Sets a parameter for the internal servlet.
     *
     * @param key The name of the parameter.
     * @param value The value of the parameter.
     */
    public VelocityRendererBuilder setParameter(String key, String value) {
        params.put(key, value);
        return this;
    }

    /**
     * Sets the application context.
     *
     * @param applicationContext The application context.
     */
    public VelocityRendererBuilder setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        return this;
    }

    public VelocityRenderer build() {
        VelocityView velocityView = new VelocityView(
                new ApplicationContextJeeConfig(applicationContext, params));
        return new VelocityRenderer(velocityView);
    }
}
