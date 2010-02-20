package org.apache.tiles.freemarker;

import java.io.IOException;
import java.io.Writer;

import org.apache.tiles.template.body.AbstractModelBody;

import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

public class FreemarkerModelBody extends AbstractModelBody {

    private TemplateDirectiveBody templateDirectiveBody;

    public FreemarkerModelBody(Writer defaultWriter, TemplateDirectiveBody templateDirectiveBody) {
        super(defaultWriter);
        this.templateDirectiveBody = templateDirectiveBody;
    }

    @Override
    public void evaluate(Writer writer) throws IOException {
        if (templateDirectiveBody == null) {
            return;
        }

        try {
            templateDirectiveBody.render(writer);
        } catch (TemplateException e) {
            throw new IOException("TemplateException when rendering body", e);
        }
    }

}
