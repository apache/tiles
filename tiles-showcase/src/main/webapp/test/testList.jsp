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

<strong>Example of list usage</strong>
<%-- Insert a menu component.
  Menu component require two lists : one of items, and one of links.
--%>
<tiles:insertTemplate template="menu.jsp" >
  <tiles:put name="title" value="Test menu" />
  <tiles:putList name="items">
    <tiles:add value="home" />
    <tiles:add>
	  <img src="<%=request.getContextPath()%>/images/struts-power.gif"
	       align="right" border="0"></tiles:add>
    <tiles:add value="documentation"/>
  </tiles:putList>
  <tiles:putList name="links">
    <tiles:add value="/index.jsp"/>
    <tiles:add value="/../struts-documentation"/>
    <tiles:add value="/../comps-doc" type="string" />
  </tiles:putList>
</tiles:insertTemplate>
