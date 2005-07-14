<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test 'ignore' attribute : 
  Insert components/templates with undefined attributes, or bad urls.
  Undefined must be spkipped, while false urls must output exception.
--%>
<hr>
<strong>Test ignore : body isn't defined</strong>
<br>
<tiles:insert template="layoutTestIgnore.jsp">
  <tiles:put name="title"  value="Test ignore : body isn't defined" />
  <tiles:put name="header" value="header.jsp" />
</tiles:insert>

<hr>
<strong>Test ignore : bad body definition name (exception must be shown)</strong>
<br>
<tiles:insert template="layoutTestIgnore.jsp">
  <tiles:put name="title"  value="Test ignore : bad body definition name (exception must be shown)" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="badDefinitionName" type="definition" />
</tiles:insert>

<hr>
<strong>Test ignore : Definition not found (no errors, no insertion)</strong>
<br>
<tiles:definition id="templateDefinition" template="layout.jsp">
  <tiles:put name="title"  value="Test ignore : Definition not found (no errors, no insertion)" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp"   />
</tiles:definition>
<tiles:insert beanName="badTemplateDefinitionName" ignore="true"/>

<hr>
<strong>Test ignore : bad body urls (exception must be shown)</strong>
<br>
<tiles:insert template="layoutTestIgnore.jsp">
  <tiles:put name="title"  value="Test ignore : bad body urls (exception must be shown)" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body2.jsp"/>
</tiles:insert>

