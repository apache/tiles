package org.apache.tiles.request;

public abstract class AbstractRequest implements Request{
    /**
     * Name of the attribute used to store the force-include option.
     * @since 2.0.6
     */
    public static final String FORCE_INCLUDE_ATTRIBUTE_NAME =
        "org.apache.tiles.servlet.context.ServletTilesRequestContext.FORCE_INCLUDE";


    protected void setForceInclude(boolean forceInclude) {
        getContext("request").put(FORCE_INCLUDE_ATTRIBUTE_NAME, forceInclude);
    }

    protected boolean isForceInclude() {
        Boolean forceInclude = (Boolean) getContext("request").get(
                FORCE_INCLUDE_ATTRIBUTE_NAME);
        if (forceInclude != null) {
            return forceInclude;
        }
        return false;
    }
}
