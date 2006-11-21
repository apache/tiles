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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test 'ignore' attribute : 
  Insert components/templates with undefined attributes, or bad urls.
  Undefined must be spkipped, while false urls must output exception.
--%>
<hr>
<strong>Test ignore : body isn't defined</strong>
<br>
<tiles:insert template="layoutTestIgnore.jsp">
  <tiles:put name="title"  value="Test ignore : body isn't defined" />
  <tiles:put name="header" value="header.jsp" />
</tiles:insert>

<hr>
<strong>Test ignore : bad body definition name (exception must be shown)</strong>
<br>
<tiles:insert template="layoutTestIgnore.jsp">
  <tiles:put name="title"  value="Test ignore : bad body definition name (exception must be shown)" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="badDefinitionName" type="definition" />
</tiles:insert>

<hr>
<strong>Test ignore : Definition not found (no errors, no insertion)</strong>
<br>
<tiles:definition id="templateDefinition" template="layout.jsp">
  <tiles:put name="title"  value="Test ignore : Definition not found (no errors, no insertion)" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp"   />
</tiles:definition>
<tiles:insert beanName="badTemplateDefinitionName" ignore="true"/>

<hr>
<strong>Test ignore : bad body urls (exception must be shown)</strong>
<br>
<tiles:insert template="layoutTestIgnore.jsp">
  <tiles:put name="title"  value="Test ignore : bad body urls (exception must be shown)" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body2.jsp"/>
</tiles:insert>

