<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<strong>Example of list usage</strong>
<%-- Insert a menu component.
  Menu component require two lists : one of items, and one of links.
--%>
<tiles:insert component="menu.jsp" >
  <tiles:put name="title" value="Test menu" />
  <tiles:putList name="items">
    <tiles:add value="home" />
    <tiles:add>
	  <img src="<%=request.getContextPath()%>/images/struts-power.gif"
	       align="right" border="0"></tiles:add>
    <tiles:add value="documentation"/>
  </tiles:putList>
  <tiles:putList name="links">
    <tiles:add value="/index.jsp"/>
    <tiles:add value="/../struts-documentation"/>
    <tiles:add value="/../comps-doc" type="string" />
  </tiles:putList>
</tiles:insert>
