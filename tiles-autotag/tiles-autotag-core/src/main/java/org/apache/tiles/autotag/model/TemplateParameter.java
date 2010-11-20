package org.apache.tiles.autotag.model;

import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.request.Request;

public class TemplateParameter {

    private String name;

    private String exportedName;

    private String documentation;

    private String type;

    private String defaultValue;

    private boolean required;

    public TemplateParameter(String name, String exportedName, String type, String defaultValue, boolean required) {
        this.name = name;
        this.exportedName = exportedName;
        this.type = type;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getName() {
        return name;
    }

    public String getExportedName() {
        return exportedName;
    }

    public String getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isBody() {
        return ModelBody.class.getName().equals(type);
    }

    public boolean isRequest() {
        return Request.class.getName().equals(type);
    }

    public String getGetterSetterSuffix() {
        return exportedName.substring(0, 1).toUpperCase() + exportedName.substring(1);
    }

    @Override
    public String toString() {
        return "TemplateParameter [name=" + name + ", exportedName="
                + exportedName + ", documentation=" + documentation + ", type="
                + type + ", defaultValue=" + defaultValue + ", required="
                + required + "]";
    }
}
