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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div align="center"><font size="+1"><b>
<TABLE border=0 cellPadding=2 cellSpacing=0 width=190>
  <FORM name=panel>
  <TBODY>
  <TR>
    <TD class=spanhd>Internet Search</TD></TR>
  <TR>
  
<logic:present parameter="query">
<bean:parameter id="query" name="query"/>
    <TD class=datagrey>Query found : <BR><BR> <%=query%></TD></TR>
</logic:present>

    <TD class=datagrey>Enter a term or topic: <BR><BR><INPUT maxLength=32 
      name=query size=17></TD></TR>
  <TR>
    <TD class=inputgrey>&nbsp;<INPUT alt="Search the Internet" border=0 
      height=24 name="Search the Internet" src="<%=request.getContextPath()%>/tutorial/images/input_gen_search.gif" 
      title="Search the Internet" type=image 
  width=86></FORM></TD></TR></FORM></TBODY></TABLE>  </b></font></div>