<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>



<%--
  A simple layout.
  First, we get component/template attribute (title) as a String.
  Next, we insert component/template attribute (header and body).
--%>
<table  border="2"  width="300"  bordercolor="Gray">
<tr>
<td  bgcolor="Blue"><strong><tiles:getAsString name="title"/></strong></td>
</tr>
<tr>
<td><tiles:insert attribute="header"/></td>
</tr>
<tr>
<td><tiles:insert attribute="body"/></td>
</tr>
</table>
