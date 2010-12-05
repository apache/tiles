/**
 *
 */
package org.apache.tiles.autotag.velocity;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateMethod;
import org.apache.tiles.autotag.model.TemplateParameter;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.tiles.request.Request;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;

/**
 * Tests {@link VelocityPropertiesGenerator}.
 *
 * @version $Rev$ $Date$
 */
public class VelocityPropertiesGeneratorTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.velocity.VelocityPropertiesGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, Map)}.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testGenerate() throws Exception {
        Properties props = new Properties();
        InputStream propsStream = getClass().getResourceAsStream("/org/apache/tiles/autotag/velocity.properties");
        props.load(propsStream);
        propsStream.close();
        VelocityEngine velocityEngine = new VelocityEngine(props);

        VelocityPropertiesGenerator generator = new VelocityPropertiesGenerator(velocityEngine);
        File file = File.createTempFile("autotag", null);
        file.delete();
        file.mkdir();
        file.deleteOnExit();
        TemplateSuite suite = new TemplateSuite("tldtest", "Test for TLD docs.");

        List<TemplateParameter> params = new ArrayList<TemplateParameter>();
        TemplateParameter param = new TemplateParameter("one", "one", "java.lang.String", null, true);
        param.setDocumentation("Parameter one.");
        params.add(param);
        param = new TemplateParameter("two", "two", "int", null, false);
        param.setDocumentation("Parameter two.");
        params.add(param);
        param = new TemplateParameter("three", "three", "long", null, false);
        param.setDocumentation("Parameter three.");
        params.add(param);
        param = new TemplateParameter("request", "request", Request.class.getName(), null, false);
        param.setDocumentation("The request.");
        params.add(param);
        param = new TemplateParameter("modelBody", "modelBody", ModelBody.class.getName(), null, false);
        param.setDocumentation("The body.");
        params.add(param);
        TemplateMethod executeMethod = new TemplateMethod("execute", params);

        TemplateClass clazz = new TemplateClass("org.apache.tiles.autotag.template.DoStuffTemplate",
                "doStuff", "DoStuff", executeMethod);
        clazz.setDocumentation("Documentation of the DoStuff class");

        suite.addTemplateClass(clazz);
        params = new ArrayList<TemplateParameter>();
        param = new TemplateParameter("one", "one", "java.lang.Double", null, true);
        param.setDocumentation("Parameter one.");
        params.add(param);
        param = new TemplateParameter("two", "two", "float", null, false);
        param.setDocumentation("Parameter two.");
        params.add(param);
        param = new TemplateParameter("three", "three", "java.util.Date", null, false);
        param.setDocumentation("Parameter three.");
        params.add(param);
        param = new TemplateParameter("request", "request", Request.class.getName(), null, false);
        param.setDocumentation("The request.");
        params.add(param);
        executeMethod = new TemplateMethod("execute", params);

        clazz = new TemplateClass("org.apache.tiles.autotag.template.DoStuffNoBodyTemplate",
                "doStuffNoBody", "DoStuffNoBody", executeMethod);
        clazz.setDocumentation("Documentation of the DoStuffNoBody class");

        suite.addTemplateClass(clazz);

        generator.generate(file, "org.apache.tiles.autotag.velocity.test", suite, null);

        InputStream expected = getClass().getResourceAsStream("/velocity.properties.test");
        File effectiveFile = new File(file, "META-INF/velocity.properties");
        assertTrue(effectiveFile.exists());
        InputStream effective = new FileInputStream(effectiveFile);
        assertTrue(IOUtils.contentEquals(effective, expected));
        effective.close();
        expected.close();

        FileUtils.deleteDirectory(file);
    }

}
