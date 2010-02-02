package org.apache.tiles.autotag.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateMethod;
import org.apache.tiles.autotag.model.TemplateParameter;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.tiles.autotag.model.TemplateSuiteFactory;
import org.apache.tiles.request.Request;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;

public class QDoxTemplateSuiteFactory implements TemplateSuiteFactory {

    private JavaDocBuilder builder;

    private String suiteName;

    private String suiteDocumentation;

    public QDoxTemplateSuiteFactory(File...sourceFiles) {
        builder = new JavaDocBuilder();
        try {
            for (File file : sourceFiles) {
                builder.addSource(file);
            }
        } catch (IOException e) {
            throw new ClassParseException("I/O Exception when adding source files", e);
        }
    }

    public QDoxTemplateSuiteFactory(URL... urls) {
        builder = new JavaDocBuilder();
        try {
            for (URL url: urls) {
                builder.addSource(url);
            }
        } catch (IOException e) {
            throw new ClassParseException("I/O Exception when adding source files", e);
        }
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public void setSuiteDocumentation(String suiteDocumentation) {
        this.suiteDocumentation = suiteDocumentation;
    }

    @Override
    public TemplateSuite createTemplateSuite() {
        List<TemplateClass> classes = new ArrayList<TemplateClass>();
        for (JavaClass clazz : builder.getClasses()) {
            TemplateMethod startMethod = null;
            TemplateMethod endMethod = null;
            TemplateMethod executeMethod = null;
            for (JavaMethod method : clazz.getMethods()) {
                if (isFeasible(method)) {
                    if ("start".equals(method.getName())) {
                        TemplateMethod templateMethod = createMethod(method);
                        startMethod = templateMethod;
                    } else if ("end".equals(method.getName())) {
                        endMethod = createMethod(method);
                    } else if ("execute".equals(method.getName())) {
                        executeMethod = createMethod(method);
                    }
                }
            }
            if ((startMethod != null && endMethod != null)
                    || executeMethod != null) {
                TemplateClass templateClass = new TemplateClass(
                        clazz.getFullyQualifiedName(), startMethod, endMethod, executeMethod);
                templateClass.setDocumentation(clazz.getComment());
                classes.add(templateClass);
            }
        }
        return new TemplateSuite(suiteName, suiteDocumentation, classes);
    }

    private TemplateMethod createMethod(JavaMethod method) {
        List<TemplateParameter> params = new ArrayList<TemplateParameter>();
        for (JavaParameter parameter : method.getParameters()) {
            TemplateParameter templateParameter = new TemplateParameter(
                    parameter.getName(), parameter.getType()
                            .getFullyQualifiedName(), false, false);
            params.add(templateParameter);
        }
        TemplateMethod templateMethod = new TemplateMethod(method.getName(),
                params);
        templateMethod.setDocumentation(method.getComment());
        DocletTag[] tags = method.getTagsByName("param");
        for (DocletTag tag : tags) {
            String[] tagParams = tag.getParameters();
            if (tagParams.length > 0) {
                TemplateParameter templateParameter = templateMethod
                        .getParameterByName(tagParams[0]);
                if (templateParameter != null) {
                    String tagValue = tag.getValue();
                    int pos = tagValue.indexOf(" ");
                    templateParameter.setDocumentation(tagValue.substring(pos)
                            .trim());
                }
            }
        }
        return templateMethod;
    }

    private boolean isFeasible(JavaMethod method) {
        Type returns = method.getReturns();
        if (returns != null && "void".equals(returns.getFullyQualifiedName())
                && method.isPublic() && !method.isStatic()
                && !method.isAbstract() && !method.isConstructor()) {
            JavaParameter[] params = method.getParameters();
            if (params.length > 0) {
                JavaParameter param = params[params.length - 1];
                if (Request.class.getName().equals(
                        param.getType().getFullyQualifiedName())) {
                    return true;
                }
            }
        }
        return false;
    }
}