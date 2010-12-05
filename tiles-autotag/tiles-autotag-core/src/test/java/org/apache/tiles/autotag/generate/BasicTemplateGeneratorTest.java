/**
 *
 */
package org.apache.tiles.autotag.generate;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tiles.autotag.generate.BasicTemplateGenerator.TCGeneratorDirectoryPair;
import org.apache.tiles.autotag.generate.BasicTemplateGenerator.TSGeneratorDirectoryPair;
import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.junit.Test;

/**
 * Tests {@link BasicTemplateGenerator}.
 *
 * @version $Rev$ $Date$
 */
public class BasicTemplateGeneratorTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.generate.BasicTemplateGenerator#generate(java.lang.String, org.apache.tiles.autotag.model.TemplateSuite, Map)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGenerate() throws IOException {
        File file = File.createTempFile("tiles", "template");
        file.deleteOnExit();
        TemplateSuite suite = createMock(TemplateSuite.class);
        TemplateClass templateClass = createMock(TemplateClass.class);
        TemplateSuiteGenerator templateSuiteGenerator = createMock(TemplateSuiteGenerator.class);
        TemplateClassGenerator templateClassGenerator = createMock(TemplateClassGenerator.class);
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = createMock(Map.class);
        List<TemplateClass> templateClasses = new ArrayList<TemplateClass>();

        templateClasses.add(templateClass);

        expect(suite.getTemplateClasses()).andReturn(templateClasses);
        templateSuiteGenerator.generate(file, "my.package", suite, parameters);
        templateClassGenerator.generate(file, "my.package", suite, templateClass, parameters);

        replay(suite, templateClass, templateSuiteGenerator, templateClassGenerator, parameters);
        TSGeneratorDirectoryPair tsPair = new TSGeneratorDirectoryPair(file, templateSuiteGenerator);
        TCGeneratorDirectoryPair tcPair = new TCGeneratorDirectoryPair(file, templateClassGenerator);
        List<TSGeneratorDirectoryPair> tsList = new ArrayList<BasicTemplateGenerator.TSGeneratorDirectoryPair>();
        tsList.add(tsPair);
        List<TCGeneratorDirectoryPair> tcList = new ArrayList<BasicTemplateGenerator.TCGeneratorDirectoryPair>();
        tcList.add(tcPair);
        BasicTemplateGenerator generator = new BasicTemplateGenerator(tsList, tcList, true, false);
        assertTrue(generator.isGeneratingClasses());
        assertFalse(generator.isGeneratingResources());
        generator.generate("my.package", suite, parameters);
        verify(suite, templateClass, templateSuiteGenerator, templateClassGenerator, parameters);
    }
}
