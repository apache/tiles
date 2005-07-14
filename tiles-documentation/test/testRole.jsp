<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test tags basic behaviors 
--%>
<hr>
<strong>Show Component if 'tomcat' role is set</strong>
<br>
<tiles:insert template="layout.jsp" role="tomcat">
  <tiles:put name="title"  value="Tomcat role" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp" />
</tiles:insert>

<hr>
<strong>Show Component if 'role1' role is set</strong>
<br>
<tiles:insert template="layout.jsp" role="role1">
  <tiles:put name="title"  value="role1 role" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp" />
</tiles:insert>

