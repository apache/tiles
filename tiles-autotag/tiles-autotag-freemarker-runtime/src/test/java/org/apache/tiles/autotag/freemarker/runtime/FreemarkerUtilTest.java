/**
 *
 */
package org.apache.tiles.autotag.freemarker.runtime;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.Writer;
import java.util.HashMap;

import org.junit.Test;

import freemarker.core.Environment;
import freemarker.core.Macro;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

/**
 * Tests {@link FreemarkerUtil}.
 *
 * @version $Rev$ $Date$
 */
public class FreemarkerUtilTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.freemarker.runtime.FreemarkerUtil#getAsObject(freemarker.template.TemplateModel, java.lang.Object)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetAsObject() throws TemplateModelException {
        TemplateNumberModel model = createMock(TemplateNumberModel.class);
        Template template = createMock(Template.class);
        TemplateHashModel rootDataModel = createMock(TemplateHashModel.class);
        Writer out = createMock(Writer.class);

        expect(model.getAsNumber()).andReturn(new Integer(42));
        expect(template.getMacros()).andReturn(new HashMap<String, Macro>());

        replay(template, rootDataModel, out);
        new Environment(template, rootDataModel, out);

        replay(model);
        assertEquals(new Integer(42), FreemarkerUtil.getAsObject(model, new Integer(1)));
        verify(template, rootDataModel, out, model);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.freemarker.runtime.FreemarkerUtil#getAsObject(freemarker.template.TemplateModel, java.lang.Object)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetAsObjectDefault() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel rootDataModel = createMock(TemplateHashModel.class);
        Writer out = createMock(Writer.class);

        expect(template.getMacros()).andReturn(new HashMap<String, Macro>());

        replay(template, rootDataModel, out);
        new Environment(template, rootDataModel, out);

        assertEquals(new Integer(1), FreemarkerUtil.getAsObject(null, new Integer(1)));
        verify(template, rootDataModel, out);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.freemarker.runtime.FreemarkerUtil#getAsObject(freemarker.template.TemplateModel, java.lang.Object)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test(expected=FreemarkerAutotagException.class)
    public void testGetAsObjectException() throws TemplateModelException {
        TemplateNumberModel model = createMock(TemplateNumberModel.class);
        Template template = createMock(Template.class);
        TemplateHashModel rootDataModel = createMock(TemplateHashModel.class);
        Writer out = createMock(Writer.class);

        expect(model.getAsNumber()).andThrow(new TemplateModelException());
        expect(template.getMacros()).andReturn(new HashMap<String, Macro>());

        replay(template, rootDataModel, out);
        new Environment(template, rootDataModel, out);

        replay(model);
        try {
            assertEquals(new Integer(42), FreemarkerUtil.getAsObject(model, new Integer(1)));
        } finally {
            verify(template, rootDataModel, out, model);
        }
    }

}
