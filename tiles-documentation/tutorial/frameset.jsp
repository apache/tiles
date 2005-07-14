<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Layout component 
  parameters : title, header, menu, body, footer 
--%>

<html>
<head>
    <title><tiles:getAsString name="title"/></title>
</head>

<body>
<frameset rows="3">
  <frame src="<tiles:get name="header" />" name="header" id="header" scrolling="Auto">
  <frame src="<tiles:get name="body" />" name="body" id="header" scrolling="Auto">
  <frame src="<tiles:get name="footer" />" name="footer" id="header" scrolling="Auto">
</frameset>
</body>


</html>
