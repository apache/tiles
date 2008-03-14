<%@ page session="false" %>
<%--
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
--%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:destroyContainer/>
<tiles:initContainer containerFactory="org.apache.tiles.factory.TilesContainerFactory">
    <tiles:putAttribute name="definitions-config"
               value="/WEB-INF/tiles-defs.xml,/org/apache/tiles/classpath-defs.xml,/WEB-INF/tiles-defs-1.1.xml"/>
    <tiles:putAttribute name="org.apache.tiles.context.TilesContextFactory"
               value="org.apache.tiles.context.enhanced.EnhancedContextFactory"/>
    <tiles:putAttribute name="org.apache.tiles.factory.TilesContainerFactory.MUTABLE"
               value="true"/>
    <tiles:putAttribute name="org.apache.tiles.definition.DefinitionsReader"
               value="org.apache.tiles.compat.definition.digester.CompatibilityDigesterDefinitionsReader"/>
    <tiles:putAttribute name="org.apache.tiles.renderer.impl.BasicRendereFactory.TYPE_RENDERERS"
               value="reversed,org.apache.tiles.test.renderer.ReverseStringAttributeRenderer"/>
    <tiles:putAttribute name="org.apache.tiles.evaluator.AttributeEvaluator"
               value="org.apache.tiles.evaluator.el.ELAttributeEvaluator"/>
</tiles:initContainer>

<tiles:insertDefinition name="test.definition" />
