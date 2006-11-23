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
<%@ taglib uri="/WEB-INF/tiles.tld" prefix="tiles" %>

<tiles:insert page="/layout/columnsLayout.jsp" flush="true">
  <tiles:put name="numCols" value="2" />
  <tiles:putList name="list0" >
    <tiles:add value="/tutorial/portal/login.jsp" />
    <tiles:add value="/tutorial/portal/messages.jsp" />
    <tiles:add value="/tutorial/portal/newsFeed.jsp" />
    <tiles:add value="/tutorial/portal/advert2.jsp" />
  </tiles:putList>
  <tiles:putList name="list1" >
    <tiles:add value="/tutorial/portal/advert3.jsp" />
    <tiles:add value="/tutorial/portal/stocks.jsp" />
    <tiles:add value="/tutorial/portal/whatsNew.jsp" />
    <tiles:add value="/tutorial/portal/personalLinks.jsp" />
    <tiles:add value="/tutorial/portal/search.jsp" />
  </tiles:putList>
</tiles:insert>