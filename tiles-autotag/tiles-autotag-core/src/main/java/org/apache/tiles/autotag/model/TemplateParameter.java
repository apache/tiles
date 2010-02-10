package org.apache.tiles.autotag.model;

import org.apache.tiles.request.Request;

public class TemplateParameter {

    private String name;

    private String documentation;

    private String type;

    private boolean required;

    private boolean body;

    public TemplateParameter(String name, String type, boolean required,
            boolean body) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.body = body;
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

    public String getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isBody() {
        return body;
    }

    public boolean isRequest() {
        return Request.class.getName().equals(type);
    }

    @Override
    public String toString() {
        return "TemplateParameter\n[body=" + body + ",\ndocumentation="
                + documentation + ",\nname=" + name + ", required=" + required
                + ", type=" + type + "]";
    }

}
