<%--
/**
 * Summarize channels as unadorned HTML.
 *
 * @parameters ListArray CHANNELS
 * @version $Revision$ $Date$
 */
--%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:importAttribute name="CHANNELS" scope="page"/>

<logic:iterate name="CHANNELS" id="CHANNEL" >
<TABLE border="0" cellspacing="2" cellpadding="4" width="300" align="center" >
<TR>
<TD><logic:present name="CHANNEL" property="image"><img src="<bean:write name="CHANNEL" property="image.URL"/>"></logic:present></TD>
<TD class="spanhd" width="100%"><a href="<bean:write name="CHANNEL" property="link"/>">
<bean:write name="CHANNEL" property="title"/></a></TD>
</TR>
<TD class="yellow" colspan="2"><bean:write name="CHANNEL" property="description"/></TD>
</TR>

<TR>
<TD class="datagrey" colspan="2">
<logic:iterate name="CHANNEL" property="items" id="ITEM">
<br><b><bean:write name="ITEM" property="title"/></b>
<br><bean:write name="ITEM" property="description"/>
<br>&nbsp;&nbsp;[ <a href="<bean:write name="ITEM" property="link"/>">more</a> ]
<br>
</logic:iterate>
</TD>
</TR>
</TABLE>
<br>
</logic:iterate>
