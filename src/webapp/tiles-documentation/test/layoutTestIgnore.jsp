<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>



<%--
  Layout to test 'ignore' attribute.
--%>
<table  border="2"  width="300"  bordercolor="Gray">
<tr>
<td  bgcolor="Blue"><strong><tiles:getAsString name="title" ignore="false" /></strong></td>
</tr>
<tr>
<td><tiles:insert attribute="header" ignore="true"/></td>
</tr>
<tr>
<td><tiles:insert attribute="body" ignore="true"/>
</td>
</tr>
</table>
