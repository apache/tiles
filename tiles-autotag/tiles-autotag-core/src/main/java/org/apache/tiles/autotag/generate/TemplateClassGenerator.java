package org.apache.tiles.autotag.generate;

import java.io.File;

import org.apache.tiles.autotag.model.TemplateClass;

public interface TemplateClassGenerator {

    void generate(File directory, String packageName, TemplateClass clazz);
}
