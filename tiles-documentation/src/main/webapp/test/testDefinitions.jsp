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

<%-- Test template definitions (from factory) 
--%>
<hr>
<strong>Insert definition defined directly in jsp page</strong>
<tiles:definition id="definition" template="/test/layout.jsp" >
  <tiles:put name="title"  value="Test definition defined in jsp page" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp" />
</tiles:definition>

<br>
<tiles:insert beanName="definition"/>

<hr>
<strong>Insert definition defined in factory</strong>
<br>
<tiles:insert definition="test.layout.test1"/>

<hr>
<strong>Insert definition defined in factory</strong>
<br>
<tiles:insert definition="test.layout.test2"/>

<hr>
<strong>Insert definition defined in factory</strong>
<br>
<tiles:insert definition="test.layout.test3"/>

<hr>
<strong>Insert definition defined in factory : Overload title attribute</strong>
<br>
<tiles:insert definition="test.layout.test3">
  <tiles:put name="title" value="Test definition : overload attribute 'title'" />
</tiles:insert>
