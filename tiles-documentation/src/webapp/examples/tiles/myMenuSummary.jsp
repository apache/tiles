<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<div align="center"><font size="+1"><b>

<TABLE border="0" cellPadding="2" cellSpacing=0 width="100%" >
  
  <TR>
    <TD class=spanhd>User Customized Menu <br>(my Menu)</TD>
  </TR>
  <TR>
    <TD class="datagrey">
	<ul>
	  <li>It is possible to allow user to customize its own menu.</li>
	  <li>User menu settings are stored in user session as a list of menu entries.</li>
	  <li>The same layout as other menus is used, but the list is provided by an associated
	  "controller".</li>
	  <li>You also need to provide a page allowing user to choose and arrange its own menu entries.</li>
	  <li>You can use provided example, or improve it to meet your need.</li>
	</ul>
	</TD>
  </TR>
  <TR>
    <td class="datalightblue" >
	<ul>
	  <li>Edit user menu entries :
        <BR><a href="<%=request.getContextPath()%>/examples/myMenuSettings.jsp">my Menu</a></li>
	  <li>Actual user menu :
        <BR><tiles:insert name="examples.userMenu" flush="true" /></li>
	</ul>
   </TD>
  </TR>

</TABLE>

</b></font></div>
