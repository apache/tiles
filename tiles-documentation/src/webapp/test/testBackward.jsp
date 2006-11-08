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

<%-- Test backward compatibility
--%>

<hr>
<strong>Check synonyms for include and put.</strong>
<br>
<tiles:insert component="layout.jsp">
  <tiles:put name="title"  value="Test with a tag body" direct="true" />
  <tiles:put name="header" value="header.jsp">
    <strong>This is a header</strong>
  </tiles:put>
  <tiles:put name="body"   value="body.jsp" direct="false" />
</tiles:insert>
