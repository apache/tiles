package org.apache.tiles.autotag.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.apache.tiles.autotag.core.AutotagRuntimeException;
import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.tiles.autotag.tool.StringTool;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public abstract class AbstractTemplateClassGenerator implements
        TemplateClassGenerator {

    @Override
    public void generate(File directory, String packageName,
            TemplateSuite suite, TemplateClass clazz) {
        File dir = new File(directory, getDirectoryName(directory, packageName,
                suite, clazz));
        dir.mkdirs();
        File file = new File(dir, getFilename(dir, packageName, suite, clazz));
        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("suite", suite);
        context.put("clazz", clazz);
        context.put("stringTool", new StringTool());
        try {
            file.createNewFile();
            Template template = Velocity.getTemplate(getTemplatePath(dir,
                    packageName, suite, clazz));
            Writer writer = new FileWriter(file);
            try {
                template.merge(context, writer);
            } finally {
                writer.close();
            }
        } catch (ResourceNotFoundException e) {
            throw new AutotagRuntimeException("Cannot find template resource",
                    e);
        } catch (ParseErrorException e) {
            throw new AutotagRuntimeException(
                    "The template resource is not parseable", e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AutotagRuntimeException(
                    "Another generic exception while parsing the template resource",
                    e);
        }
    }

    protected abstract String getTemplatePath(File directory,
            String packageName, TemplateSuite suite, TemplateClass clazz);

    protected abstract String getFilename(File directory, String packageName,
            TemplateSuite suite, TemplateClass clazz);

    protected abstract String getDirectoryName(File directory,
            String packageName, TemplateSuite suite, TemplateClass clazz);
}
