<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/test/myLayout.jsp" flush="true">
  <tiles:put name="title"  value="My first page" />
  <tiles:put name="header" value="/test/header.jsp" />
  <tiles:put name="footer" value="/common/footer.jsp" />
  <tiles:put name="menu"   value="/basic/menu.jsp" />
  <tiles:put name="body"   value="/forwardExampleAction.do" />
</tiles:insert>
