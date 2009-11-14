/**
 *
 */
package org.apache.tiles.freemarker.context;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * Tests {@link FreeMarkerRequestUtil}.
 *
 */
public class FreeMarkerRequestUtilTest {

    /**
     * A string writer.
     */
    private StringWriter writer;

    /**
     * The FreeMarker environment.
     */
    private Environment env;

    /**
     * The locale object.
     */
    private Locale locale;

    /**
     * The template.
     */
    private Template template;

    /**
     * The template model.
     */
    private TemplateHashModel model;

    /**
     * @throws java.lang.Exception If something goes wrong.
     */
    @Before
    public void setUp() throws Exception {
        template = createMock(Template.class);
        model = createMock(TemplateHashModel.class);
        writer = new StringWriter();
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.context.FreeMarkerRequestUtil#getRequestHashModel(freemarker.core.Environment)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetRequestHashModel() throws TemplateModelException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);

        expect(model.get("Request")).andReturn(requestModel);

        replay(template, model, request, objectWrapper);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
        assertEquals(requestModel, FreeMarkerRequestUtil.getRequestHashModel(env));
        verify(template, model, request, objectWrapper);
    }

}
