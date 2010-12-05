package org.apache.tiles.autotag.plugin;

import java.util.Map;

import org.apache.tiles.autotag.freemarker.FMTemplateGeneratorFactory;
import org.apache.tiles.autotag.generate.TemplateGeneratorBuilder;
import org.apache.tiles.autotag.generate.TemplateGeneratorFactory;
import org.apache.velocity.app.VelocityEngine;

/**
 * Generates Freemarker code.
 *
 * @goal generate-freemarker
 *
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GenerateFreemarkerMojo extends AbstractGenerateMojo {

    /** {@inheritDoc} */
    @Override
    protected Map<String, String> getParameters() {
        return null;
    }

    @Override
    protected TemplateGeneratorFactory createTemplateGeneratorFactory(
            VelocityEngine velocityEngine) {
        return new FMTemplateGeneratorFactory(classesOutputDirectory,
                velocityEngine, TemplateGeneratorBuilder.createNewInstance());
    }
}
