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
<%@ taglib uri="/WEB-INF/struts-tiles.tld"    prefix="tiles" %>
<%@ taglib uri="/WEB-INF/extensions.tld"    prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%-- Edit an Address object
  @param address An address object to edit.
  @param compName UI Component name. Use as prefix for html fields and sub-components
--%>
<%-- Retrieve parameters from component context, and declare them as page variable --%>
<tiles:useAttribute id="addr" name="address" scope="page"  />
<tiles:useAttribute id="prefix" name="compName" classname="java.lang.String"/>
<%-- Add a separator tothe component name, in order to have html fields prefix name : 'compName.'--%>
<% prefix = prefix + "."; %>

<table border="0" width="100%">

  <tr>
    <th align="right" width="30%">
      Street helo
    </th>
    <td align="left">
	  <%-- Declare an html input field. 										--%>
	  <%-- We use a tag that extends Struts 'text' tag. This extension add 		--%>
	  <%-- attribute 'prefix', allowing to give a prefix to the normal name  	--%>
	  <ext:text name="addr" prefix="<%=prefix%>" property="street1" size="50"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      Street (con't)
    </th>
    <td align="left">
        <ext:text prefix="<%=prefix%>" name="addr" property="street2" size="50"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      City
    </th>
    <td align="left">
        <ext:text prefix="<%=prefix%>" name="addr" property="city" size="50"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      Country
    </th>
    <td align="left">
        <ext:text prefix="<%=prefix%>" name="addr" property="country" size="50"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      Zip code
    </th>
    <td align="left">
	  <ext:text prefix="<%=prefix%>" name="addr" property="zipCode" size="50"/>
    </td>
  </tr>

</table>
