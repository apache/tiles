package org.apache.tiles.autotag.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateSuite {

    private String name;

    private String documentation;

    private Map<String, String> customVariables;

    Map<String, TemplateClass> templateClasses;

    public TemplateSuite(String name, String documentation) {
        this(name, documentation, null);
    }

    public TemplateSuite(String name, String documentation,
            Iterable<? extends TemplateClass> classes) {
        this.name = name;
        this.documentation = documentation;
        customVariables = new HashMap<String, String>();
        templateClasses = new LinkedHashMap<String, TemplateClass>();
        if (classes != null) {
            for (TemplateClass templateClass : classes) {
                templateClasses.put(templateClass.getName(), templateClass);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void addTemplateClass(TemplateClass clazz) {
        templateClasses.put(clazz.getName(), clazz);
    }

    public Collection<TemplateClass> getTemplateClasses() {
        return templateClasses.values();
    }

    public TemplateClass getTemplateClassByName(String name) {
        return templateClasses.get(name);
    }

    @Override
    public String toString() {
        return "TemplateSuite\n[documentation=" + documentation + ", name="
                + name + ", templateClasses=\n" + templateClasses + "]";
    }

    public Map<String, String> getCustomVariables() {
        return customVariables;
    }
}
