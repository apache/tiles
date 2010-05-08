/**
 *
 */
package org.apache.tiles.request.freemarker;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;

import org.junit.Test;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModelException;

/**
 * Tests {@link EnvironmentScopeMap}.
 *
 * @version $Rev$ $Date$
 */
public class EnvironmentScopeMapTest {

    /**
     * Test method for {@link org.apache.tiles.request.freemarker.EnvironmentScopeMap#keySet()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testKeySet() {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        Configuration configuration = createMock(Configuration.class);
        Set<String> names = createMock(Set.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(template.getConfiguration()).andReturn(configuration);
        expect(configuration.getSharedVariableNames()).andReturn(names);

        replay(template, model, configuration, names);
        Environment env = new Environment(template, model, writer);
        EnvironmentScopeMap map = new EnvironmentScopeMap(env);
        assertEquals(names, map.keySet());
        verify(template, model, configuration, names);
    }


    /**
     * Test method for {@link org.apache.tiles.request.freemarker.EnvironmentScopeMap#keySet()}.
     * @throws TemplateModelException If something goes wrong.
     */
    @SuppressWarnings("unchecked")
    @Test(expected=FreemarkerRequestException.class)
    public void testKeySetException() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModelEx model = createMock(TemplateHashModelEx.class);
        Configuration configuration = createMock(Configuration.class);
        Set<String> names = createMock(Set.class);
        Writer writer = new StringWriter();

        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        expect(model.keys()).andThrow(new TemplateModelException());
        expect(template.getConfiguration()).andReturn(configuration);
        expect(configuration.getSharedVariableNames()).andReturn(names);

        try {
            replay(template, model, configuration, names);
            Environment env = new Environment(template, model, writer);
            EnvironmentScopeMap map = new EnvironmentScopeMap(env);
            map.keySet();
        } finally {
            verify(template, model, configuration, names);
        }
    }
}
