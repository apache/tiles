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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test jsp include in a BodyTag (<iterate>).
--%>
<%
java.util.List list = new java.util.ArrayList();
list.add( "header.jsp" );
list.add( "body.jsp" );
int position=0;
%>

 <hr>
 
 <strong>1 request.getRequestDispatcher(uri).include(request, response)</strong>

 <logic:iterate id="uri" collection="<%=list%>" type="java.lang.String" >
  <br>
  include number <%= position++ %>
  <br>
   <% // insert the id
   response.flushBuffer();
   response.getWriter().flush();
   //out.flush();
   request.getRequestDispatcher(uri).include(request, response);
   response.getWriter().flush();
   response.flushBuffer();
   %>
 </logic:iterate>
 
<hr>
<strong>pageContext.include(page)</strong>

 <logic:iterate id="uri" collection="<%=list%>" type="java.lang.String" >
  <br>
  include number <%= position++ %>
  <br>
   <% // insert the id
   pageContext.include(uri);
   %>
 </logic:iterate><hr>
 
<hr>
<strong>tiles:insert</strong>

 <logic:iterate id="uri" collection="<%=list%>" type="java.lang.String" >
  <br>
  include number <%= position++ %>
  <br>
   <tiles:insert definition="test.layout.test1" flush="false"/>
 </logic:iterate>

<strong>Done</strong>

 
