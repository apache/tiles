package org.apache.tiles.autotag.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateMethod {

    private String name;

    private String documentation;

    private Map<String, TemplateParameter> parameters;

    public TemplateMethod(String name,
            Iterable<? extends TemplateParameter> parameters) {
        this.name = name;
        this.parameters = new LinkedHashMap<String, TemplateParameter>();
        for (TemplateParameter parameter : parameters) {
            this.parameters.put(parameter.getName(), parameter);
        }
    }

    public String getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public Collection<TemplateParameter> getParameters() {
        return parameters.values();
    }

    public TemplateParameter getParameterByName(String name) {
        return parameters.get(name);
    }

    public boolean hasBody() {
        if (parameters.size() >= 2) {
            for (TemplateParameter param : parameters.values()) {
                if (param.isBody()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "TemplateMethod\n[documentation=" + documentation + ", name="
                + name + ", parameters=\n" + parameters + "]";
    }

}
