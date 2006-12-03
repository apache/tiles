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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="java.util.Iterator" %>

<%-- 
--%>

<%-- Push component attributes in page context --%>
<tiles:importAttribute />

<%-- Prepare the links list to be iterated --%>
<bean:define id="links" name="links" type="java.util.List" scope="page" />
<% Iterator i = links.iterator(); %>

<%-- Iterate on list of items
    --%>
<table>
<logic:iterate id="item" name="items" >

<tr>
  <td width="10" valign="top" ></td>
  <td valign="top"  >
	  <% // Compute link value
	    String link = i.next().toString();
	    if(link.startsWith("/") )
		  link = request.getContextPath() + link;
	  %>
	  <font size="-1"><a href="<%=link%>"><bean:write name="item" filter="false"/></a></font>
  </td>
</tr>
</logic:iterate>
</table>
