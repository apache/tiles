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
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.freemarker.context.FreeMarkerUtil;
import org.apache.tiles.freemarker.io.NullWriter;
import org.apache.tiles.template.AddListAttributeModel;
import org.junit.Before;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
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
public class AddListAttributeFMModelTest {

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
     * Test method for {@link org.apache.tiles.freemarker.template.AddListAttributeFMModel#execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)}.
     * @throws IOException If something goes wrong.
     * @throws TemplateException If something goes wrong.
     */
    @Test
    public void testExecute() throws TemplateException, IOException {
        AddListAttributeModel tModel = createMock(AddListAttributeModel.class);
        AddListAttributeFMModel fmModel = new AddListAttributeFMModel(tModel);
        HttpServletRequest request = createMock(HttpServletRequest.class);

        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, objectWrapper);
        expect(model.get(FreemarkerServlet.KEY_REQUEST)).andReturn(requestModel);
        initEnvironment();

        TemplateDirectiveBody body = createMock(TemplateDirectiveBody.class);
        Stack<Object> composeStack = new Stack<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("role", objectWrapper.wrap("myRole"));
     
        expect(request.getAttribute(FreeMarkerUtil.COMPOSE_STACK_ATTRIBUTE_NAME)).andReturn(composeStack);
        tModel.start(composeStack, "myRole");
        tModel.end(composeStack);
        body.render(isA(NullWriter.class));
        
        replay(request, tModel, body);
        fmModel.execute(env, params, null, body);
        verify(template, model, request, tModel, body);
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
