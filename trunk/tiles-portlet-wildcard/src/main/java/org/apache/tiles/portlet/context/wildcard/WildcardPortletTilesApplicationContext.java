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
 */

package org.apache.tiles.portlet.context.wildcard;

import javax.portlet.PortletContext;

/**
 * Portlet-based implementation of the TilesApplicationContext interface that
 * can resolve resources even using wildcards.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 * @deprecated Use {@link org.apache.tiles.portlet.wildcard.WildcardPortletTilesApplicationContext}.
 */
public class WildcardPortletTilesApplicationContext extends
        org.apache.tiles.portlet.wildcard.WildcardPortletTilesApplicationContext {

    /**
     * Constructor.
     *
     * @param portletContext The portlet context.
     * @since 2.1.0
     * @deprecated Use
     * {@link org.apache.tiles.portlet.wildcard.WildcardPortletTilesApplicationContext
     * #WildcardPortletTilesApplicationContext(PortletContext)}
     * .
     */
    public WildcardPortletTilesApplicationContext(PortletContext portletContext) {
        super(portletContext);
    }
}
