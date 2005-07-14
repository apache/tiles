<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"     %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld"  prefix="tiles" %>

<html:html>
  <head>
    <title>Content Form</title>
  </head>

  <body bgcolor="white">
      <bean:insert id='xout2' page='/testAction.do'/>
      <bean:write name='xout2' filter='true'/>

      <tiles:insert template="/forwardExampleAction.do">
        <tiles:put name="label"    content="Text:"       direct="true"/>
        <tiles:put name="property" content="contentForm" direct="true"/>
      </tiles:insert>

	
  </body>
</html:html>

