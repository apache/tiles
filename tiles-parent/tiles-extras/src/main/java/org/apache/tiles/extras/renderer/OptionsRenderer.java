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
package org.apache.tiles.extras.renderer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tiles.Attribute;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a custom "options" syntax for attributes.
 * The first option that can be rendered is.
 * Comes from http://tech.finn.no/the-ultimate-view/<p/>
 *
 * Actual rendering is delegated to the TypeDetectingRenderer that's supplied in the constructor.
 *
 * For example:
 * "/WEB-INF/tiles/fragments/${options[myoptions]}/content.jsp"
 * given the myptions list-attribute is defined like:
 * <pre>
        <put-list-attribute name="myoptions">
            <add-list-attribute>
                <add-attribute value="car"/>
                <add-attribute value="vechile"/>
                <add-attribute value="advert"/>
            </add-list-attribute>
        </put-list-attribute>
   </pre>
 * will look for content.jsp
 * first in "/WEB-INF/tiles/fragments/car/" then
 * second in "/WEB-INF/tiles/fragments/vechile/" and
 * last in "/WEB-INF/tiles/fragments/advert".
 * <p/>
 * <p/>
 * Currently only supports one occurrance of such an "option" pattern in the attribute's value.
 *
 */
public final class OptionsRenderer implements Renderer {

    private static final Pattern OPTIONS_PATTERN
            = Pattern.compile(Pattern.quote("{options[") + "(.+)" + Pattern.quote("]}"));

    private static final Logger LOG = LoggerFactory.getLogger(OptionsRenderer.class);

    private final ApplicationContext applicationContext;
    private final Renderer renderer;

    public OptionsRenderer(final ApplicationContext applicationContext, final Renderer renderer){
        this.applicationContext = applicationContext;
        this.renderer = renderer;
    }

    @Override
    public boolean isRenderable(final String path, final Request request) {
        return renderer.isRenderable(path, request);
    }

    @Override
    public void render(final String path, final Request request) throws IOException {

        Matcher matcher =  OPTIONS_PATTERN.matcher((String) path);

        if (null != matcher && matcher.find()) {
            boolean done = false;
            String match = matcher.group(1);
            ListAttribute fallbacks = (ListAttribute) TilesAccess
                    .getCurrentContainer(request)
                    .getAttributeContext(request)
                    .getAttribute(match);

            if(null == fallbacks){
                throw new IllegalStateException("A matching list-attribute name=\"" + match + "\" must be defined.");
            }else if(fallbacks.getValue().isEmpty()){
                throw new IllegalStateException("list-attribute name=\"" + match + "\" must have minimum one attribute");
            }

            for (Attribute option : (List<Attribute>) fallbacks.getValue()) {
                String template = path.replaceFirst(Pattern.quote(matcher.group()), (String)option.getValue());
                done = renderAttempt(template, request);
                if(done){ break; }
            }
            if (!done) {
              throw new IOException("None of the options existed for " + path);
            }
        } else {
            renderer.render(path, request);
        }
    }

    private boolean renderAttempt(final String template, final Request request) throws IOException{
        boolean result = false;
        if(!Cache.isTemplateMissing(template)){
            try {
                if (null != applicationContext.getResource(template)) { // can throw FileNotFoundException !
                    renderer.render(template, request); // can throw FileNotFoundException !
                    result = true;
                    Cache.setIfAbsentTemplateFound(template, true);
                }
            } catch (FileNotFoundException ex) {
                if(ex.getMessage().contains(template)){
                    // expected outcome. continue loop.
                    LOG.trace(ex.getMessage());
                }else{
                    // comes from an inner templateAttribute.render(..) so throw on
                    throw ex;
                }
            } catch(IOException ex){ //xxx ???
                throw ex;
            }
            Cache.setIfAbsentTemplateFound(template, false);
        }
        return result;
    }

    private static final class Cache{

        /** It takes CACHE_LIFE milliseconds for any hot deployments to register.
         */
        private static final ConcurrentMap<String,Boolean> TEMPLATE_EXISTS
                = new ConcurrentHashMap<String,Boolean>();

        private volatile static long cacheLastCleaned = System.currentTimeMillis();

        private static final String CACHE_LIFE_PROPERTY = Cache.class.getName() + ".ttl_ms";

        /** Cache TTL be customised by a system property like
         *  -Dorg.apache.tiles.extras.renderer.OptionsRenderer.Cache.ttl_ms=0
         *
         * The default is 5 minutes.
         * Setting it to zero disables all caching.
         */
        private static final long CACHE_LIFE = null != Long.getLong(CACHE_LIFE_PROPERTY)
                ? Long.getLong(CACHE_LIFE_PROPERTY)
                : 1000 * 60 * 5;

        static boolean isTemplateMissing(final String template){
            if(0 < CACHE_LIFE && System.currentTimeMillis() > cacheLastCleaned + CACHE_LIFE){
                cacheLastCleaned = System.currentTimeMillis();
                TEMPLATE_EXISTS.clear();
                return false;
            }else{
                return TEMPLATE_EXISTS.containsKey(template) && !TEMPLATE_EXISTS.get(template);
            }
        }

        static void setIfAbsentTemplateFound(final String template, final boolean found){
            if(0 < CACHE_LIFE && !TEMPLATE_EXISTS.containsKey(template)){
                TEMPLATE_EXISTS.putIfAbsent(template, found);
            }
        }

        private Cache(){}
    }
}
