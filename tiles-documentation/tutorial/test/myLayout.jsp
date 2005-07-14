<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%-- Layout component 
  parameters : title, header, menu, body, footer 
--%>

<html>
<head>
    <title><tiles:getAsString name="title"/></title>
</head>

<body>
<TABLE width="100%">
  
  <TR>
    <TD colspan="2">
	  <tiles:insert attribute="header" >
	    <tiles:put name="body" beanName="body" beanScope="template" />
	  </tiles:insert>
	</TD></TR>
  <TR>
    <TD width="120"><tiles:insert attribute="menu" /></TD>
    <TD>
	  <tiles:useAttribute name="body" classname="java.lang.String"/>
	  <bean:insert id="bodyStr" page="<%=body%>" />
	  <bean:write name="bodyStr" filter="false"/>
	</TD></TR>
  <TR>
    <TD colspan="2"><tiles:insert attribute="footer" /></TD>
  </TR>
</TABLE>

</body>
</html>
