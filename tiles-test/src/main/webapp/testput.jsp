<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert template="/layout.jsp">
  <tiles:put name="title"  value="This is the title." />
  <tiles:put name="header" value="/header.jsp" />
  <tiles:put name="body"   value="/body.jsp" />
</tiles:insert>