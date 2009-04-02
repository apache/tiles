/**
 * 
 */
package org.apache.tiles.freemarker.template;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.ImportAttributeModel;
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
import freemarker.template.utility.DeepUnwrap;

/**
 * @author antonio
 *
 */
public class ImportAttributeFMModelTest {

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
     * Test method for {@link org.apache.tiles.freemarker.template.ImportAttributeFMModel#execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecute() throws TemplateException, IOException {
        ImportAttributeModel tModel = createMock(ImportAttributeModel.class);
        ImportAttributeFMModel fmModel = new ImportAttributeFMModel(tModel);
        TilesContainer container = createMock(TilesContainer.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(null);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        expect(servletContext.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel);
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel);
        expect(template.getObjectWrapper()).andReturn(objectWrapper).times(2);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("toName", objectWrapper.wrap("myToName"));
        params.put("ignore", objectWrapper.wrap(false));

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");
        expect(tModel.getImportedAttributes(container, "myName", "myToName",
                false, env)).andReturn(attributes);
        
        replay(tModel, body, container, attribute);
        fmModel.execute(env, params, null, body);
        assertEquals("value1", DeepUnwrap.unwrap(env.getCurrentNamespace().get("one")));
        assertEquals("value2", DeepUnwrap.unwrap(env.getCurrentNamespace().get("two")));
        verify(template, model, request, tModel, body, container, servlet, servletContext, attribute);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.ImportAttributeFMModel#execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecuteRequest() throws TemplateException, IOException {
        ImportAttributeModel tModel = createMock(ImportAttributeModel.class);
        ImportAttributeFMModel fmModel = new ImportAttributeFMModel(tModel);
        TilesContainer container = createMock(TilesContainer.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(null);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        request.setAttribute("one", "value1");
        request.setAttribute("two", "value2");
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        expect(servletContext.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel).times(3);
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("toName", objectWrapper.wrap("myToName"));
        params.put("ignore", objectWrapper.wrap(false));
        params.put("scope", objectWrapper.wrap("request"));

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");
        expect(tModel.getImportedAttributes(container, "myName", "myToName",
                false, env)).andReturn(attributes);
        
        replay(tModel, body, container, attribute);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, tModel, body, container, servlet, servletContext, attribute);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.ImportAttributeFMModel#execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecuteSession() throws TemplateException, IOException {
        ImportAttributeModel tModel = createMock(ImportAttributeModel.class);
        ImportAttributeFMModel fmModel = new ImportAttributeFMModel(tModel);
        TilesContainer container = createMock(TilesContainer.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpSession session = createMock(HttpSession.class);
        expect(request.getSession()).andReturn(session).times(2);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(null);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        session.setAttribute("one", "value1");
        session.setAttribute("two", "value2");
        replay(request, session);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(2);
        expect(servletContext.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container);
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel).times(3);
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("toName", objectWrapper.wrap("myToName"));
        params.put("ignore", objectWrapper.wrap(false));
        params.put("scope", objectWrapper.wrap("session"));

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");
        expect(tModel.getImportedAttributes(container, "myName", "myToName",
                false, env)).andReturn(attributes);
        
        replay(tModel, body, container, attribute);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, session, tModel, body, container, servlet, servletContext, attribute);
    }

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.ImportAttributeFMModel#execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecuteApplication() throws TemplateException, IOException {
        ImportAttributeModel tModel = createMock(ImportAttributeModel.class);
        ImportAttributeFMModel fmModel = new ImportAttributeFMModel(tModel);
        TilesContainer container = createMock(TilesContainer.class);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(null);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        replay(request);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        
        GenericServlet servlet = createMock(GenericServlet.class);
        ServletContext servletContext = createMock(ServletContext.class);
        expect(servlet.getServletContext()).andReturn(servletContext).times(4);
        expect(servletContext.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container);
        servletContext.setAttribute("one", "value1");
        servletContext.setAttribute("two", "value2");
        replay(servlet, servletContext);
        ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel);
        expect(model.get(FreemarkerServlet.KEY_APPLICATION)).andReturn(servletContextModel).times(3);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Map<String, Object> params = new HashMap<String, Object>();
        Attribute attribute = createMock(Attribute.class);
        params.put("name", objectWrapper.wrap("myName"));
        params.put("toName", objectWrapper.wrap("myToName"));
        params.put("ignore", objectWrapper.wrap(false));
        params.put("scope", objectWrapper.wrap("application"));

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");
        expect(tModel.getImportedAttributes(container, "myName", "myToName",
                false, env)).andReturn(attributes);
        
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
