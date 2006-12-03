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


<%-- Menu Layout
  This layout render a menu with links.
  It takes as parameter the title, and a list of items. Each item is a bean with following properties :
  value, href, icon, tooltip.
  @param title Menu title
  @param items list of items. Items are beans whith following properties : 
 --%>



<%-- Push tiles attributes in page context --%>
<tiles:importAttribute />

<table>
<logic:present name="title">
<tr>
  <th colspan=2>
    <div align="left"><strong><tiles:getAsString name="title"/></strong></div>
  </th>
</tr>
</logic:present>

<%-- iterate on items list --%>
<logic:iterate id="item" name="items" type="org.apache.struts.tiles.beans.MenuItem" >

<%  // Add site url if link start with "/"
  String link = item.getLink();
	if(link.startsWith("/") ) link = request.getContextPath() + link;
%>
<tr>
  <td width="10" valign="top" ></td>
  <td valign="top"  >
	  <font size="-1"><a href="<%=link%>">
<logic:notPresent name="item" property="icon"><%=item.getValue()%></logic:notPresent>
<logic:present name="item" property="icon">
	<%  // Add site url if link start with "/"
	  String icon = item.getIcon();
		if(icon.startsWith("/") ) icon = request.getContextPath() + icon;
	%>
<img src='<%=request.getContextPath()%><bean:write name="item" property="icon" scope="page"/>'
       alt='<bean:write name="item" property="tooltip" scope="page" ignore="true"/>' /></logic:present></a>
	  </font>
  </td>
</tr>
</logic:iterate>
</table>
