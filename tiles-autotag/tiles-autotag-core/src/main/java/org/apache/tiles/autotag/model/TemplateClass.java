package org.apache.tiles.autotag.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateClass {

    private String name;

    private String tagName;

    private String tagClassPrefix;

    private String documentation;

    private TemplateMethod executeMethod;

    public TemplateClass(String name) {
        this(name, null, null, null);
    }

    public TemplateClass(String name, String tagName, String tagClassPrefix,
            TemplateMethod executeMethod) {
        this.name = name;
        this.tagName = tagName;
        this.tagClassPrefix = tagClassPrefix;
        this.executeMethod = executeMethod;
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        int pos = name.lastIndexOf('.');
        if (pos >= 0) {
            return name.substring(pos + 1);
        }
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

    public TemplateMethod getExecuteMethod() {
        return executeMethod;
    }

    public Collection<TemplateParameter> getParameters() {
        Map<String, TemplateParameter> params = new LinkedHashMap<String, TemplateParameter>();
        fillRegularParameters(params, executeMethod);
        return params.values();
    }

    public boolean hasBody() {
        return executeMethod.hasBody();
    }

    @Override
    public String toString() {
        return "TemplateClass [name=" + name + ", tagName=" + tagName
                + ", tagClassPrefix=" + tagClassPrefix + ", documentation="
                + documentation + ", executeMethod=" + executeMethod + "]";
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
}
