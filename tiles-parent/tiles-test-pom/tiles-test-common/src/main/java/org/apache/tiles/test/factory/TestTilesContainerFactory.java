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
package org.apache.tiles.test.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.extras.complete.CompleteAutoloadTilesContainerFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.render.BasicRendererFactory;
import org.apache.tiles.test.renderer.ReverseStringRenderer;


/**
 * Test Tiles container factory to customize Tiles behaviour.
 *
 * @version $Rev$ $Date$
 */
public class TestTilesContainerFactory extends CompleteAutoloadTilesContainerFactory {

    /** {@inheritDoc} */
    @Override
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        super.registerAttributeRenderers(rendererFactory, applicationContext, container,
                attributeEvaluatorFactory);
        ReverseStringRenderer renderer = new ReverseStringRenderer();
        rendererFactory.registerRenderer("reversed", renderer);
    }

    /** {@inheritDoc} */
    @Override
    protected List<ApplicationResource> getSources(ApplicationContext applicationContext) {
        List<ApplicationResource> urls = new ArrayList<ApplicationResource>();
        urls.addAll(applicationContext
                .getResources("/WEB-INF/**/tiles-defs*.xml"));
        urls.add(applicationContext.getResource(
                "classpath:/org/apache/tiles/classpath-defs.xml"));
        urls.add(applicationContext.getResource(
                "classpath:/org/apache/tiles/freemarker-classpath-defs.xml"));
        urls.add(applicationContext.getResource(
            "classpath:/org/apache/tiles/velocity-classpath-defs.xml"));
        return urls;
    }
}
