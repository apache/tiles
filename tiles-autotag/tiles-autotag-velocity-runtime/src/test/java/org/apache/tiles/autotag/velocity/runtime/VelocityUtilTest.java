/**
 *
 */
package org.apache.tiles.autotag.velocity.runtime;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.Node;
import org.junit.Test;

/**
 * Tests {@link VelocityUtil}.
 *
 * @version $Rev$ $Date$
 */
public class VelocityUtilTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.velocity.runtime.VelocityUtil#getParameters(org.apache.velocity.context.InternalContextAdapter, org.apache.velocity.runtime.parser.node.Node)}.
     */
    @Test
    public void testGetParameters() {
        InternalContextAdapter context = createMock(InternalContextAdapter.class);
        Node node = createMock(Node.class);
        ASTMap astMap = createMock(ASTMap.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> params = createMock(Map.class);

        expect(node.jjtGetChild(0)).andReturn(astMap);
        expect(astMap.value(context)).andReturn(params);

        replay(context, node, astMap, params);
        assertSame(params, VelocityUtil.getParameters(context, node));
        verify(context, node, astMap, params);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.velocity.runtime.VelocityUtil#getObject(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testGetObject() {
        assertEquals(new Integer(1), VelocityUtil.getObject(new Integer(1), new Integer(2)));
        assertEquals(new Integer(1), VelocityUtil.getObject(new Integer(1), null));
        assertEquals(new Integer(2), VelocityUtil.getObject(null, new Integer(2)));
        assertNull(VelocityUtil.getObject(null, null));
    }

}
