<div align="center"><font size="+1"><b>

<TABLE border="0" cellPadding="2" cellSpacing=0 width="100%" >
  
  <TR>
    <TD class=spanhd>Administration</TD>
  </TR>
  <TR>
    <TD class="datagrey">
	<ul>
	  <li>Some administration facilities are provided.</li>
	  <li>It is possible to reload definitions configuration files without restarting
	  the web server.</li>
	  <li>Also, it is possible to view definitions loaded and resolved by the factory.</li>
	  <li>This facilities are used during development. They should be removed or protected with a password 
	  if used in a production environment.</li>
	</ul>
	</TD>
  </TR>
  <TR>
    <td class="datalightblue">  
	<ul>
	  <li><a href="<%=request.getContextPath()%>/admin/tiles/reload.do">Reload Factory</a></li>
	  <li><a href="<%=request.getContextPath()%>/admin/tiles/view.do">View Factory</a></li>
	</ul>
    </TD>
  </TR>
</TABLE>

</b></font></div>