/**
 * 
 */
package org.apache.tiles.velocity.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.velocity.TilesVelocityException;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Renderable;

public abstract class AbstractDefaultToStringRenderable implements Renderable {
    protected final Context velocityContext;
    protected final Map<String, Object> params;
    protected final HttpServletResponse response;
    protected final HttpServletRequest request;

    private Log log = LogFactory.getLog(getClass());

    public AbstractDefaultToStringRenderable(Context velocityContext,
            Map<String, Object> params, HttpServletResponse response,
            HttpServletRequest request) {
        this.velocityContext = velocityContext;
        this.params = params;
        this.response = response;
        this.request = request;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        try {
            render(null, writer);
        } catch (MethodInvocationException e) {
            throw new TilesVelocityException("Cannot invoke method when rendering", e);
        } catch (ParseErrorException e) {
            throw new TilesVelocityException("Cannot parse when rendering", e);
        } catch (ResourceNotFoundException e) {
            throw new TilesVelocityException("Cannot find resource when rendering", e);
        } catch (IOException e) {
            throw new TilesVelocityException("I/O exception when rendering", e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                log.error("Error when closing a StringWriter, the impossible happened!", e);
            }
        }
        return writer.toString();
    }
}