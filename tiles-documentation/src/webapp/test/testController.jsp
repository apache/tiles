<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test tiles Controller call 
--%>
<hr>
<strong>Test controller set in definition from config file</strong>
<br>
<tiles:insert definition="test.struts.controller" >
</tiles:insert>

<hr>
<strong>Test controller set in insert</strong>
<br>
<tiles:insert template="layout.jsp" 
           controllerClass="org.apache.struts.webapp.tiles.test.TestTileController" >
  <tiles:put name="title"  value="Test controller set in insert" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp" />
</tiles:insert>

<hr>
<strong>Test controller set in insert, and attribute from definition</strong>
<br>
<tiles:insert definition="test.layout.test1" 
           controllerClass="org.apache.struts.webapp.tiles.test.TestTileController" >
  <tiles:put name="title"  value="Test controller set in insert, and attribute from definition" />
</tiles:insert>



