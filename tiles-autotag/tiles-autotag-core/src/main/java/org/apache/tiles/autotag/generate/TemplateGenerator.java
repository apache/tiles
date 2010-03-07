package org.apache.tiles.autotag.generate;


import org.apache.tiles.autotag.model.TemplateSuite;

public interface TemplateGenerator {

    void generate(String packageName, TemplateSuite suite);
}
