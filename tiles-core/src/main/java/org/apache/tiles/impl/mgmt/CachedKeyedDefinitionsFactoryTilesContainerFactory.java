package org.apache.tiles.impl.mgmt;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesException;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.mgmt.TileDefinition;

public class CachedKeyedDefinitionsFactoryTilesContainerFactory extends
        KeyedDefinitionsFactoryTilesContainer implements MutableTilesContainer {

    private DefinitionManager mgr = new DefinitionManager();
    
    private Map<String, DefinitionManager> key2definitionManager
            = new HashMap<String, DefinitionManager>();

    public void register(TileDefinition definition)
        throws TilesException {
        ComponentDefinition def = new ComponentDefinition(definition);
        mgr.addDefinition(def);
    }

    @Override
    protected ComponentDefinition getDefinition(String definition,
                                                TilesRequestContext context)
        throws DefinitionsFactoryException {
        return mgr.getDefinition(definition, context);
    }

    @Override
    public DefinitionsFactory getDefinitionsFactory() {
        return mgr.getFactory();
    }

    @Override
    public DefinitionsFactory getDefinitionsFactory(String key) {
        DefinitionsFactory factory = key2definitionsFactory.get(key);
        if (factory == null) {
            factory = mgr.getFactory();
        }
        
        return factory;
    }

    @Override
    public void setDefinitionsFactory(DefinitionsFactory definitionsFactory) {
        super.setDefinitionsFactory(definitionsFactory);
        mgr.setFactory(definitionsFactory);
    }

    @Override
    public void setDefinitionsFactory(String key, DefinitionsFactory definitionsFactory, Map<String, String> initParameters) {
        DefinitionManager mgr = key2definitionManager.get(key);
        if (mgr == null) {
            mgr = new DefinitionManager();
            key2definitionManager.put(key, mgr);
        }
        mgr.setFactory(definitionsFactory);
    }

}
