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

<%-- Layout Tiles 
  This layout create a html page with <header> and <body> tags. It render
   a header, left menu, body and footer tile.
  @param title String use in page title
  @param header Header tile (jsp url or definition name)
  @param menu Menu 
  @param body Body
  @param footer Footer
--%>
<HTML>
  <HEAD>
	<link rel=stylesheet href="<%=request.getContextPath()%>/layouts/stylesheet.css" type="text/css">
    <title><tiles:getAsString name="title"/></title>
	
  </HEAD>

<body bgcolor="#C0C0C0" text="#000000" link="#023264" alink="#023264" vlink="#023264">
<table border="0" width="100%" cellspacing="5">
<tr>
  <td colspan="2"><tiles:attribute name="header" /></td>
</tr>
<tr>
  <td valign="top"  align="left">
    <tiles:attribute name='body' />
  </td>
  <td width="140" valign="top">
    <tiles:attribute name='menu'/>
  </td>
</tr>
<tr>
  <td colspan="2">
    <tiles:attribute name="footer" />
  </td>
</tr>
</table>
</body>
</html>

