package org.apache.tiles.freemarker.context;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;

import junit.framework.TestCase;

import org.apache.tiles.context.TilesRequestContext;
import org.easymock.classextension.EasyMock;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;

public class FreeMarkerTilesRequestContextTest extends TestCase {

    private FreeMarkerTilesRequestContext context;
    
    private StringWriter writer;

    private Environment env;
    
    private Locale locale;
    
    protected void setUp() throws Exception {
        Template template = EasyMock.createMock(Template.class);
        TemplateHashModel model = EasyMock.createMock(TemplateHashModel.class);
        writer = new StringWriter();
        EasyMock.expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        EasyMock.replay(template, model);
        env = new Environment(template, model, writer);
        locale = Locale.ITALY;
        env.setLocale(locale);
    }

    public void testDispatch() throws IOException {
        String path = "this way";
        TilesRequestContext enclosedRequest = EasyMock.createMock(TilesRequestContext.class);
        enclosedRequest.include(path);
        EasyMock.replay(enclosedRequest);
        context = new FreeMarkerTilesRequestContext(enclosedRequest, env);
        context.dispatch(path);
        EasyMock.verify(enclosedRequest);
    }

    public void testGetRequestLocale() {
        TilesRequestContext enclosedRequest = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.replay(enclosedRequest);
        context = new FreeMarkerTilesRequestContext(enclosedRequest, env);
        assertEquals(locale, context.getRequestLocale());
        EasyMock.verify(enclosedRequest);
    }

    public void testGetRequest() {
        TilesRequestContext enclosedRequest = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.replay(enclosedRequest);
        context = new FreeMarkerTilesRequestContext(enclosedRequest, env);
        assertEquals(env, context.getRequest());
        EasyMock.verify(enclosedRequest);
    }

    public void testGetResponse() {
        TilesRequestContext enclosedRequest = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.replay(enclosedRequest);
        context = new FreeMarkerTilesRequestContext(enclosedRequest, env);
        assertEquals(env, context.getResponse());
        EasyMock.verify(enclosedRequest);
    }
}
