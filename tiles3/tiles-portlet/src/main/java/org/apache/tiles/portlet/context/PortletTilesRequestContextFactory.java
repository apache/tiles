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

package org.apache.tiles.portlet.context;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates an instance of the appropriate {@link Request}
 * implementation under a portlet environment.
 *
 * @version $Rev$ $Date$
 * @since 2.1.1
 */
public class PortletTilesRequestContextFactory implements
        TilesRequestContextFactory {

    /**
     * The logging object.
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The site of the request object array in case there is a request, a
     * response and a portlet context.
     *
     * @since 2.1.4
     */
    private static final int REQUEST_OBJECTS_LENGTH = 3;

    /**
     * Constructor. To see if a portlet context is available, simply accesses a
     * portlet class.
     */
    public PortletTilesRequestContextFactory() {
        try {
            logger.debug("The portlet environment is available, "
                    + "since the class {} is present", PortletRequest.class);
        } catch (NoClassDefFoundError e) {
            throw new NotAPortletEnvironmentException(
                    "Cannot access portlet classes", e);
        }
    }

    /** {@inheritDoc} */
    public Request createRequestContext(ApplicationContext context,
                                                    Object... requestItems) {
        if (requestItems.length == 2
                && requestItems[0] instanceof PortletRequest
                && requestItems[1] instanceof PortletResponse) {
            PortletContext portletContext = getPortletContext(context);
            if (portletContext != null) {
                return new PortletTilesRequestContext(context, portletContext,
                        (PortletRequest) requestItems[0],
                        (PortletResponse) requestItems[1]);
            }
        } else if (requestItems.length == REQUEST_OBJECTS_LENGTH
                && requestItems[0] instanceof PortletRequest
                && requestItems[1] instanceof PortletResponse
                && requestItems[2] instanceof PortletContext) {
            return new PortletTilesRequestContext(context,
                    (PortletContext) requestItems[2],
                    (PortletRequest) requestItems[0],
                    (PortletResponse) requestItems[1]);
        }


        return null;
    }

    /**
     * Returns the original portlet context.
     *
     * @param context The application context.
     * @return The original portlet context, if found.
     */
    protected PortletContext getPortletContext(ApplicationContext context) {
        if (context instanceof PortletTilesApplicationContext) {
            PortletTilesApplicationContext app = (PortletTilesApplicationContext) context;
            return app.getPortletContext();
        }
        return null;
    }
}
