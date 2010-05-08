package org.apache.tiles.request.freemarker.extractor;

import java.util.Enumeration;

import org.apache.tiles.request.collection.IteratorEnumeration;
import org.apache.tiles.request.collection.extractor.AttributeExtractor;
import org.apache.tiles.request.freemarker.FreemarkerRequestException;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

public class EnvironmentScopeExtractor implements AttributeExtractor {

    private Environment request;

    public EnvironmentScopeExtractor(Environment request) {
        this.request = request;
    }

    @Override
    public void removeValue(String name) {
        request.setVariable(name, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getKeys() {
        try {
            return new IteratorEnumeration<String>(request
                    .getKnownVariableNames().iterator());
        } catch (TemplateModelException e) {
            throw new FreemarkerRequestException(
                    "Cannot iterate variable names correctly", e);
        }
    }

    @Override
    public Object getValue(String key) {
        try {
            TemplateModel variable = request.getVariable(key);
            if (variable != null) {
                return DeepUnwrap.unwrap(variable);
            }
            return null;
        } catch (TemplateModelException e) {
            throw new FreemarkerRequestException(
                    "Cannot get attribute with name '" + key + "'", e);
        }
    }

    @Override
    public void setValue(String key, Object value) {
        try {
            TemplateModel model = request.getObjectWrapper().wrap(value);
            request.setVariable(key, model);
        } catch (TemplateModelException e) {
            throw new FreemarkerRequestException(
                    "Error when wrapping an object setting the '" + key
                            + "' attribute", e);
        }
    }
}
