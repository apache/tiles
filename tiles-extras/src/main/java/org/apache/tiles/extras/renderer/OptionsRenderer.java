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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
 * Comes from <a href="http://tech.finn.no/the-ultimate-view/">The Ultimate View</a> article.<p/>
 *
 * Actual rendering is delegated to the TypeDetectingRenderer that's supplied in the constructor.<p/>
 *
 * For example:
 * "/WEB-INF/tiles/fragments/${options[myoptions]}/content.jsp"
 * given the myptions list-attribute is defined like:
 * <pre>
        &lt;put-list-attribute name="myoptions">
            &lt;add-list-attribute>
                &lt;add-attribute value="car"/>
                &lt;add-attribute value="vechile"/>
                &lt;add-attribute value="advert"/>
            &lt;/add-list-attribute>
        &lt;/put-list-attribute>
   </pre>
 * will look for content.jsp <br/>
 * first in "/WEB-INF/tiles/fragments/car/" then <br/>
 * second in "/WEB-INF/tiles/fragments/vechile/" and <br/>
 * last in "/WEB-INF/tiles/fragments/advert".
 * <p/>
 * <p/>
 * Currently only supports one occurrance of such an "option" pattern in the attribute's value.
 * <p/>
 * Limitation: "looking" for templates is implemented using applicationContext.getResource(..)
 * therefore the option values in the options list need to be visible as applicationResources.
 * <p/>
 * The attribute found and rendered is cached so to improve performance on subsequent lookups.
 * The default cache time-to-live is {@value #DEFAULT_CACHE_LIFE}, specified by {@link #DEFAULT_CACHE_LIFE}.
 * It can be customised by setting the system property {@value #CACHE_LIFE_PROPERTY}, see {@link #CACHE_LIFE_PROPERTY}.
 * Setting it to zero will disable the cache.
 */
public final class OptionsRenderer implements Renderer {

    public static final String CACHE_LIFE_PROPERTY = OptionsRenderer.class.getName() + ".cache_ttl_ms";

    public static final long DEFAULT_CACHE_LIFE = 1000 * 60 * 5;

    public static final Pattern OPTIONS_PATTERN
            = Pattern.compile(Pattern.quote("{options[") + "(.+)" + Pattern.quote("]}"));

    private static final Logger LOG = LoggerFactory.getLogger(OptionsRenderer.class);

    private final ApplicationContext applicationContext;
    private final Renderer renderer;

    public OptionsRenderer(final ApplicationContext applicationContext, final Renderer renderer) {
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

            if (null == fallbacks) {
                throw new IllegalStateException("A matching list-attribute name=\"" + match + "\" must be defined.");
            } else if (fallbacks.getValue().isEmpty()) {
                throw new IllegalStateException(
                        "list-attribute name=\"" + match + "\" must have minimum one attribute");
            }

            for (Attribute option : (List<Attribute>) fallbacks.getValue()) {
                String template = path.replaceFirst(Pattern.quote(matcher.group()), (String) option.getValue());
                done = renderAttempt(template, request);
                if (done) { break; }
            }
            if (!done) {
                throw new IOException("None of the options existed for " + path);
            }
        } else {
            renderer.render(path, request);
        }
    }

    private boolean renderAttempt(final String template, final Request request) throws IOException {
        boolean result = false;
        if (Cache.attemptTemplate(template)) {
            try {
                if (null != applicationContext.getResource(template)) {
                    renderer.render(template, request);
                    result = true;
                }
            } catch (IOException ex) {
                if (ex.getMessage().contains(template)) {
                    // expected outcome. continue loop.
                    LOG.trace(ex.getMessage());
                } else {
                    // comes from an inner templateAttribute.render(..) so throw on
                    throw ex;
                }
            } catch (RuntimeException ex) {
                if (ex.getMessage().contains(template)) {
                    // expected outcome. continue loop.
                    LOG.trace(ex.getMessage());
                } else {
                    // comes from an inner templateAttribute.render(..) so throw on
                    throw ex;
                }
            }
            Cache.update(template, result);
        }
        return result;
    }

    private static final class Cache {

        private static final long CACHE_LIFE = Long.getLong(CACHE_LIFE_PROPERTY, DEFAULT_CACHE_LIFE);

        /** It takes CACHE_LIFE milliseconds for any hot deployments to register.
         */
        private static final ConcurrentMap<String,Boolean> TEMPLATE_EXISTS;

        static {
            LOG.info("cache_ttl_ms=" + CACHE_LIFE);

            LoadingCache<String,Boolean> builder = CacheBuilder
                    .newBuilder()
                    .expireAfterWrite(CACHE_LIFE, TimeUnit.MILLISECONDS)
                    .build(
                        new CacheLoader<String, Boolean>() {
                            @Override
                            public Boolean load(String key) {
                                throw new UnsupportedOperationException(
                                        "illegal TEMPLATE_EXISTS.get(\"" + key
                                        + "\") before TEMPLATE_EXISTS.containsKey(\"" + key + "\")");
                            }
                        });

            TEMPLATE_EXISTS = builder.asMap();
        }


        static boolean attemptTemplate(final String template) {
            Boolean found = TEMPLATE_EXISTS.get(template);
            return found == null || found;
        }

        static void update(final String template, final boolean found) {
            TEMPLATE_EXISTS.putIfAbsent(template, found);
        }

        private Cache() {}
    }
}
