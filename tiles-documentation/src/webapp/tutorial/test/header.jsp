<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"     %>


<P><img src="<%=request.getContextPath()%>/images/id_nav_outside.gif" align="left" border="0">
<img src="<%=request.getContextPath()%>/images/id_nav_bkgnd.gif" align="right" border="0"> </P>

value="<tiles:getAsString name="body"/>"
