package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PutListAttributeModel extends PutAttributeModel {

    /**
     * If true, the attribute will put the elements of the attribute with the
     * same name of the parent definition before the ones specified here. By
     * default, it is 'false'
     *
     * @return The "inherit" value.
     * @since 2.1.0
     */
    public boolean getInherit() {
        return FreeMarkerUtil.getAsBoolean(currentParams.get("inherit"), false);
    }

    /**
     * Get list defined in tag.
     *
     * @return The value of this list attribute.
     */
    @SuppressWarnings("unchecked")
    public List<Attribute> getAttributes() {
        return (List<Attribute>) super.getValue();
    }

    @Override
    protected void doStart(Environment env, Map<String, TemplateModel> params,
            TemplateModel[] loopVars, TemplateDirectiveBody body) {
        super.doStart(env, params, loopVars, body);
        value = new ArrayList<Attribute>();
    }

    @Override
    protected void evaluateBody(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        if (body != null) {
            Writer writer = new NullWriter();
            try {
                body.render(writer);
            } catch (TemplateException e) {
                throw new FreeMarkerTilesException(
                        "Exception during rendition of the body", e);
            } catch (IOException e) {
                throw new FreeMarkerTilesException(
                        "I/O Exception during rendition of the body", e);
            }
        }
    }

    /**
     * Process nested &lg;putAttribute&gt; tag.
     * <p/>
     * Places the value of the nested tag within the
     * {@link org.apache.tiles.AttributeContext}.It is the responsibility
     * of the descendent to check security.  Security will be managed by called
     * tags.
     *
     * @param nestedTag the put tag desciendent.
     */
    public void processNestedModel(AddAttributeModel nestedTag) {
        Attribute attribute = new Attribute(nestedTag.getValue(), null,
                nestedTag.getRole(), nestedTag.getType());

        this.addValue(attribute);
    }

    /**
     * Adds an attribute value to the list.
     *
     * @param attribute The attribute to add.
     */
    private void addValue(Attribute attribute) {
        this.getAttributes().add(attribute);
    }

    /** {@inheritDoc} */
    @Override
    protected void execute(Environment env) {
        PutListAttributeModelParent parent = (PutListAttributeModelParent) findAncestorWithClass(
                env, PutListAttributeModelParent.class);

        if (parent == null) {
            // Try with the old method.
            super.execute(env);
        }

        parent.processNestedModel(this);
    }

    private static class NullWriter extends Writer {

        @Override
        public void close() throws IOException {
            // Does nothing
        }

        @Override
        public void flush() throws IOException {
            // Does nothing
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            // Does nothing
        }
        
    }
}
