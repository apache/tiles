<table  width="100%">
<tr>
<th bgcolor="aqua"><FONT size=4>History</FONT></th></tr>
  <TR>
    <TD><FONT size=2><STRONG> 03 Nov. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
	  <FONT size=2>
      <UL>
        <LI>Tiles now use the commons-logging package.</LI>
		<li><useAttribute>: Corrected a bug where the tag fail when reused by server.</li>
		<li>The struts TilesPlugin now create one factory for each struts module</li>
	  </UL>
	  </FONT>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 19 Jul. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
	  <FONT size=2>
      <UL>
        <LI>TilesPlugin available for Struts1.1</LI>
		<li>Blank war files have been updated</li>
		<li>Definition factory interface has change. A new life cycle is introduce</li>
		<li>Struts multi-modules feature works with the Tiles</li>
	  </UL>
	  </FONT>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 20 Jun. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>Update DefinitionDispatcherAction. It now works again</FONT></LI>
	  </UL>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 13 Jun. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>Tiles configuration file now accept nested list as attribute</FONT></LI>
        <LI><FONT size=2>Add <uri> tag to DTD (submitted by Loren Halvorson)</FONT></LI>
		configuration file</FONT></LI>
	  </UL>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 23 May 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>tiles.XmlDefinition.XmlParser : enable default value for <item> classtype
		 attribute
		 (org.apache.struts.tiles.beans.SimpleMenuItem). As a consequence, all classtype attribute 
		 can be removed in examples</FONT></LI>
        <LI><FONT size=2>tiles.XmlDefinition.I18nFactorySet : Enable serialization (bug reported 
		by Dan Sykes)</FONT></LI>
	  </UL>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 14 Apr. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>tiles.XmlDefinition.XmlParser : Correct bug with new Digester release 
		preventing config file lists to be correctly parsed</FONT></LI>
        <LI><FONT size=2>tabsLayout.jsp : Correct some syntax errors(bug reported by David Marshall)</FONT></LI>
        <LI><FONT size=2>taglib.tiles.UseAttribute : Add release of variable 'id' in release method 
		(bug reported by Heath Chiavettone)</FONT></LI>
        <LI><FONT size=2>tiles.XmlDefinition.XmlDefinition : Add inheritance for controllerClass and 
		controllerUrl attributes (bug reported by Jim Crossley) </FONT></LI>
        <LI><FONT size=2>tiles.XmlDefinition.I18nFactorySet : Add another loading method for config 
		files in order to let Websphere 3.5.x run (patch from Stephen Houston)</FONT></LI>
        <LI><FONT size=2>tiles.ComponentActionServlet : Add overload of processForward and
		processInclude in order to catch properly forward in struts1.0.x struts-config (bug reported from 
		struts user list)</FONT></LI>
	  </UL>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 22 Feb. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>Correct build process preventing examples compilation in war files </FONT></LI>
	  </UL>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 20 Feb. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>New tiles-blank-struts1.x applications</FONT></LI>
        <LI><FONT size=2>Change distribution (again) : back to the old fashion</FONT></LI>
	  </UL>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 18 Feb. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>New tiles-blank application</FONT></LI>
        <LI><FONT size=2>Added a TilesController to run with latest Struts 1.1 dev</FONT></LI>
        <LI><FONT size=2>Change distribution : a stable release build shipped with Struts1.0.x 
		and a development release build with Struts1.1 dev</FONT></LI>
	  </UL>
	</TD>
  </TR> 
  <TR>
    <TD><FONT size=2><STRONG> 11 Jan. 2002</STRONG> </FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>Corrected bug preventing empty &lt;put&gt; body tags in Tomcat4</FONT></LI>
        <LI><FONT size=2>Corrected customized factory loading. It now works again</FONT></LI>
	  </UL>
	</TD>
  </TR> 

 <TR>
    <TD><FONT size=2><STRONG>&nbsp;&nbsp; <A href="<%=request.getContextPath()%>/doc/portal/revisionsCont.html">
	<FONT size=2><STRONG>more ...</STRONG></FONT></A></STRONG></FONT>
	</TD>
 </TR>
</table>
