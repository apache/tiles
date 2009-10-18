<%@ page session="false" %>
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
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<table  border="2"  width="300"  bordercolor="Gray">
  <tr>
    <td  bgcolor="Blue"><strong><tiles:getAsString name="title"/></strong></td>
  </tr>
  <tr>
    <td><tiles:insertAttribute name="header"/></td>
  </tr>
  <tr>
    <td><tiles:insertAttribute name="body"/></td>
  </tr>
</table>           