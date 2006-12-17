package org.apache.tiles.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesException;
import org.apache.tiles.definition.DefinitionsFactory;

public class KeyedDefinitionsFactoryTilesContainer extends BasicTilesContainer {
	
	protected Map<String, DefinitionsFactory> key2definitionsFactory;
	
	protected Map<String, Map<String, String>> key2initParams;

	public KeyedDefinitionsFactoryTilesContainer() {
		key2definitionsFactory = new HashMap<String, DefinitionsFactory>();
		key2initParams = new HashMap<String, Map<String,String>>();
	}

    @Override
	public void init(Map<String, String> initParameters) throws TilesException {
		super.init(initParameters);
		
		for (Map.Entry<String, DefinitionsFactory> entry:
				key2definitionsFactory.entrySet()) {
			String key = entry.getKey();
			initializeDefinitionsFactory(entry.getValue(),
					getResourceString(initParameters), key2initParams.get(key));
		}
	}

	/**
     * Standard Getter
     *
     * @return the definitions factory used by this container.
     */
    public DefinitionsFactory getDefinitionsFactory(String key) {
    	DefinitionsFactory retValue = null;
    	
    	if (key != null) {
    		retValue = key2definitionsFactory.get(key);
    	}
    	if (retValue == null) {
    		retValue = getDefinitionsFactory();
    	}
    	
    	return retValue;
    }

    /**
     * Set the definitions factory. This method first ensures
     * that the container has not yet been initialized.
     *
     * @param definitionsFactory the definitions factory for this instance.
     */
    public void setDefinitionsFactory(String key,
    		DefinitionsFactory definitionsFactory,
    		Map<String, String> initParameters) {
    	if (key != null) {
	        checkInit();
	        key2definitionsFactory.put(key, definitionsFactory);
	        key2initParams.put(key, initParameters);
    	} else {
    		setDefinitionsFactory(definitionsFactory);
    	}
    }
}
