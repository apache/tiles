<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Layout component 
  parameters : title, header, menu, body, footer 
--%>
<HTML>
  <HEAD>
    <title><tiles:getAsString name="title"/></title>
  </HEAD>

<body bgcolor="#ffffff" text="#000000" link="#023264" alink="#023264" vlink="#023264">
<table border="0" width="100%" cellspacing="5">
<tr>
  <td colspan="2"><tiles:insert attribute="header" /></td>
</tr>
<tr>
  <td width="140" valign="top">
    <tiles:insert attribute='menu'/>
  </td>
  <td valign="top"  align="left">
    <tiles:insert attribute='body' />
  </td>
</tr>
<tr>
  <td colspan="2">
    <hr>
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

