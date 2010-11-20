package org.apache.tiles.autotag.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.app.VelocityEngine;

import com.thoughtworks.xstream.XStream;

public abstract class AbstractGenerateMojo extends AbstractMojo {
    static final String META_INF_TEMPLATE_SUITE_XML = "META-INF/template-suite.xml";

    /**
     * The project
     *
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    List<String> classpathElements;

    public void execute() throws MojoExecutionException {
        try {
            InputStream stream = findTemplateSuiteDescriptor();
            XStream xstream = new XStream();
            TemplateSuite suite = (TemplateSuite) xstream.fromXML(stream);
            stream.close();

            Properties props = new Properties();
            InputStream propsStream = getClass().getResourceAsStream("/org/apache/tiles/autotag/velocity.properties");
            props.load(propsStream);
            propsStream.close();

            generate(suite, new VelocityEngine(props));
        } catch (IOException e) {
            throw new MojoExecutionException("error", e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("error", e);
        }
    }

    protected abstract void generate(TemplateSuite suite, VelocityEngine velocityEngine);

    private InputStream findTemplateSuiteDescriptor() throws IOException {
        InputStream retValue = null;

        for (String path: classpathElements) {
            File file = new File(path);
            if (file.isDirectory()) {
                File candidate = new File(file, META_INF_TEMPLATE_SUITE_XML);
                if (candidate.exists()) {
                    return new FileInputStream(candidate);
                }
            } else if (file.getPath().endsWith(".jar")) {
                JarFile jar = new JarFile(file);
                ZipEntry entry = jar.getEntry(META_INF_TEMPLATE_SUITE_XML);
                if (entry != null) {
                    return jar.getInputStream(entry);
                }
            }
        }

        return retValue;
    }

}
