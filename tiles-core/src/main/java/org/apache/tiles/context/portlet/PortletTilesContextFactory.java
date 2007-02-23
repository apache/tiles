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

package org.apache.tiles.context.portlet;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.portlet.PortletTilesApplicationContext;
import org.apache.tiles.context.portlet.PortletTilesRequestContext;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.util.Map;

/**
 * Creates an instance of the appropriate TilesApplicationContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class PortletTilesContextFactory implements TilesContextFactory {

    public void init(Map configParameters) {
    }

    /**
     * Creates a TilesApplicationContext from the given context.
     */
    public TilesApplicationContext createApplicationContext(Object context) {
        if (context instanceof PortletContext) {
            PortletContext portletContext = (PortletContext) context;
            return new PortletTilesApplicationContext(portletContext);
        }
        
        return null;
    }

    public TilesRequestContext createRequestContext(TilesApplicationContext context,
                                                    Object... requestItems) {
        if (requestItems.length == 2) {
            PortletContext portletContext = getPortletContext(context);
            if (portletContext != null) {
                PortletTilesApplicationContext app = (PortletTilesApplicationContext) context;
                return new PortletTilesRequestContext(app.getPortletContext(),
                    (PortletRequest) requestItems[0],
                    (PortletResponse) requestItems[1]);
            }
        }
        
        return null;
    }

    protected PortletContext getPortletContext(TilesApplicationContext context) {
        if (context instanceof PortletTilesApplicationContext) {
            PortletTilesApplicationContext app = (PortletTilesApplicationContext) context;
            return app.getPortletContext();
        }
        return null;
    }
}
