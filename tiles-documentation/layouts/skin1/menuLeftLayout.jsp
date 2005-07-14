<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Layout Tiles 
  This layout create a html page with <header> and <body> tags. It render
   a header, left menu, body and footer tile.
  @param title String use in page title
  @param header Header tile (jsp url or definition name)
  @param menu Menu 
  @param body Body
  @param footer Footer
--%>
<HTML>
  <HEAD>
	<link rel=stylesheet href="<%=request.getContextPath()%>/layouts/stylesheet.css" type="text/css">
    <title><tiles:getAsString name="title"/></title>
	
  </HEAD>

<body bgcolor="#C0C0C0" text="#000000" link="#023264" alink="#023264" vlink="#023264">
<table border="0" width="100%" cellspacing="5">
<tr>
  <td colspan="2"><tiles:insert attribute="header" /></td>
</tr>
<tr>
  <td valign="top"  align="left">
    <tiles:insert attribute='body' />
  </td>
  <td width="140" valign="top">
    <tiles:insert attribute='menu'/>
  </td>
</tr>
<tr>
  <td colspan="2">
    <tiles:insert attribute="footer" />
  </td>
</tr>
</table>
</body>
</html>

