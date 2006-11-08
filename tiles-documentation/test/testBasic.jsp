<%--
/*
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

<%-- Test tags basic behaviors 
--%>
<hr>
<strong>Basic template usage</strong>
<br>
<tiles:insert template="layout.jsp">
  <tiles:put name="title"  value="Test with default no types" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp" />
</tiles:insert>

<hr>
<strong>Specify attribute types</strong>
<br>
<tiles:insert template="layout.jsp">
  <tiles:put name="title"  value="Test with specified types"   type="string" />
  <tiles:put name="header" value="header.jsp" type="page"   />
  <tiles:put name="body"   value="body.jsp"   type="page"   />
</tiles:insert>

<hr>
<strong>Set attribute value with tag body</strong>
<br>
<tiles:insert template="layout.jsp">
  <tiles:put name="title"  value="Test with a tag body" />
  <tiles:put name="header" type="string">
    <strong>This header is inserted as body of tag</strong>
  </tiles:put>
  <tiles:put name="body"   value="body.jsp"/>
</tiles:insert>

<hr>
<strong>Use of definition</strong>
<br>
<tiles:definition id="templateDefinition" template="layout.jsp">
  <tiles:put name="title"  value="Use of definition" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp"   />
</tiles:definition>
<tiles:insert beanName="templateDefinition" />

<hr>
<strong>Use of definition, overload of parameters </strong>Title parameter
from previous definition is overloaded
<br>
<tiles:insert beanName="templateDefinition" >
  <tiles:put name="title"  value="Use of definition, overload of parameters"   type="string" />
</tiles:insert>

<hr>
<strong>Test ignore : body isn't defined </strong>(We use another layout)
<br>
<tiles:insert template="layoutTestIgnore.jsp">
  <tiles:put name="title"  value="Test ignore : body isn't defined" />
  <tiles:put name="header" value="header.jsp" />
</tiles:insert>

 
