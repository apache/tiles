package org.apache.tiles.velocity.template;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

public class ContextHolder {

    private Context velocityContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext application;

    /**
     * Sets the current {@link HttpServletRequest}. This is required for this
     * tool to operate and will throw a NullPointerException if this is not set
     * or is set to {@code null}.
     */
    public void setRequest(HttpServletRequest request) {
        if (request == null) {
            throw new NullPointerException("request should not be null");
        }
        this.request = request;
    }

    /**
     * Sets the current {@link HttpServletResponse}. This is required for this
     * tool to operate and will throw a NullPointerException if this is not set
     * or is set to {@code null}.
     */
    public void setResponse(HttpServletResponse response) {
        if (response == null) {
            throw new NullPointerException("response should not be null");
        }
        this.response = response;
    }

    /**
     * Sets the {@link ServletContext}. This is required for this tool to
     * operate and will throw a NullPointerException if this is not set or is
     * set to {@code null}.
     */
    public void setServletContext(ServletContext application) {
        if (application == null) {
            throw new NullPointerException("servlet context should not be null");
        }
        this.application = application;
    }

    /**
     * Initializes this tool.
     * 
     * @param context the current {@link Context}
     * @throws IllegalArgumentException if the param is not a {@link Context}
     */
    public void setVelocityContext(Context context) {
        if (context == null) {
            throw new NullPointerException(
                    "velocity context should not be null");
        }
        this.velocityContext = context;
    }

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected HttpServletResponse getResponse() {
        return response;
    }

    protected ServletContext getServletContext() {
        return application;
    }

    protected Context getVelocityContext() {
        return velocityContext;
    }
}
