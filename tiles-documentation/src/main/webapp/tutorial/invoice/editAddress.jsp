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
<%@ taglib uri="/WEB-INF/struts-tiles.tld"    prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%-- Edit an Address object
  @param bean An address object to edit.
  @param beanName The path to add between the bean and the properties to edit.
--%>
<%-- Retrieve parameters from component context, and declare them as page variable --%>
<tiles:useAttribute id="beanName" name="property" classname="java.lang.String" ignore="true" />
<tiles:importAttribute name="bean" />

<%-- Add a '.' separator to the path (beanName), in order to access the property from the given bean --%>
<% if( beanName == null ) beanName = ""; else if (beanName !="" ) beanName = beanName + "."; %>

<table border="0" width="100%">

  <tr>
    <th align="right" width="30%">
      Street
    </th>
    <td align="left">
	  <%-- Declare an html input field. 										--%>
	  <%-- We use the bean passed as parameter.  								--%>
	  <%-- Property name is prefixed by the sub-bean name if any.			    --%>
	  
	  <html:text name="bean" property='<%=beanName+"street1"%>' size="50"/>
	  
    </td>
  </tr>

  <tr>
    <th align="right">
      Street (con't)
    </th>
    <td align="left">
        <html:text property='<%=beanName+"street2"%>' size="50"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      City
    </th>
    <td align="left">
        <html:text name="bean" property='<%=beanName+"city"%>' size="50"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      Country
    </th>
    <td align="left">
        <html:text property='<%=beanName+"country"%>' size="50"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      Zip code
    </th>
    <td align="left">
	  <html:text property='<%=beanName+"zipCode"%>' size="50"/>
    </td>
  </tr>

</table>
