<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/tutorial/common/menuViewSrc.jsp" flush="true" >
  <tiles:putList name="list" >
    <tiles:add value="/tutorial/basicPage.jsp" />
    <tiles:add value="/tutorial/portalPage.jsp" />
    <tiles:add value="/tutorial/portal/portalBody.jsp" />
    <tiles:add value="/tutorial/common/header.jsp" />
    <tiles:add value="/tutorial/common/menu.jsp" />
    <tiles:add value="/tutorial/common/footer.jsp" />
    <tiles:add value="/layout/classicLayout.jsp" />
    <tiles:add value="/layout/vboxLayout.jsp" />
  </tiles:putList>
</tiles:insert>
