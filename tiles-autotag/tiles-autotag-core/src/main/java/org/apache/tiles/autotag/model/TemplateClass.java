package org.apache.tiles.autotag.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateClass {

    private String name;

    private String tagName;

    private String tagClassPrefix;

    private String documentation;

    private TemplateMethod startMethod;

    private TemplateMethod endMethod;

    private TemplateMethod executeMethod;

    public TemplateClass(String name) {
        this(name, null, null, null, null, null);
    }

    public TemplateClass(String name, String tagName, String tagClassPrefix,
            TemplateMethod startMethod, TemplateMethod endMethod,
            TemplateMethod executeMethod) {
        this.name = name;
        this.tagName = tagName;
        this.tagClassPrefix = tagClassPrefix;
        this.startMethod = startMethod;
        this.endMethod = endMethod;
        this.executeMethod = executeMethod;
    }

    public String getName() {
        return name;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagClassPrefix() {
        return tagClassPrefix;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public TemplateMethod getStartMethod() {
        return startMethod;
    }

    public TemplateMethod getEndMethod() {
        return endMethod;
    }

    public TemplateMethod getExecuteMethod() {
        return executeMethod;
    }

    public Collection<TemplateParameter> getParameters() {
        Map<String, TemplateParameter> params = new LinkedHashMap<String, TemplateParameter>();
        fillRegularParameters(params, startMethod);
        fillRegularParameters(params, endMethod);
        fillRegularParameters(params, executeMethod);
        return params.values();
    }

    private void fillRegularParameters(Map<String, TemplateParameter> params,
            TemplateMethod method) {
        if (method != null) {
            for (TemplateParameter param: method.getParameters()) {
                if (!param.isRequest() && !param.isBody()) {
                    params.put(param.getName(), param);
                }
            }
        }
    }

    public boolean hasBody() {
        return (startMethod != null && endMethod != null);
    }

    @Override
    public String toString() {
        return "TemplateClass\n[documentation=" + documentation + ",\nendMethod="
                + endMethod + ",\nexecuteMethod=" + executeMethod + ",\nname="
                + name + ",\nstartMethod=" + startMethod + "]";
    }

}
