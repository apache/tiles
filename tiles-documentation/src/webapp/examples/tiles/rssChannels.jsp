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
<%--
/**
 * Summarize channels as unadorned HTML.
 *
 * @param CHANNELS List of channels
 * @version $Revision$ $Date$
 */
--%>

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<div align="center"><font size="+1"><b>

<tiles:importAttribute name="CHANNELS" scope="page"/>

<logic:iterate name="CHANNELS" id="CHANNEL" >
<TABLE border="0" cellspacing="0" cellpadding="4" width="100%" align="center" >
<TR>
<TD class="spanhd" ><logic:present name="CHANNEL" property="image">
  <a href="<bean:write name="CHANNEL" property="link"/>">
    <img src="<bean:write name="CHANNEL" property="image.URL"/>"></logic:present></a>
</TD>
<TD class="spanhd" width="100%"><bean:write name="CHANNEL" property="title"/> <a href="<bean:write name="CHANNEL" property="link"/>">[home]</a></TD>
</TR>
<TD class="yellow" colspan="2"><bean:write name="CHANNEL" property="description"/></TD>
</TR>

<TR>
<TD class="datagrey" colspan="2">
<logic:iterate name="CHANNEL" property="items" id="ITEM">
<br><b><bean:write name="ITEM" property="title"/></b>
<br><bean:write name="ITEM" property="description"/>
<br>&nbsp;&nbsp;[ <a href="<bean:write name="ITEM" property="link"/>">more</a> ]
<br>
</logic:iterate>
</TD>
</TR>
</TABLE>
<br>
</logic:iterate>

</b></font></div>
