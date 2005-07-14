/*
 * $Id$ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
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

package org.apache.struts.webapp.tiles.rssChannel;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.digester.rss.Channel;
//import org.apache.commons.digester.rss.RSSDigester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

/**
 * Read and parse RSS files found at on a given
 * list in Tile attribute, save the Channel
 * beans in Tile attribute,and forward to "continue".
 * Tiles input attributes :
 * <ul>
 *   <li>urls : list of channel urls</li>
 *   <li>url : a channel url (if urls not use)</li>
 * </ul>
 * Tiles output attributes :
 * <ul>
 *   <li>channels : List of Digester Channel beans</li>
 * </ul>
 *
 * @version $Rev$ $Date$
 */
public final class RssChannelsAction extends TilesAction {

    /** 
     * Commons Logging instance.
     */
    private static Log log = LogFactory.getLog(RssChannelsAction.class);

    /**
     * Tile attribute key for saving Channel bean
     */
    public static final String CHANNELS_KEY = "CHANNELS";

    /**
     * Tile attribute key for getting Channel urls list
     */
    public static final String CHANNEL_URLS_KEY = "urls";

    /**
     * Tile attribute key for getting Channel url attribute
     */
    public static final String CHANNEL_URL_KEY = "url";

    /**
     * Main process of class. Reads, parses
     * @param context The current Tile context, containing Tile attributes.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request (if any).
     * @param request The HTTP request we are processing.
     * @param response The HTTP response we are creating.
     *
     * @exception Exception if the application business logic throws
     *  an exception
     * @since Struts 1.1
     */
    public ActionForward execute(
        ComponentContext context,
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {

        log.debug("Enter Rss Channel Action");

        ActionMessages errors = new ActionMessages();

        // -- Retrieve parameters --
        // Urls can come from a list, or from a single attribute.

        List channels = (List) context.getAttribute(CHANNEL_URLS_KEY);
        if (channels == null) {
            Object url = context.getAttribute(CHANNEL_URL_KEY);
            channels = new ArrayList(1);
            channels.add(url);
        }

        log.debug("urls count" + channels.size());

        // -- Loop through channels --
        List channelBeans = new ArrayList(channels.size());
//        try {
//            for (int i = 0; i < channels.size(); i++) {
//                RSSDigester digester = new RSSDigester();
//                String url = (String) channels.get(i);
//                // Add application path if needed
//                if (url.startsWith("/")) {
//                    url = toFullUrl(request, url);
//                }
//
//                log.debug("Channel url=" + url);
//
//                Channel obj = (Channel) digester.parse(url);
//
//                log.debug("Channel:" + obj);
//
//                channelBeans.add(obj);
//            }
//            
//        } catch (Throwable t) {
//            errors.add(
//                ActionMessages.GLOBAL_MESSAGE,
//                new ActionMessage("rss.access.error"));
//
//            servlet.log(t.toString());
//        }

        // -- Handle Errors ---
        if (!errors.isEmpty()) {
            this.saveErrors(request, errors);
            // If no input page, use error forwarding

            log.debug("Exit Rss Channel Action : error");

            return null;
        }

        // -- Save Bean, and Continue  ---

        log.debug("Exit Rss Channel Action");

        // Use Tile context to pass channels
        context.putAttribute(CHANNELS_KEY, channelBeans);

        return null;
    }

    /**
     * Compute Full local url from an url starting with "/".
     */
    private String toFullUrl(HttpServletRequest request, String url) {
        StringBuffer buff = new StringBuffer();

        buff.append(request.getScheme()).append("://").append(
            request.getServerName());

        if (request.getServerPort() != 80) {
            buff.append(":").append(request.getServerPort());
        }

        buff.append(request.getContextPath()).append(url);

        return buff.toString();
    }

}
