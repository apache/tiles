/**
 *
 */
package org.apache.tiles.autotag.generate;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.junit.Test;

/**
 * Tests {@link AbstractTemplateGenerator}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractTemplateGeneratorTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.generate.AbstractTemplateGenerator#generate(java.io.File, java.lang.String, org.apache.tiles.autotag.model.TemplateSuite)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGenerate() throws IOException {
        AbstractTemplateGenerator generator = createMockBuilder(AbstractTemplateGenerator.class).withConstructor().createMock();
        File file = File.createTempFile("tiles", "template");
        file.deleteOnExit();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass templateClass = createMock(TemplateClass.class);
        TemplateSuiteGenerator templateSuiteGenerator = createMock(TemplateSuiteGenerator.class);
        TemplateClassGenerator templateClassGenerator = createMock(TemplateClassGenerator.class);
        List<TemplateClass> templateClasses = new ArrayList<TemplateClass>();

        templateClasses.add(templateClass);

        expect(suite.getTemplateClasses()).andReturn(templateClasses);
        templateSuiteGenerator.generate(file, "my.package", suite);
        templateClassGenerator.generate(file, "my.package", suite, templateClass);

        replay(generator, suite, templateClass, templateSuiteGenerator, templateClassGenerator);
        generator.addTemplateSuiteGenerator(templateSuiteGenerator);
        generator.addTemplateClassGenerator(templateClassGenerator);
        generator.generate(file, "my.package", suite);
        verify(generator, suite, templateClass, templateSuiteGenerator, templateClassGenerator);
    }
}
