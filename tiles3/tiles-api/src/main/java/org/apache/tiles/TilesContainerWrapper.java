package org.apache.tiles;

import java.io.IOException;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;

public class TilesContainerWrapper implements TilesContainer {

    protected TilesContainer container;

    public TilesContainerWrapper(TilesContainer container) {
        this.container = container;
        if (container == null) {
            throw new NullPointerException("The wrapped container must be not null");
        }
    }

    public TilesContainer getWrappedContainer() {
        return container;
    }

    @Override
    public void endContext(Request request) {
        container.endContext(request);
    }

    @Override
    public Object evaluate(Attribute attribute, Request request) {
        return container.evaluate(attribute, request);
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return container.getApplicationContext();
    }

    @Override
    public AttributeContext getAttributeContext(Request request) {
        return container.getAttributeContext(request);
    }

    @Override
    public Definition getDefinition(String definitionName, Request request) {
        return container.getDefinition(definitionName, request);
    }

    @Override
    public boolean isValidDefinition(String definition, Request request) {
        return container.isValidDefinition(definition, request);
    }

    @Override
    public void prepare(String preparer, Request request) {
        container.prepare(preparer, request);
    }

    @Override
    public void render(String definition, Request request) {
        container.render(definition, request);
    }

    @Override
    public void render(Definition definition, Request request) {
        container.render(definition, request);
    }

    @Override
    public void render(Attribute attribute, Request request) throws IOException {
        container.render(attribute, request);
    }

    @Override
    public void renderContext(Request request) {
        container.renderContext(request);
    }

    @Override
    public AttributeContext startContext(Request request) {
        return container.startContext(request);
    }
}
