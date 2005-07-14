<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Layout component 
  parameters : title, header, menu, body, footer 
--%>

<html>
<head>
    <title><tiles:getAsString name="title"/></title>
</head>

<body>
<TABLE width="100%">
  
  <TR>
    <TD colspan="2"><tiles:insert attribute="header" /></TD></TR>
  <TR>
    <TD width="120"><tiles:insert attribute="menu" /></TD>
    <TD><tiles:insert attribute="body" /></TD></TR>
  <TR>
    <TD colspan="2"><tiles:insert attribute="footer" /></TD>
  </TR>
</TABLE>

</body>
</html>
