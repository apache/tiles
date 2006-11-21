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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Frameset Layout component 
  parameters : title, header, menu, body, footer 
--%>

<html>
<head>
    <title><tiles:get name="title"/></title>
</head>

<frameset rows="73, *, 73">
  <frame src="<%=request.getContextPath()%><tiles:get name="header" />" name="header" >
  <frame src="<%=request.getContextPath()%><tiles:get name="body" />" name="body" >
  <frame src="<%=request.getContextPath()%><tiles:get name="footer" />" name="footer" >
</frameset>


</html>
