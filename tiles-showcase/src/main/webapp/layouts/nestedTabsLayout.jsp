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

<%-- 
  Tabs Layout .
  This layout allows to render several tiles in a tabs fashion. Such tabs can be nested: a tab level can   contain nested tabs. Each tabs must be declared with a different parameter name.
   Implementation store each tabs selection index in the session context. The unique id for the 
  session attribut is compute from the url and the tabs selection parameter. 
  [todo] Improve the computation of unique id. 
  @param tabList A list of available tabs. We use MenuItem to carry data (name, body, icon, ...)
  @param selectedIndex Index of default selected tab
  @param parameterName Name of parameter carrying selected info in http request.
--%>

<%-- 
Use tiles attributes, and declare them as page java variable.
These attribute must be passed to the tile. 
--%>

<tiles:useAttribute name="parameterName" classname="java.lang.String" />
<tiles:useAttribute id="selectedIndexStr" name="selectedIndex" ignore="true" classname="java.lang.String" />
<tiles:useAttribute name="tabList" classname="java.util.List" />
<%
  String selectedColor="#98ABC7";
  String notSelectedColor="#C0C0C0";
  
  int index = 0; // Loop index
  int selectedIndex = 0;
    // Check if selected come from request parameter
  try {
    selectedIndex = Integer.parseInt(selectedIndexStr);
	  // Try to retrieve from http parameter, or previous storage
	  // Need to use a more unique id for storage name
	String paramValue = request.getParameter( parameterName );
	if( paramValue == null )
	  {
      selectedIndex = ((Integer)(session.getAttribute( 
	            request.getRequestURI() + parameterName ))).intValue();
	  }
	 else
      selectedIndex = Integer.parseInt(paramValue);
	}
   catch( java.lang.NumberFormatException ex )
    { // do nothing
	}
   catch( java.lang.NullPointerException ex )
    { // do nothing
	}
  // Check selectedIndex bounds
  if( selectedIndex < 0 || selectedIndex >= tabList.size() ) selectedIndex = 0;
  String selectedBody = ((org.apache.struts.tiles.beans.MenuItem)tabList.get(selectedIndex)).getLink(); // Selected body
  // Store selected index for future references
  session.setAttribute( request.getRequestURI() + parameterName , new Integer(selectedIndex) );
%>

<table border="0"  cellspacing="0" cellpadding="0">
  <%-- Draw tabs --%>
<tr>
  <td width="10">&nbsp;</td>
  <td>
    <table border="0"  cellspacing="0" cellpadding="5">
      <tr>
<logic:iterate id="tab" name="tabList" type="org.apache.struts.tiles.beans.MenuItem" >
<% // compute href
  String href = request.getRequestURI() + "?"+parameterName + "=" + index;
  String color = notSelectedColor;
  if( index == selectedIndex )
    {
	selectedBody = tab.getLink();
	color = selectedColor;
	} // enf if
  index++;
%>
  <td bgcolor="<%=color%>">
  <a href="<%=href%>" ><%=tab.getValue()%></a>
  </td>
  <td width="1" ></td>
  
</logic:iterate>
      </tr>
    </table>
  </td>
  <td width="10" >&nbsp;</td>
</tr>


<tr>
  <td height="5" bgcolor="<%=selectedColor%>" colspan="3" >&nbsp;</td>
</tr>  

  <%-- Draw body --%>
<tr>
  <td width="10" bgcolor="<%=selectedColor%>">&nbsp;</td>
  <td>
  <tiles:attribute name="<%=selectedBody%>" flush="true" />
  </td>
  <td width="10" bgcolor="<%=selectedColor%>">&nbsp;</td>
</tr>  

<tr>
  <td height="5" bgcolor="<%=selectedColor%>" colspan="3" >&nbsp;</td>
</tr>  

</table>

