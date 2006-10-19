<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert name="test.definition" type="definition">
  <tiles:put name="body"   value="/override.jsp" />
</tiles:insert>
