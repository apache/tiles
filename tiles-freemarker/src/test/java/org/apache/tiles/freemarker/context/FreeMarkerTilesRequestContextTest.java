package org.apache.tiles.freemarker.context;

import java.io.StringWriter;
import java.util.HashMap;

import org.easymock.classextension.EasyMock;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import junit.framework.TestCase;

public class FreeMarkerTilesRequestContextTest extends TestCase {

    private FreeMarkerTilesRequestContext context;
    
    private StringWriter writer;
    
    protected void setUp() throws Exception {
        Template template = EasyMock.createMock(Template.class);
        TemplateHashModel model = EasyMock.createMock(TemplateHashModel.class);
        writer = new StringWriter();
        EasyMock.expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        EasyMock.replay(template, model);
        Environment env = new Environment(template, model, writer);
    }

    public void testGetRequestScope() {
        fail("Not yet implemented");
    }

    public void testDispatch() {
        fail("Not yet implemented");
    }

    public void testGetRequestLocale() {
        fail("Not yet implemented");
    }

    public void testGetRequest() {
        fail("Not yet implemented");
    }

    public void testFreeMarkerTilesRequestContext() {
        fail("Not yet implemented");
    }

}
