<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/common/submenu.jsp" flush="true">
  <tiles:put name="title" value="Main Menu" />
  <tiles:putList name="items" >
    <tiles:add value="Home" />
    <tiles:add value="Basic Page" />
    <tiles:add value="First Portal" />
  </tiles:putList>
  <tiles:putList name="links" >
    <tiles:add value="/tutorial/index.jsp" />
    <tiles:add value="/tutorial/basicPage.jsp" />
    <tiles:add value="/tutorial/portalPage.jsp" />
  </tiles:putList>
</tiles:insert>
