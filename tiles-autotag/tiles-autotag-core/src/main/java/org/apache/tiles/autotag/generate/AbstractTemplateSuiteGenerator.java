package org.apache.tiles.autotag.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.tiles.autotag.core.AutotagRuntimeException;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.tiles.autotag.tool.StringTool;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public abstract class AbstractTemplateSuiteGenerator implements TemplateSuiteGenerator {

    private VelocityEngine velocityEngine;

    public AbstractTemplateSuiteGenerator(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public void generate(File directory, String packageName, TemplateSuite suite) {
        File dir = new File(directory, getDirectoryName(directory, packageName, suite));
        dir.mkdirs();
        File file = new File(dir, getFilename(dir, packageName, suite));
        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("suite", suite);
        context.put("stringTool", new StringTool());
        try {
            file.createNewFile();
            Template template = velocityEngine.getTemplate(getTemplatePath(dir,
                    packageName, suite));
            Writer writer = new FileWriter(file);
            try {
                template.merge(context, writer);
            } finally {
                writer.close();
            }
        } catch (ResourceNotFoundException e) {
            throw new AutotagRuntimeException("Cannot find template resource", e);
        } catch (ParseErrorException e) {
            throw new AutotagRuntimeException("The template resource is not parseable", e);
        } catch (IOException e) {
            throw new AutotagRuntimeException(
                    "I/O Exception when generating file", e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AutotagRuntimeException(
                    "Another generic exception while parsing the template resource",
                    e);
        }
    }

    protected abstract String getTemplatePath(File directory, String packageName, TemplateSuite suite);

    protected abstract String getFilename(File directory, String packageName, TemplateSuite suite);

    protected abstract String getDirectoryName(File directory, String packageName, TemplateSuite suite);
}
