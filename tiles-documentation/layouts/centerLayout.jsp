<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Centered Layout Tiles 
  This layout render a header, left tile, right tile, body and footer.
  It doesn't create <html> and <body> tag.
  @param header Header tile (jsp url or definition name)
  @param right Right center tile (optional)
  @param body Body or center tile
  @param left Left center tile (optional)
  @param footer Footer tile
--%>

<table border="0" width="100%" cellspacing="5">
<tr>
  <td colspan="3"><tiles:insert attribute="header" /></td>
</tr>
<tr>
  <td width="140" valign="top">
    <tiles:insert attribute=right ignore='true'/>
  </td>
  <td valign="top"  align="left">
    <tiles:insert attribute='body' />
  </td>
  <td valign="top"  align="left">
    <tiles:insert attribute='left' ignore='true'/>
  </td>
</tr>
<tr>
  <td colspan="3">
    <tiles:insert attribute="footer" />
  </td>
</tr>
</table>

