<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test tags basic behaviors 
--%>
<hr>
<strong>Basic template usage</strong>
<br>
<tiles:insert template="layout.jsp">
  <tiles:put name="title"  value="Test with default no types" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp" />
</tiles:insert>

<hr>
<strong>Specify attribute types</strong>
<br>
<tiles:insert template="layout.jsp">
  <tiles:put name="title"  value="Test with specified types"   type="string" />
  <tiles:put name="header" value="header.jsp" type="page"   />
  <tiles:put name="body"   value="body.jsp"   type="page"   />
</tiles:insert>

<hr>
<strong>Set attribute value with tag body</strong>
<br>
<tiles:insert template="layout.jsp">
  <tiles:put name="title"  value="Test with a tag body" />
  <tiles:put name="header" type="string">
    <strong>This header is inserted as body of tag</strong>
  </tiles:put>
  <tiles:put name="body"   value="body.jsp"/>
</tiles:insert>

<hr>
<strong>Use of definition</strong>
<br>
<tiles:definition id="templateDefinition" template="layout.jsp">
  <tiles:put name="title"  value="Use of definition" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp"   />
</tiles:definition>
<tiles:insert beanName="templateDefinition" />

<hr>
<strong>Use of definition, overload of parameters </strong>Title parameter
from previous definition is overloaded
<br>
<tiles:insert beanName="templateDefinition" >
  <tiles:put name="title"  value="Use of definition, overload of parameters"   type="string" />
</tiles:insert>

<hr>
<strong>Test ignore : body isn't defined </strong>(We use another layout)
<br>
<tiles:insert template="layoutTestIgnore.jsp">
  <tiles:put name="title"  value="Test ignore : body isn't defined" />
  <tiles:put name="header" value="header.jsp" />
</tiles:insert>

 
