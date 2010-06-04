/**
 *
 */
package org.apache.tiles.el;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ELContextImpl}.
 *
 * @version $Rev$ $Date$
 */
public class ELContextImplTest {

    private ELContextImpl context;

    private ELResolver resolver;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        resolver = createMock(ELResolver.class);
        context = new ELContextImpl(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.el.ELContextImpl#getELResolver()}.
     */
    @Test
    public void testGetELResolver() {
        replay(resolver);
        assertEquals(resolver, context.getELResolver());
        verify(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.el.ELContextImpl#setFunctionMapper(javax.el.FunctionMapper)}.
     */
    @Test
    public void testSetFunctionMapper() {
        FunctionMapper functionMapper = createMock(FunctionMapper.class);

        replay(resolver, functionMapper);
        context.setFunctionMapper(functionMapper);
        assertEquals(functionMapper, context.getFunctionMapper());
        verify(resolver, functionMapper);
    }

    /**
     * Test method for {@link org.apache.tiles.el.ELContextImpl#setVariableMapper(javax.el.VariableMapper)}.
     */
    @Test
    public void testSetVariableMapper() {
        VariableMapper variableMapper = createMock(VariableMapper.class);

        replay(resolver, variableMapper);
        context.setVariableMapper(variableMapper);
        assertEquals(variableMapper, context.getVariableMapper());
        verify(resolver, variableMapper);
    }

    /**
     * Tests {@link ELContextImpl#getFunctionMapper()}.
     */
    @Test
    public void testNullFunctionMapper() {
        replay(resolver);
        FunctionMapper functionMapper = context.getFunctionMapper();
        assertNull(functionMapper.resolveFunction("whatever", "it_IT"));
        verify(resolver);
    }

    /**
     * Tests {@link ELContextImpl#getVariableMapper()}.
     */
    @Test
    public void testVariableMapperImpl() {
        ValueExpression expression = createMock(ValueExpression.class);

        replay(resolver, expression);
        VariableMapper variableMapper = context.getVariableMapper();
        assertNull(variableMapper.resolveVariable("whatever"));
        variableMapper.setVariable("var", expression);
        assertEquals(expression, variableMapper.resolveVariable("var"));
        verify(resolver, expression);
    }
}
