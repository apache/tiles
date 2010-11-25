/**
 *
 */
package org.apache.tiles.autotag.freemarker.runtime;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.freemarker.FreemarkerRequest;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.core.Macro;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Tests {@link BodyFMModel}.
 *
 * @version $Rev$ $Date$
 */
public class BodyFMModelTest {

    /**
     * Test method for
     * {@link org.apache.tiles.autotag.freemarker.runtime.BodyFMModel#execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)}
     * .
     *
     * @throws IOException If something goes wrong.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException, TemplateModelException {
        BodyFMModel model = createMockBuilder(BodyFMModel.class).createMock();
        @SuppressWarnings("unchecked")
        Map<String, TemplateModel> params = createMock(Map.class);
        Template template = createMock(Template.class);
        TemplateHashModel rootDataModel = createMock(TemplateHashModel.class);
        Writer out = createMock(Writer.class);
        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        GenericServlet servlet = createMock(GenericServlet.class);
        ObjectWrapper wrapper = createMock(ObjectWrapper.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        HttpServletRequest httpServletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);

        expect(template.getMacros()).andReturn(new HashMap<String, Macro>());
        expect(servlet.getServletContext()).andReturn(servletContext)
                .anyTimes();
        expect(
                servletContext
                        .getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);

        replay(servlet, wrapper, servletContext, applicationContext,
                httpServletRequest, httpServletResponse);
        ServletContextHashModel servletContextHashModel = new ServletContextHashModel(
                servlet, wrapper);
        HttpRequestHashModel httpRequestHashModel = new HttpRequestHashModel(
                httpServletRequest, httpServletResponse, wrapper);

        expect(rootDataModel.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(
                servletContextHashModel);
        expect(rootDataModel.get(FreemarkerServlet.KEY_REQUEST)).andReturn(
                httpRequestHashModel);

        replay(template, rootDataModel, out);
        Environment env = new Environment(template, rootDataModel, out);

        model.execute(eq(params), isA(FreemarkerRequest.class),
                isA(FreemarkerModelBody.class));

        replay(model, params, body);
        model.execute(env, params, new TemplateModel[0], body);
        verify(model, params, template, rootDataModel, out, servlet, wrapper,
                servletContext, httpServletRequest, httpServletResponse, body,
                applicationContext);
    }

}
