<%--
/**
 * Summarize channels errors as unadorned HTML.
 *
 * @parameters errors
 * @version $Revision: 1.2 $ $Date$
 */
--%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<TABLE border="0" cellspacing="2" cellpadding="4" width="300" align="center" >
<TR>
<TD class="alert">
Error while reading channels.
<br></br>Are you connected ?
</TD>
</TR>
<TR>
<TD class="error" width="100%"><html:errors/></TD>
</TR>

</TABLE>

