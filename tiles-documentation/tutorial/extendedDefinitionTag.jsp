<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:definition id="definitionName" extends="myFirstDefinition" >
  <tiles:put name="title"  value="My first extended definition tag page" />
</tiles:definition>

<tiles:insert beanName="definitionName" flush="true" />
