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

<%-- Centered Layout Tiles 
  This layout render a header, left tile, right tile, body and footer.
  It doesn't create <html> and <body> tag.
  @param header Header tile (jsp url or definition name)
  @param right Right center tile (optional)
  @param body Body or center tile
  @param left Left center tile (optional)
  @param footer Footer tile
--%>

<table border="0" width="100%" cellspacing="5">
<tr>
  <td colspan="3"><tiles:insertAttribute name="header" /></td>
</tr>
<tr>
  <td width="140" valign="top">
    <tiles:insertAttribute name=right ignore='true'/>
  </td>
  <td valign="top"  align="left">
    <tiles:insertAttribute name='body' />
  </td>
  <td valign="top"  align="left">
    <tiles:insertAttribute name='left' ignore='true'/>
  </td>
</tr>
<tr>
  <td colspan="3">
    <tiles:insertAttribute name="footer" />
  </td>
</tr>
</table>

