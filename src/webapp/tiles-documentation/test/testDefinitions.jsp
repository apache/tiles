<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test template definitions (from factory) 
--%>
<hr>
<strong>Insert definition defined directly in jsp page</strong>
<tiles:definition id="definition" template="/test/layout.jsp" >
  <tiles:put name="title"  value="Test definition defined in jsp page" />
  <tiles:put name="header" value="header.jsp" />
  <tiles:put name="body"   value="body.jsp" />
</tiles:definition>

<br>
<tiles:insert beanName="definition"/>

<hr>
<strong>Insert definition defined in factory</strong>
<br>
<tiles:insert definition="test.layout.test1"/>

<hr>
<strong>Insert definition defined in factory</strong>
<br>
<tiles:insert definition="test.layout.test2"/>

<hr>
<strong>Insert definition defined in factory</strong>
<br>
<tiles:insert definition="test.layout.test3"/>

<hr>
<strong>Insert definition defined in factory : Overload title attribute</strong>
<br>
<tiles:insert definition="test.layout.test3">
  <tiles:put name="title" value="Test definition : overload attribute 'title'" />
</tiles:insert>
