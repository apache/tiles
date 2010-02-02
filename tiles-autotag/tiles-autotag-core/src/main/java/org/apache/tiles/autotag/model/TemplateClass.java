package org.apache.tiles.autotag.model;

public class TemplateClass {

    private String name;

    private String documentation;

    private TemplateMethod startMethod;

    private TemplateMethod endMethod;

    private TemplateMethod executeMethod;

    public TemplateClass(String name) {
        this(name, null, null, null);
    }

    public TemplateClass(String name,
            TemplateMethod startMethod, TemplateMethod endMethod,
            TemplateMethod executeMethod) {
        this.name = name;
        this.startMethod = startMethod;
        this.endMethod = endMethod;
        this.executeMethod = executeMethod;
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

    public TemplateMethod getStartMethod() {
        return startMethod;
    }

    public TemplateMethod getEndMethod() {
        return endMethod;
    }

    public TemplateMethod getExecuteMethod() {
        return executeMethod;
    }

    @Override
    public String toString() {
        return "TemplateClass\n[documentation=" + documentation + ",\nendMethod="
                + endMethod + ",\nexecuteMethod=" + executeMethod + ",\nname="
                + name + ",\nstartMethod=" + startMethod + "]";
    }

}
