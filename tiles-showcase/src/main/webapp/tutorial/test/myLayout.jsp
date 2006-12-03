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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<%-- Layout component 
  parameters : title, header, menu, body, footer 
--%>

<html>
<head>
    <title><tiles:getAsString name="title"/></title>
</head>

<body>
<TABLE width="100%">
  
  <TR>
    <TD colspan="2">
	  <tiles:insertAttribute name="header" >
	    <%-- FIXME It had beanScope="template", check if the definition is taken
	    from componentContext--%>
	    <tiles:put name="body" value="${body}" />
	  </tiles:insertAttribute>
	</TD></TR>
  <TR>
    <TD width="120"><tiles:insertAttribute name="menu" /></TD>
    <TD>
	  <tiles:useAttribute name="body" classname="java.lang.String"/>
	  <bean:insert id="bodyStr" page="<%=body%>" />
	  <bean:write name="bodyStr" filter="false"/>
	</TD></TR>
  <TR>
    <TD colspan="2"><tiles:insertAttribute name="footer" /></TD>
  </TR>
</TABLE>

</body>
</html>
