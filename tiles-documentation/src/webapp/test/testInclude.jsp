<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test jsp include in a BodyTag (<iterate>).
--%>
<%
java.util.List list = new java.util.ArrayList();
list.add( "header.jsp" );
list.add( "body.jsp" );
int position=0;
%>

 <hr>
 
 <strong>1 request.getRequestDispatcher(uri).include(request, response)</strong>

 <logic:iterate id="uri" collection="<%=list%>" type="java.lang.String" >
  <br>
  include number <%= position++ %>
  <br>
   <% // insert the id
   response.flushBuffer();
   response.getWriter().flush();
   //out.flush();
   request.getRequestDispatcher(uri).include(request, response);
   response.getWriter().flush();
   response.flushBuffer();
   %>
 </logic:iterate>
 
<hr>
<strong>pageContext.include(page)</strong>

 <logic:iterate id="uri" collection="<%=list%>" type="java.lang.String" >
  <br>
  include number <%= position++ %>
  <br>
   <% // insert the id
   pageContext.include(uri);
   %>
 </logic:iterate><hr>
 
<hr>
<strong>tiles:insert</strong>

 <logic:iterate id="uri" collection="<%=list%>" type="java.lang.String" >
  <br>
  include number <%= position++ %>
  <br>
   <tiles:insert definition="test.layout.test1" flush="false"/>
 </logic:iterate>

<strong>Done</strong>

 
