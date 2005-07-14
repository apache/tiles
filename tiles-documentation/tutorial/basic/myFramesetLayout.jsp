<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Frameset Layout component 
  parameters : title, header, menu, body, footer 
--%>

<html>
<head>
    <title><tiles:get name="title"/></title>
</head>

<frameset rows="73, *, 73">
  <frame src="<%=request.getContextPath()%><tiles:get name="header" />" name="header" >
  <frame src="<%=request.getContextPath()%><tiles:get name="body" />" name="body" >
  <frame src="<%=request.getContextPath()%><tiles:get name="footer" />" name="footer" >
</frameset>


</html>
