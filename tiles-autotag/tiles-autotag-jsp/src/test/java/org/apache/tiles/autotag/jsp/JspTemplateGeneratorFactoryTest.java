/**
 *
 */
package org.apache.tiles.autotag.jsp;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;

import org.apache.tiles.autotag.generate.TemplateGenerator;
import org.apache.tiles.autotag.generate.TemplateGeneratorBuilder;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;

/**
 * Tests {@link JspTemplateGeneratorFactory}.
 *
 * @version $Rev$ $Date$
 */
public class JspTemplateGeneratorFactoryTest {

    /**
     * Test method for {@link JspTemplateGeneratorFactory#createTemplateGenerator()}.
     */
    @Test
    public void testCreateTemplateGenerator() {
        File classesOutputDirectory = createMock(File.class);
        File resourcesOutputDirectory = createMock(File.class);
        VelocityEngine velocityEngine = createMock(VelocityEngine.class);
        TemplateGeneratorBuilder builder = createMock(TemplateGeneratorBuilder.class);
        TemplateGenerator generator = createMock(TemplateGenerator.class);

        expect(builder.setClassesOutputDirectory(classesOutputDirectory)).andReturn(builder);
        expect(builder.setResourcesOutputDirectory(resourcesOutputDirectory)).andReturn(builder);
        expect(builder.addResourcesTemplateSuiteGenerator(isA(TLDGenerator.class))).andReturn(builder);
        expect(builder.addClassesTemplateClassGenerator(isA(TagClassGenerator.class))).andReturn(builder);
        expect(builder.build()).andReturn(generator);

        replay(classesOutputDirectory, resourcesOutputDirectory, velocityEngine, builder, generator);
        JspTemplateGeneratorFactory factory = new JspTemplateGeneratorFactory(classesOutputDirectory, resourcesOutputDirectory, velocityEngine, builder);
        assertSame(generator, factory.createTemplateGenerator());
        verify(classesOutputDirectory, resourcesOutputDirectory, velocityEngine, builder, generator);
    }

}
