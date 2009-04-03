/**
 * 
 */
package org.apache.tiles.freemarker.template;

import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.freemarker.io.NullWriter;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.InsertDefinitionModel;
import org.junit.Before;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

/**
 * @author antonio
 *
 */
public class InsertDefinitionFMModelTest {

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
     * The writer.
     */
    private StringWriter writer;

    /**
     * The object wrapper.
     */
    private ObjectWrapper objectWrapper;

    /**
     * @throws java.lang.Exception If something goes wrong.
     */
    @Before
    public void setUp() throws Exception {
        template = createMock(Template.class);
        model = createMock(TemplateHashModel.class);
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        writer = new StringWriter();
        objectWrapper = DefaultObjectWrapper.getDefaultInstance();
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.InsertDefinitionFMModel#execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecute() throws TemplateException, IOException {
        InsertDefinitionModel tModel = createMock(InsertDefinitionModel.class);
        InsertDefinitionFMModel fmModel = new InsertDefinitionFMModel(tModel);
        TilesContainer container = createMock(TilesContainer.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(null).times(2);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        expectLastCall().times(2);
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(3);
        expect(servletContext.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container).times(2);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel).times(2);
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel).times(2);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("template", objectWrapper.wrap("myTemplate"));
        params.put("role", objectWrapper.wrap("myRole"));
        params.put("preparer", objectWrapper.wrap("myPreparer"));
     
        tModel.start(container, env);
        tModel.end(container, "myName", "myTemplate", "myRole", "myPreparer", env);
        body.render(isA(NullWriter.class));
        
        replay(tModel, body, container, attribute);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, tModel, body, container, servlet, servletContext, attribute);
    }

    /**
     * Initializes the FreeMarker environment.
     */
    private void initEnvironment() {
        replay(template, model);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
    }
}
