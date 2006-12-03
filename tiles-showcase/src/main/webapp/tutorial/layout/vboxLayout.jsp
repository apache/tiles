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
  Render a list on severals columns
  parameters : componentsList 
--%>

<tiles:useAttribute id="list" name="componentsList" classname="java.util.List" />

<%-- Normally, we should use the iterate tag to iterate over the list.
  Unfortunatelly, this doesn't work with jsp1.1, because actual iterate tag use a BodyContent
  and include do a flush(), which is illegal inside a BodyContent.
  Jsp1.3 propose a new tag mechanism for iteration, which not use BodyContent.
  Wait until jsp1.3 
  
<logic:iterate id="comp" name="list" type="String" >
  <tiles:insertAttribute name="<%=comp%>" flush="false" />
  <br>
</logic:iterate>  
--%>

<%-- For now, do iteration manually : --%> 
<%
Iterator i=list.iterator();
while( i.hasNext() )
  {
  String comp=(String)i.next();
%>
<tiles:insertAttribute name="<%=comp%>" flush="true" />
<br>

<%
  } // end loop
%>

