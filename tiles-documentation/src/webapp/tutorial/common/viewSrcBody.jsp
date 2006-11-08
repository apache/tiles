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
<%@ page language="java" %>
<%@ page errorPage="/common/viewSrcBodyError.jsp" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%-- Include requested resource file.
  If file is not found, Exception is thrown, and catched by the errorPage 
  directive (see above). Error page show a message.   
--%>

  <%-- Import component attributes, if any.
   --%>
   

<tiles:importAttribute/>

<logic:notPresent name="srcPath" >
  <logic:present parameter="src" >
  <bean:parameter id="srcPath" name="src" />
  </logic:present>  
</logic:notPresent>
     

<logic:present name="srcPath" >
<bean:define id="srcPathVar" name="srcPath" type="java.lang.String"/>
<bean:resource id="src" name="<%=srcPathVar%>" />
<strong>file '<%=srcPathVar%>'</strong>
<br>
<pre>
<bean:write filter="true" name="src" scope="page"/>
</pre>
<br>
</logic:present>

<logic:notPresent name="srcPath" >
No source specified !
</logic:notPresent>
