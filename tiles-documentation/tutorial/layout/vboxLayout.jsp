<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Layout component 
  Render a list on severals columns
  parameters : componentsList 
--%>

<tiles:useAttribute id="list" name="componentsList" classname="java.util.List" />

<%-- Normally, we should use the iterate tag to iterate over the list.
  Unfortunatelly, this doesn't work with jsp1.1, because actual iterate tag use a BodyContent
  and include do a flush(), which is illegal inside a BodyContent.
  Jsp1.3 propose a new tag mechanism for iteration, which not use BodyContent.
  Wait until jsp1.3 
  
<logic:iterate id="comp" name="list" type="String" >
  <tiles:insert name="<%=comp%>" flush="false" />
  <br>
</logic:iterate>  
--%>

<%-- For now, do iteration manually : --%> 
<%
Iterator i=list.iterator();
while( i.hasNext() )
  {
  String comp=(String)i.next();
%>
<tiles:insert name="<%=comp%>" flush="true" />
<br>

<%
  } // end loop
%>

