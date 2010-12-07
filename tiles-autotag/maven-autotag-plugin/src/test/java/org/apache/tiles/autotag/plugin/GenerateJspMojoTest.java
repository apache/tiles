/**
 *
 */
package org.apache.tiles.autotag.plugin;

import static org.junit.Assert.*;

import org.apache.tiles.autotag.jsp.JspTemplateGeneratorFactory;
import org.junit.Test;

/**
 * Tests {@link GenerateJspMojo}.
 *
 * @version $Rev$ $Date$
 */
public class GenerateJspMojoTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.plugin.GenerateJspMojo#createTemplateGeneratorFactory(org.apache.velocity.app.VelocityEngine)}.
     */
    @Test
    public void testCreateTemplateGeneratorFactory() {
        GenerateJspMojo mojo = new GenerateJspMojo();
        assertTrue(mojo.createTemplateGeneratorFactory(null) instanceof JspTemplateGeneratorFactory);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.plugin.GenerateJspMojo#getParameters()}.
     */
    @Test
    public void testGetParameters() {
        GenerateJspMojo mojo = new GenerateJspMojo();
        mojo.taglibURI = "http://www.test.org/taglib";
        assertEquals("http://www.test.org/taglib", mojo.getParameters().get("taglibURI"));
    }

}
