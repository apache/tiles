package org.apache.tiles.velocity.context;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.velocity.context.Context;
import org.junit.Test;

/**
 * @author antonio
 *
 */
public class VelocityTilesRequestContextFactoryTest {

    /**
     * The object to test.
     */
    private VelocityTilesRequestContextFactory factory;
    
    /**
     * Tests {@link VelocityTilesRequestContextFactory#createRequestContext(TilesApplicationContext, Object...)}. 
     */
    @Test
    public void testCreateRequestContext() {
        StringWriter writer = new StringWriter();
        TilesRequestContextFactory parentFactory = createMock(TilesRequestContextFactory.class);
        TilesApplicationContext applicationContext = createMock(TilesApplicationContext.class);
        Context velocityContext = createMock(Context.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        TilesRequestContext enclosedRequest = createMock(TilesRequestContext.class);
        expect(enclosedRequest.getRequestObjects()).andReturn(new Object[] {request, response});
        expect(parentFactory.createRequestContext(applicationContext, request, response)).andReturn(enclosedRequest);
        replay(parentFactory, enclosedRequest, applicationContext, velocityContext, request, response);
        factory = new VelocityTilesRequestContextFactory();
        factory.setRequestContextFactory(parentFactory);
        VelocityTilesRequestContext context = (VelocityTilesRequestContext) factory
                .createRequestContext(applicationContext, velocityContext,
                        request, response, writer);
        assertEquals(enclosedRequest, context.getWrappedRequest());
        Object[] requestItems = context.getRequestObjects();
        assertEquals(4, requestItems.length);
        assertEquals(velocityContext, requestItems[0]);
        assertEquals(request, requestItems[1]);
        assertEquals(response, requestItems[2]);
        assertEquals(writer, requestItems[3]);
        verify(parentFactory, enclosedRequest, applicationContext, velocityContext, request, response);
    }
}
