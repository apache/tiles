<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:definition name="test.inner.definition.tags" template="/layout.jsp">
    <tiles:put name="title"  value="This is an inner definition with tags."/>
    <tiles:put name="header" value="/header.jsp"/>
    <tiles:put name="body"   value="/body.jsp"/>
</tiles:definition>
<tiles:definition name="test.composite.definition.tags" template="/layout.jsp">
    <tiles:put name="title"  value="This is a composite definition with tags."/>
    <tiles:put name="header" value="/header.jsp"/>
    <tiles:put name="body"   value="test.inner.definition.tags"/>
</tiles:definition>
<tiles:insert name="test.composite.definition.tags" type="definition" />
