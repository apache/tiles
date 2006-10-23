/*
 * $Id$
 *
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tiles.context;

import org.apache.tiles.TilesApplicationContext;

import javax.servlet.ServletContext;
import javax.portlet.PortletContext;

/**
 * Provides static access to previously created
 * Tiles Contexts
 *
 * @version $Id$
 * @since Sep 21, 2006
 */
public class TilesContextAccess {

    private static final String APP_CONTEXT_PARAM_KEY =
        "org.apache.tiles.APPLICATION_CONTEXT";

    /** Retreive the TilesApplicationContext for the given Context */
    public static TilesApplicationContext getApplicationContext(ServletContext context) {
        return (TilesApplicationContext)context.getAttribute(APP_CONTEXT_PARAM_KEY);
    }


    /** Register the TilesApplicationContext for the given Context */
    static void registerApplicationContext(ServletContext context,
                                           TilesApplicationContext tilesContext) {
        context.setAttribute(APP_CONTEXT_PARAM_KEY, tilesContext);
    }

    /** Retreive the TilesApplicationContext for the given Context */
    public static TilesApplicationContext getApplicationContext(PortletContext context) {
        return (TilesApplicationContext)context.getAttribute(APP_CONTEXT_PARAM_KEY);
    }

    /** Register the TilesApplicationContext for the given Context */
    static void registerApplicationContext(PortletContext context,
                                           TilesApplicationContext tilesContext) {
        context.setAttribute(APP_CONTEXT_PARAM_KEY, tilesContext);
    }
}
