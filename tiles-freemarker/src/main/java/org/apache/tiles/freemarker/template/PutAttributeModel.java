package org.apache.tiles.freemarker.template;

import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;

public class PutAttributeModel extends AddAttributeModel {

    /**
     * Returns  the name of the attribute.
     *
     * @return The name of the attribute.
     */
    public String getName() {
        return FreeMarkerUtil.getAsString(currentParams.get("name"));
    }

    /**
     * Checks if the attribute should be cascaded to nested definitions.
     *
     * @return <code>true</code> if the attribute will be cascaded.
     * @since 2.1.0
     */
    public boolean isCascade() {
        return FreeMarkerUtil.getAsBoolean(currentParams.get("cascade"), false);
    }

    /** {@inheritDoc} */
    @Override
    protected void execute(Environment env) {
        PutAttributeModelParent parent = (PutAttributeModelParent)
            findAncestorWithClass(env, PutAttributeModelParent.class);

        if (parent == null) {
            throw new FreeMarkerTilesException("Error: cannot find an PutAttributeModelParent ancestor'");
        }

        parent.processNestedModel(this);
    }
}
