<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div align="center"><font size="+1"><b>
<TABLE border=0 cellPadding=2 cellSpacing=0 width=190>
  <FORM name=panel>
  <TBODY>
  <TR>
    <TD class=spanhd>Internet Search</TD></TR>
  <TR>
  
<logic:present parameter="query">
<bean:parameter id="query" name="query"/>
    <TD class=datagrey>Query found : <BR><BR> <%=query%></TD></TR>
</logic:present>

    <TD class=datagrey>Enter a term or topic: <BR><BR><INPUT maxLength=32 
      name=query size=17></TD></TR>
  <TR>
    <TD class=inputgrey>&nbsp;<INPUT alt="Search the Internet" border=0 
      height=24 name="Search the Internet" src="<%=request.getContextPath()%>/tutorial/images/input_gen_search.gif" 
      title="Search the Internet" type=image 
  width=86></FORM></TD></TR></FORM></TBODY></TABLE>  </b></font></div>