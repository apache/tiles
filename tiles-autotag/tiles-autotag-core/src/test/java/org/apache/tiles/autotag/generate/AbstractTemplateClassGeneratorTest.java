/**
 *
 */
package org.apache.tiles.autotag.generate;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Test;

/**
 * Tests {@link AbstractTemplateClassGenerator}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractTemplateClassGeneratorTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.generate.AbstractTemplateClassGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, org.apache.tiles.autotag.model.TemplateClass)}.
     * @throws Exception If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     */
    @Test
    public void testGenerate() throws ResourceNotFoundException, ParseErrorException, Exception {
        VelocityEngine velocityEngine = createMock(VelocityEngine.class);
        AbstractTemplateClassGenerator generator = createMockBuilder(AbstractTemplateClassGenerator.class).withConstructor(velocityEngine).createMock();
        File directory = File.createTempFile("autotag", null);
        directory.delete();
        directory.mkdir();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass clazz = createMock(TemplateClass.class);
        Template template = createMock(Template.class);
        String packageName = "org.apache.tiles.autotag.test";

        expect(generator.getDirectoryName(directory, packageName, suite, clazz)).andReturn("mydir");
        File mydir = new File(directory, "mydir");
        expect(generator.getFilename(mydir , packageName, suite, clazz)).andReturn("myfile.txt");
        String sampleVmPath = "/sample.vm";
        expect(generator.getTemplatePath(mydir, packageName, suite, clazz)).andReturn(sampleVmPath);
        expect(velocityEngine.getTemplate("/sample.vm")).andReturn(template);
        template.merge(isA(VelocityContext.class), isA(FileWriter.class));

        replay(velocityEngine, generator, suite, clazz, template);
        generator.generate(directory, packageName, suite, clazz);
        verify(velocityEngine, generator, suite, clazz, template);
        FileUtils.deleteDirectory(directory);
    }

}
