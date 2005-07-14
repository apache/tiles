<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Layout component 
  Render a list of tiles in a vertical column
  @param : list List of names to insert 
  This file contains 3 possible implementations.
  Use only one and comment the others. Don't forget to move the appropriate taglib
  declaration in the header.
--%>

<tiles:useAttribute id="list" name="list" classname="java.util.List" />

<%-- Iterate over names.
  We don't use <iterate> tag because it doesn't allow insert (in JSP1.1)
 --%>
<%
Iterator i=list.iterator();
while( i.hasNext() )
  {
  String name= (String)i.next();
%>
<tiles:insert name="<%=name%>" flush="true" />
<br>

<%
  } // end loop
%>


<%-- Iterate over names.
  Use jstl <forEach> tag. 
  Require the jstl taglib.
 --%>
<%-- 
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:forEach var="name" items="${list}">
  <tiles:insert beanName="name" flush="true" />
<br>
</c:forEach>
 --%>
 
<%-- Iterate over names.
  Use struts <iterate> tag. Work on jsp1.2 and greater web container.
 --%>
<%-- 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:iterate id="name" name="list" type="java.lang.String">
  <tiles:insert beanName="name" flush="false" />
<br>
</logic:iterate>
 --%>
