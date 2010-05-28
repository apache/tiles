/**
 *
 */
package org.apache.tiles.locale.impl;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Locale;
import java.util.Map;

import org.apache.tiles.request.Request;
import org.junit.Test;

/**
 * Tests {@link DefaultLocaleResolver}.
 *
 * @version $Rev$ $Date$
 */
public class DefaultLocaleResolverTest {

    /**
     * Test method for {@link org.apache.tiles.locale.impl.DefaultLocaleResolver#resolveLocale(org.apache.tiles.request.Request)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testResolveLocale() {
        Request request = createMock(Request.class);
        Map<String, Object> sessionScope = createMock(Map.class);
        Locale locale = Locale.ITALY;

        expect(request.getContext("session")).andReturn(sessionScope);
        expect(sessionScope.get(DefaultLocaleResolver.LOCALE_KEY)).andReturn(null);
        expect(request.getRequestLocale()).andReturn(locale);

        replay(request, sessionScope);
        DefaultLocaleResolver resolver = new DefaultLocaleResolver();
        assertSame(locale, resolver.resolveLocale(request));
        verify(request, sessionScope);
    }

}
