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


<%-- 
  This file contains definition declarations.
  These definitions can be used in any jsp page by including 
  this file with :
     <%@ include file="filename.jsp" %> 
  Each definition declaration create a bean stored in specified jsp context.
  Default jsp context is 'page', so we need to specify a more useful context.
--%>

  <%-- Master layout definition --%>
<tiles:definition id="masterLayout" page="/layouts/classicLayout.jsp" scope="request" >
  <tiles:put name="title"  value="My First Definition Page" />
  <tiles:put name="header" value="/tutorial/common/header.jsp" />
  <tiles:put name="footer" value="/tutorial/common/footer.jsp" />
  <tiles:put name="menu"   value="/tutorial/common/menu.jsp" />
  <tiles:put name="body"   value="/tutorial/basic/helloBody.jsp" />
</tiles:definition>

  <%-- portal definition --%>
<tiles:definition id="portalExample" page="/layout/columnsLayout.jsp" scope="request">
  <tiles:put name="numCols" value="2" />
  <tiles:putList name="list0" >
    <tiles:add value="/portal/login.jsp" />
    <tiles:add value="/portal/messages.jsp" />
    <tiles:add value="/portal/newsFeed.jsp" />
    <tiles:add value="/portal/advert2.jsp" />
  </tiles:putList>
  <tiles:putList name="list1" >
    <tiles:add value="/portal/advert3.jsp" />
    <tiles:add value="/portal/stocks.jsp" />
    <tiles:add value="/portal/whatsNew.jsp" />
    <tiles:add value="/portal/personalLinks.jsp" />
    <tiles:add value="/portal/search.jsp" />
  </tiles:putList>
</tiles:definition>

  <%-- 
    Add as many definition as you need ...
  --%>
