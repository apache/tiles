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
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%-- Test tiles Controller call 
--%>
<hr>
<strong>Test controller set in definition from config file</strong>
<br>
<tiles:insertDefinition name="test.struts.controller" >
</tiles:insertDefinition>

<hr>
<strong>Test controller set in insert</strong>
<br>
<tiles:insertTemplate template="layout.jsp" 
           controllerClass="org.apache.struts.webapp.tiles.test.TestTileController" >
  <tiles:put name="title"  value="Test controller set in insert" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp" />
</tiles:insertTemplate>

<hr>
<strong>Test controller set in insert, and attribute from definition</strong>
<br>
<tiles:insertDefinition name="test.layout.test1" 
           controllerClass="org.apache.struts.webapp.tiles.test.TestTileController" >
  <tiles:put name="title"  value="Test controller set in insert, and attribute from definition" />
</tiles:insertDefinition>



