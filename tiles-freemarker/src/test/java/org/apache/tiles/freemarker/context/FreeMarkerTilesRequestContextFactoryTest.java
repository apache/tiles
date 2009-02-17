package org.apache.tiles.freemarker.context;

import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.easymock.classextension.EasyMock;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import junit.framework.TestCase;

public class FreeMarkerTilesRequestContextFactoryTest extends TestCase{

    private FreeMarkerTilesRequestContextFactory factory;
    
    public void testCreateRequestContext() throws TemplateModelException {
        Template template = EasyMock.createMock(Template.class);
        TemplateHashModel model = EasyMock.createMock(TemplateHashModel.class);
        StringWriter writer = new StringWriter();
        EasyMock.expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        TilesRequestContextFactory parentFactory = EasyMock.createMock(TilesRequestContextFactory.class);
        TilesApplicationContext applicationContext = EasyMock.createMock(TilesApplicationContext.class);
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
        ObjectWrapper wrapper = EasyMock.createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, response, wrapper);
        EasyMock.expect(model.get("Request")).andReturn(requestModel);
        TilesRequestContext enclosedRequest = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.expect(parentFactory.createRequestContext(applicationContext, request, response)).andReturn(enclosedRequest);
        EasyMock.replay(template, model, parentFactory, applicationContext, request, response, wrapper);
        Environment env = new Environment(template, model, writer);
        factory = new FreeMarkerTilesRequestContextFactory();
        factory.setRequestContextFactory(parentFactory);
        FreeMarkerTilesRequestContext context = (FreeMarkerTilesRequestContext) factory
                .createRequestContext(applicationContext, env);
        assertEquals(env, context.getRequest());
        assertEquals(enclosedRequest, context.getWrappedRequest());
        
    }

}
