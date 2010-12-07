/**
 *
 */
package org.apache.tiles.autotag.plugin;

import static org.junit.Assert.*;

import org.apache.tiles.autotag.freemarker.FMTemplateGeneratorFactory;
import org.junit.Test;

/**
 * Tests {@link GenerateFreemarkerMojo}.
 *
 * @version $Rev$ $Date$
 */
public class GenerateFreemarkerMojoTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.plugin.GenerateFreemarkerMojo#createTemplateGeneratorFactory(org.apache.velocity.app.VelocityEngine)}.
     */
    @Test
    public void testCreateTemplateGeneratorFactory() {
        GenerateFreemarkerMojo mojo = new GenerateFreemarkerMojo();
        assertTrue(mojo.createTemplateGeneratorFactory(null) instanceof FMTemplateGeneratorFactory);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.plugin.GenerateFreemarkerMojo#getParameters()}.
     */
    @Test
    public void testGetParameters() {
        GenerateFreemarkerMojo mojo = new GenerateFreemarkerMojo();
        assertNull(mojo.getParameters());
    }

}
