/**
 *
 */
package org.apache.tiles.autotag.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.autotag.core.internal.ExampleExecutableTemplate;
import org.apache.tiles.autotag.core.internal.ExampleTemplate;
import org.apache.tiles.autotag.core.internal.NotFeasibleExampleTemplate;
import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateMethod;
import org.apache.tiles.autotag.model.TemplateParameter;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link QDoxTemplateSuiteFactory}.
 *
 * @version $Rev$ $Date$
 */
public class QDoxTemplateSuiteFactoryTest {

    private QDoxTemplateSuiteFactory factory;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() {
        factory = new QDoxTemplateSuiteFactory(getClass().getResource("/org/apache/tiles/autotag/core/internal/ExampleTemplate.java"),
                getClass().getResource("/org/apache/tiles/autotag/core/internal/ExampleExecutableTemplate.java"),
                getClass().getResource("/org/apache/tiles/autotag/core/internal/NotFeasibleExampleTemplate.java"));
        factory.setSuiteName("The suite name");
        factory.setSuiteDocumentation("This are the docs");
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.core.DefaultTemplateSuiteFactory#createTemplateSuite()}.
     */
    @Test
    public void testCreateTemplateSuite() {
        TemplateSuite suite = factory.createTemplateSuite();
        assertEquals("The suite name", suite.getName());
        assertEquals("This are the docs", suite.getDocumentation());
        assertEquals(2, suite.getTemplateClasses().size());

        TemplateClass templateClass = suite.getTemplateClassByName(ExampleTemplate.class.getName());
        assertNotNull(templateClass);
        assertEquals(ExampleTemplate.class.getName(), templateClass.getName());
        assertEquals("Example start/stop template.", templateClass.getDocumentation());
        TemplateMethod templateMethod = templateClass.getExecuteMethod();
        assertNotNull(templateMethod);
        assertTrue(templateMethod.hasBody());
        assertTrue(templateClass.hasBody());
        assertEquals("execute", templateMethod.getName());
        assertEquals("It starts.", templateMethod.getDocumentation());
        List<TemplateParameter> parameters = new ArrayList<TemplateParameter>(templateMethod.getParameters());
        assertEquals(4, parameters.size());
        TemplateParameter parameter = parameters.get(0);
        assertEquals("one", parameter.getName());
        assertEquals("java.lang.String", parameter.getType());
        assertEquals("Parameter one.", parameter.getDocumentation());
        parameter = parameters.get(1);
        assertEquals("two", parameter.getName());
        assertEquals("int", parameter.getType());
        assertEquals("Parameter two.", parameter.getDocumentation());
        parameter = parameters.get(2);
        assertEquals("request", parameter.getName());
        assertEquals(Request.class.getName(), parameter.getType());
        assertEquals("The request.", parameter.getDocumentation());
        parameter = parameters.get(3);
        assertEquals("modelBody", parameter.getName());
        assertEquals(ModelBody.class.getName(), parameter.getType());
        assertEquals("The model body.", parameter.getDocumentation());

        templateClass = suite.getTemplateClassByName(ExampleExecutableTemplate.class.getName());
        assertNotNull(templateClass);
        assertEquals(ExampleExecutableTemplate.class.getName(), templateClass.getName());
        assertEquals("Example executable template.", templateClass.getDocumentation());
        templateMethod = templateClass.getExecuteMethod();
        assertNotNull(templateMethod);
        assertEquals("execute", templateMethod.getName());
        assertEquals("It executes.", templateMethod.getDocumentation());
        parameters = new ArrayList<TemplateParameter>(templateMethod.getParameters());
        assertEquals(3, parameters.size());
        parameter = parameters.get(0);
        assertEquals("one", parameter.getName());
        assertEquals("java.lang.String", parameter.getType());
        assertEquals("Parameter one.", parameter.getDocumentation());
        parameter = parameters.get(1);
        assertEquals("two", parameter.getName());
        assertEquals("int", parameter.getType());
        assertEquals("Parameter two.", parameter.getDocumentation());
        parameter = parameters.get(2);
        assertEquals("request", parameter.getName());
        assertEquals(Request.class.getName(), parameter.getType());
        assertEquals("The request.", parameter.getDocumentation());

        assertNull(suite.getTemplateClassByName(NotFeasibleExampleTemplate.class.getName()));
    }

}
