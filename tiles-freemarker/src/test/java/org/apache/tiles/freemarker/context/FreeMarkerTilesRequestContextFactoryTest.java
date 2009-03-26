package org.apache.tiles.freemarker.context;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * @author antonio
 *
 */
public class FreeMarkerTilesRequestContextFactoryTest {

    /**
     * The object to test.
     */
    private FreeMarkerTilesRequestContextFactory factory;
    
    /**
     * Tests {@link FreeMarkerTilesRequestContextFactory#createRequestContext(TilesApplicationContext, Object...)}. 
     * 
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testCreateRequestContext() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        StringWriter writer = new StringWriter();
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        TilesRequestContextFactory parentFactory = createMock(TilesRequestContextFactory.class);
        TilesApplicationContext applicationContext = createMock(TilesApplicationContext.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ObjectWrapper wrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, response, wrapper);
        expect(model.get("Request")).andReturn(requestModel);
        TilesRequestContext enclosedRequest = createMock(TilesRequestContext.class);
        expect(parentFactory.createRequestContext(applicationContext, request, response)).andReturn(enclosedRequest);
        replay(template, model, parentFactory, applicationContext, request, response, wrapper);
        Environment env = new Environment(template, model, writer);
        factory = new FreeMarkerTilesRequestContextFactory();
        factory.setRequestContextFactory(parentFactory);
        FreeMarkerTilesRequestContext context = (FreeMarkerTilesRequestContext) factory
                .createRequestContext(applicationContext, env);
        assertEquals(env, context.getRequest());
        assertEquals(enclosedRequest, context.getWrappedRequest());
        
    }

}
