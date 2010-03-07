package org.apache.tiles.autotag.model;

import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.request.Request;

public class TemplateParameter {

    private String name;

    private String exportedName;

    private String documentation;

    private String type;

    private boolean required;

    public TemplateParameter(String name, String exportedName, String type, boolean required) {
        this.name = name;
        this.exportedName = exportedName;
        this.type = type;
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
        return "TemplateParameter\n[documentation=" + documentation
                + ",\nname=" + name + ",\nexportedName=" + exportedName
                + ", required=" + required + ", type=" + type + "]";
    }

}
