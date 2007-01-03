/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.tiles.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesException;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.ComponentDefinition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;

public class KeyedDefinitionsFactoryTilesContainer extends BasicTilesContainer {
    
    public static final String DEFINITIONS_FACTORY_KEY_ATTRIBUTE_NAME =
        "org.apache.tiles.DEFINITIONS_FACTORY.key";
	
	protected Map<String, DefinitionsFactory> key2definitionsFactory;

	public KeyedDefinitionsFactoryTilesContainer() {
		key2definitionsFactory = new HashMap<String, DefinitionsFactory>();
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
     * Standard Getter
     *
     * @return the definitions factory used by this container.
     */
    public DefinitionsFactory getProperDefinitionsFactory(String key) {
        DefinitionsFactory retValue = null;
        
        if (key != null) {
            retValue = key2definitionsFactory.get(key);
        }
        
        return retValue;
    }

    /**
     * Set the definitions factory. This method first ensures
     * that the container has not yet been initialized.
     *
     * @param definitionsFactory the definitions factory for this instance.
     * @throws TilesException If something goes wrong during initialization of
     * the definitions factory.
     */
    public void setDefinitionsFactory(String key,
    		DefinitionsFactory definitionsFactory,
    		Map<String, String> initParameters) throws TilesException {
    	if (key != null) {
	        key2definitionsFactory.put(key, definitionsFactory);
            initializeDefinitionsFactory(definitionsFactory,
                    getResourceString(initParameters), initParameters);
    	} else {
    		setDefinitionsFactory(definitionsFactory);
    	}
    }

    @Override
    protected ComponentDefinition getDefinition(String definitionName,
            TilesRequestContext request) throws DefinitionsFactoryException {
        ComponentDefinition retValue = null;
        String key = getDefinitionsFactoryKey(request);
        if (key != null) {
            DefinitionsFactory definitionsFactory =
                key2definitionsFactory.get(key);
            if (definitionsFactory != null) {
                retValue = definitionsFactory.getDefinition(definitionName,
                        request);
            }
        }
        if (retValue == null) {
            retValue = super.getDefinition(definitionName, request);
        }
        return retValue;
    }
    
    protected String getDefinitionsFactoryKey(TilesRequestContext request) {
        String retValue = null;
        Map requestScope = request.getRequestScope();
        if (requestScope != null) { // Probably the request scope does not exist
            retValue = (String) requestScope.get(
                    DEFINITIONS_FACTORY_KEY_ATTRIBUTE_NAME);
        }
        
        return retValue;
    }
}
