package org.apache.tiles.autotag.jsp;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.apache.tiles.autotag.core.AutotagRuntimeException;
import org.apache.tiles.autotag.generate.TemplateSuiteGenerator;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class TLDGenerator implements TemplateSuiteGenerator {

    @Override
    public void generate(File directory, String packageName, TemplateSuite suite) {
        File dir = new File(directory, "META-INF/tld/");
        dir.mkdirs();
        File tldFile = new File(dir, suite.getName() + "-jsp.tld");
        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("suite", suite);
        try {
            tldFile.createNewFile();
            Template template = Velocity.getTemplate("/org/apache/tiles/autotag/jsp/tld.vm");
            Writer writer = new FileWriter(tldFile);
            try {
                template.merge(context, writer);
            } finally {
                writer.close();
            }
        } catch (ResourceNotFoundException e) {
            throw new AutotagRuntimeException("Cannot get tld.vm resource", e);
        } catch (ParseErrorException e) {
            throw new AutotagRuntimeException("The tld.vm resource is not parseable", e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AutotagRuntimeException("Another generic exception while parsing tld.vm", e);
        }
    }

}
