<%@ page language="java" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<logic:present parameter="src" >
<bean:parameter id="srcPath" name="src" />
<strong>file '<%=srcPath%>' not found !</strong>
</logic:present>

<logic:notPresent parameter="src" >
No source specified !
</logic:notPresent>
