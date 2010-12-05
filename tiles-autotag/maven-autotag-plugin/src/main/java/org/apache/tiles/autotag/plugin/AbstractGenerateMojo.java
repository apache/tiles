package org.apache.tiles.autotag.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.tiles.autotag.generate.TemplateGenerator;
import org.apache.tiles.autotag.generate.TemplateGeneratorFactory;
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

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/autotag-jsp-classes"
     * @required
     */
    File classesOutputDirectory;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/autotag-jsp-resources"
     * @required
     */
    File resourcesOutputDirectory;

    /**
     * Name of the package.
     * @parameter expression="sample"
     * @required
     */
    String packageName;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

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
            TemplateGenerator generator = createTemplateGeneratorFactory(
                    new VelocityEngine(props)).createTemplateGenerator();
            generator.generate(packageName, suite, getParameters());
            if (generator.isGeneratingResources()) {
                Resource resource = new Resource();
                resource.setDirectory(resourcesOutputDirectory.getAbsolutePath());
                project.addResource(resource);
            }
            if (generator.isGeneratingClasses()) {
                project.addCompileSourceRoot(classesOutputDirectory.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new MojoExecutionException("error", e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("error", e);
        }
    }

    protected abstract TemplateGeneratorFactory createTemplateGeneratorFactory(VelocityEngine velocityEngine);

    protected abstract Map<String, String> getParameters();

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
