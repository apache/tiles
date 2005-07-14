<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert definition="mainLayout" flush="true" >
  <tiles:put name="title" value="JSP Source Code" />
  <tiles:put name="body" value="/tutorial/common/viewSrcBody.jsp" />
</tiles:insert>  
