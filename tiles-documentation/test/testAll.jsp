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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


In the right column you can find some basic examples, 
while in the left column you find the corresponding output result.
<table cellspacing=4 cellpadding="2" border="4" >
<tr>
<td><strong>Output Result</strong></td>
<td><strong>Sources</strong></td>
</tr>
<tr>
    <td valign="top"><tiles:insert page="testBasic.jsp" /></td>
    <td valign="top">
	  <tiles:insert page="/common/viewSrcBody.jsp">
	    <tiles:put name="srcPath" value="/test/testBasic.jsp" />
	  </tiles:insert>
	</td>
</tr>
<tr>
    <td valign="top"><tiles:insert page="testList.jsp" /></td>
    <td valign="top">
	  <tiles:insert page="/common/viewSrcBody.jsp">
	    <tiles:put name="srcPath" value="/test/testList.jsp" />
	  </tiles:insert>
	</td>
</tr>
<tr>
    <td valign="top"><tiles:insert page="testDefinitions.jsp" /></td>
    <td valign="top">
	  <tiles:insert page="/common/viewSrcBody.jsp">
	    <tiles:put name="srcPath" value="/test/testDefinitions.jsp" />
	  </tiles:insert>
	</td>
</tr>
</table>
