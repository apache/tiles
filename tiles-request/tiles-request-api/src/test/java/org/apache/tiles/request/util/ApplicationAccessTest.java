/**
 *
 */
package org.apache.tiles.request.util;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Map;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.scope.ContextResolver;
import org.junit.Test;

/**
 * Tests {@link ApplicationAccess}.
 *
 * @version $Rev$ $Date$
 */
public class ApplicationAccessTest {

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationAccess#register(org.apache.tiles.request.ApplicationContext)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRegister() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = createMock(Map.class);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(applicationScope.put(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE, applicationContext)).andReturn(null);

        replay(applicationContext, applicationScope);
        ApplicationAccess.register(applicationContext);
        verify(applicationContext, applicationScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationAccess#registerContextResolver(org.apache.tiles.request.scope.ContextResolver, org.apache.tiles.request.ApplicationContext)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRegisterContextResolver() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = createMock(Map.class);
        ContextResolver contextResolver = createMock(ContextResolver.class);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(applicationScope.put(ApplicationAccess
                .CONTEXT_RESOLVER_ATTRIBUTE, contextResolver)).andReturn(null);

        replay(applicationContext, applicationScope, contextResolver);
        ApplicationAccess.registerContextResolver(contextResolver, applicationContext);
        verify(applicationContext, applicationScope, contextResolver);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.ApplicationAccess#getContextResolver(org.apache.tiles.request.ApplicationContext)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetContextResolver() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Map<String, Object> applicationScope = createMock(Map.class);
        ContextResolver contextResolver = createMock(ContextResolver.class);

        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(applicationScope.get(ApplicationAccess
                .CONTEXT_RESOLVER_ATTRIBUTE)).andReturn(contextResolver);

        replay(applicationContext, applicationScope, contextResolver);
        assertEquals(contextResolver, ApplicationAccess.getContextResolver(applicationContext));
        verify(applicationContext, applicationScope, contextResolver);
    }

}
