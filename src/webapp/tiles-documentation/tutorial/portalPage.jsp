<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/tutorial/layout/classicLayout.jsp" flush="true">
  <tiles:put name="title"  value="My First Portal Page" />
  <tiles:put name="header" value="/tutorial/common/header.jsp" />
  <tiles:put name="footer" value="/tutorial/common/footer.jsp" />
  <tiles:put name="menu"   value="/tutorial/basic/menu.jsp" />
  <%-- <tiles:put name="menu"   value="/tutorial/common/menu.jsp" /> --%>
  <tiles:put name="body"   value="/tutorial/portal/portalBody.jsp" />
</tiles:insert>
