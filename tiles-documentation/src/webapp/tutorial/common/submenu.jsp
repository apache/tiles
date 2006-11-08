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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="java.util.Iterator" %>


<%-- Push component attributes in page context --%>
<tiles:importAttribute />

<table>
<logic:present name="title">
<tr>
  <th colspan=2>
    <div align="left"><strong><bean:write name="title"/></strong></div>
  </th>
</tr>
</logic:present>

<%-- Check if selected exist. --%>
<logic:notPresent name="selected" >
  <% pageContext.setAttribute( "selected", "" ); %>
</logic:notPresent>

<%-- Prepare the links list to be iterated --%>
<bean:define id="links" name="links" type="java.util.List" scope="page" />
<% Iterator i = links.iterator(); %>

<%-- iterate on items list --%>
<%-- Normally, we should write something like this :
   <logic:iterate id="item" name="items" type="java.lang.String" >
   But, Struts doesn't declare the TEI class for iterate, and 
   some web container deal badly with the declared variable. 
   So, we use what follow instead.
    --%>
<logic:iterate id="iterateItem" name="items" >
<bean:define id="item" name="iterateItem" type="java.lang.String" scope="page" />


<tr>
  <td width="10" valign="top" ></td>
  <td valign="top"  >
    <%-- check if selected --%>
	<logic:notEqual name="selected" value="<%=item%>">
	  <% // Compute link value
	    String link = (String)i.next();
	    if(link.startsWith("/") )
		  link = request.getContextPath() + link;
	  %>
	  <font size="-1"><a href="<%=link%>"><%=item%></a></font>
	</logic:notEqual>
	<logic:equal name="selected" value="<%=item%>">
	  <font size="-1" color="fuchsia"><%=item%></font>
  </logic:equal>
  </td>
</tr>
</logic:iterate>

</table>

<%-- Following are some code example using this submenu
<tiles:insert page="/common/submenu.jsp" flush="true">
  <tiles:put name="title" value="Main Menu" />
  <tiles:putList name="items" >
    <tiles:add value="Home" />
    <tiles:add value="Edit Customer" />
    <tiles:add value="Invoice" />
  </tiles:putList>
  <tiles:putList name="links" >
    <tiles:add value="index.jsp" />
    <tiles:add value="invoice/index.jsp" />
    <tiles:add value="invoice/index.jsp" />
  </tiles:putList>
</tiles:insert>

<tiles:insert definition="mainSubMenu" flush="true">
  <tiles:put name="selected" value="Home" />
</tiles:insert>
--%>
