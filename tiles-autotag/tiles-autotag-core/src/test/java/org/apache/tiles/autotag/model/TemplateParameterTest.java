package org.apache.tiles.autotag.model;

import static org.junit.Assert.*;

import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.request.Request;
import org.junit.Test;

/**
 * Tests {@link TemplateParameter}.
 *
 * @version $Rev$ $Date$
 */
public class TemplateParameterTest {

    /**
     * Tests {@link TemplateParameter#TemplateParameter(String, String, String, String, boolean)}.
     */
    @Test
    public void testTemplateParameter() {
        TemplateParameter parameter = new TemplateParameter("name", "exportedName", "type", "defaultValue", true);
        assertEquals("name", parameter.getName());
        assertEquals("exportedName", parameter.getExportedName());
        assertEquals("type", parameter.getType());
        assertEquals("defaultValue", parameter.getDefaultValue());
        assertTrue(parameter.isRequired());
        assertEquals("ExportedName", parameter.getGetterSetterSuffix());
        assertFalse(parameter.isBody());
        assertFalse(parameter.isRequest());

        parameter = new TemplateParameter("name", "exportedName", Request.class.getName(), "defaultValue", false);
        assertEquals("name", parameter.getName());
        assertEquals("exportedName", parameter.getExportedName());
        assertEquals(Request.class.getName(), parameter.getType());
        assertEquals("defaultValue", parameter.getDefaultValue());
        assertFalse(parameter.isRequired());
        assertEquals("ExportedName", parameter.getGetterSetterSuffix());
        assertFalse(parameter.isBody());
        assertTrue(parameter.isRequest());

        parameter = new TemplateParameter("name", "exportedName", ModelBody.class.getName(), "defaultValue", false);
        assertEquals("name", parameter.getName());
        assertEquals("exportedName", parameter.getExportedName());
        assertEquals(ModelBody.class.getName(), parameter.getType());
        assertEquals("defaultValue", parameter.getDefaultValue());
        assertFalse(parameter.isRequired());
        assertEquals("ExportedName", parameter.getGetterSetterSuffix());
        assertTrue(parameter.isBody());
        assertFalse(parameter.isRequest());
    }

    /**
     * Tests {@link TemplateParameter#setDocumentation(String)}.
     */
    @Test
    public void testSetDocumentation() {
        TemplateParameter parameter = new TemplateParameter("name", "exportedName", "type", "defaultValue", true);
        parameter.setDocumentation("docs");
        assertEquals("docs", parameter.getDocumentation());
    }

    /**
     * Tests {@link TemplateParameter#toString()}.
     */
    @Test
    public void testToString() {
        TemplateParameter parameter = new TemplateParameter("name", "exportedName", "type", "defaultValue", true);
        assertEquals("TemplateParameter [name=name, exportedName=exportedName, " +
        		"documentation=null, type=type, defaultValue=defaultValue, required=true]", parameter.toString());
    }

}
