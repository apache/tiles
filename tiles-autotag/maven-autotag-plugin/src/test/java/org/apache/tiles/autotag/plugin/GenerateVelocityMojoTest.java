/**
 *
 */
package org.apache.tiles.autotag.plugin;

import static org.junit.Assert.*;

import org.apache.tiles.autotag.velocity.VelocityTemplateGeneratorFactory;
import org.junit.Test;

/**
 * Tests {@link GenerateVelocityMojo}.
 *
 * @version $Rev$ $Date$
 */
public class GenerateVelocityMojoTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.plugin.GenerateVelocityMojo#createTemplateGeneratorFactory(org.apache.velocity.app.VelocityEngine)}.
     */
    @Test
    public void testCreateTemplateGeneratorFactory() {
        GenerateVelocityMojo mojo = new GenerateVelocityMojo();
        assertTrue(mojo.createTemplateGeneratorFactory(null) instanceof VelocityTemplateGeneratorFactory);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.plugin.GenerateVelocityMojo#getParameters()}.
     */
    @Test
    public void testGetParameters() {
        GenerateVelocityMojo mojo = new GenerateVelocityMojo();
        assertNull(mojo.getParameters());
    }

}
