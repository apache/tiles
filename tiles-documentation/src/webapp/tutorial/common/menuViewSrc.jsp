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
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%-- Menu of sources component 
  This component is a menu of sources. When a source is clicked, its 
  code is shown using a viewer.
  A list of absolute source path must be provided.
  Viewer path can be provided.
  @param list List of source paths to display
  @param viewerPath Path of viewer page. Optional.
--%>

<tiles:importAttribute />

  <%-- Set default viewer if not specified --%>
<logic:notPresent name="viewerPath" >
  <% pageContext.setAttribute( "viewerPath", request.getContextPath()+"/tutorial/common/viewSrc.jsp" ); %>
</logic:notPresent>
<bean:define id="viewerPath" name="viewerPath" type="java.lang.String" />

<table>
<tr><th colspan=2><div align="left"><strong>JSP Sources</strong></div></th></tr>

<%-- Iterate on sources list --%>
<logic:iterate id="iterateItem" name="list" >
<bean:define id="srcPath" name="iterateItem" type="java.lang.String" scope="page" />

<tr>
  <td width="10" align="center"></td>
  <td  width="120">
    <font size="-2">
      <a href="<%=viewerPath%>?src=<%=srcPath%>"><%=srcPath%></a>
    </font>
  </td>
</tr>

</logic:iterate>
</table>
