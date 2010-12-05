/**
 *
 */
package org.apache.tiles.autotag.freemarker;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;

import org.apache.tiles.autotag.generate.TemplateGenerator;
import org.apache.tiles.autotag.generate.TemplateGeneratorBuilder;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;

/**
 * Tests {@link FMTemplateGeneratorFactory}.
 *
 * @version $Rev$ $Date$
 */
public class FMTemplateGeneratorFactoryTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.freemarker.FMTemplateGeneratorFactory#createTemplateGenerator()}.
     */
    @Test
    public void testCreateTemplateGenerator() {
        File classesOutputDirectory = createMock(File.class);
        VelocityEngine velocityEngine = createMock(VelocityEngine.class);
        TemplateGeneratorBuilder builder = createMock(TemplateGeneratorBuilder.class);
        TemplateGenerator generator = createMock(TemplateGenerator.class);

        expect(builder.setClassesOutputDirectory(classesOutputDirectory)).andReturn(builder);
        expect(builder.addClassesTemplateSuiteGenerator(isA(FMModelRepositoryGenerator.class))).andReturn(builder);
        expect(builder.addClassesTemplateClassGenerator(isA(FMModelGenerator.class))).andReturn(builder);
        expect(builder.build()).andReturn(generator);

        replay(classesOutputDirectory, velocityEngine, builder, generator);
        FMTemplateGeneratorFactory factory = new FMTemplateGeneratorFactory(classesOutputDirectory, velocityEngine, builder);
        assertSame(generator, factory.createTemplateGenerator());
        verify(classesOutputDirectory, velocityEngine, builder, generator);
    }

}
