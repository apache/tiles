<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:definition id="definitionName" template="/tutorial/basic/myLayout.jsp" >
  <tiles:put name="title"  value="My first page" />
  <tiles:put name="header" value="/tutorial/common/header.jsp" />
  <tiles:put name="footer" value="/tutorial/common/footer.jsp" />
  <tiles:put name="menu"   value="/tutorial/basic/menu.jsp" />
  <tiles:put name="body"   value="/tutorial/basic/helloBody.jsp" />
</tiles:definition>

<tiles:insert beanName="definitionName" flush="true" />
