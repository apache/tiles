<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test backward compatibility
--%>

<hr>
<strong>Check synonyms for include and put.</strong>
<br>
<tiles:insert component="layout.jsp">
  <tiles:put name="title"  value="Test with a tag body" direct="true" />
  <tiles:put name="header" value="header.jsp">
    <strong>This is a header</strong>
  </tiles:put>
  <tiles:put name="body"   value="body.jsp" direct="false" />
</tiles:insert>
