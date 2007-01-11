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
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%-- Layout component 
  Render a list of tiles in a vertical column
  @param : list List of names to insert 
  This file contains 3 possible implementations.
  Use only one and comment the others. Don't forget to move the appropriate taglib
  declaration in the header.
--%>

<tiles:useAttribute id="list" name="list" classname="java.util.List" />

<%-- Iterate over names.
  We don't use <iterate> tag because it doesn't allow insert (in JSP1.1)
 --%>
<%
Iterator i=list.iterator();
while( i.hasNext() )
  {
  String name= (String)i.next();
  if (name.startsWith("/"))
    {
%>
<tiles:insertTemplate template="<%=name%>" flush="true" />
<br>

<%
    }
  else
    {
%>
<tiles:insertDefinition name="<%=name%>" flush="true" />
<br>

<%
    }
  } // end loop
%>


<%-- Iterate over names.
  Use jstl <forEach> tag. 
  Require the jstl taglib.
 --%>
<%-- 
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:forEach var="name" items="${list}">
  <tiles:insertDefinition name="${name}" flush="true" />
<br>
</c:forEach>
 --%>
 
<%-- Iterate over names.
  Use struts <iterate> tag. Work on jsp1.2 and greater web container.
 --%>
<%-- 
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<logic:iterate id="name" name="list" type="java.lang.String">
  <tiles:insertDefinition name="${name}" flush="false" />
<br>
</logic:iterate>
 --%>
