<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%-- Layout component 
  parameters : title, header, menu, body, footer 
--%>

<html>
<head>
    <title><tiles:get name="title"/></title>
</head>

<body>
<TABLE width="100%">
  
  <TR>
    <TD colspan="2"><tiles:get name="header" /></TD></TR>
  <TR>
    <TD width="120"><tiles:get name="menu" /></TD>
    <TD>
	  **<tiles:insert name="body" flush='true'/>**
	</TD></TR>
  <TR>
    <TD colspan="2"><tiles:get name="footer" /></TD>
  </TR>
</TABLE>

</body>
</html>
