<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Test tiles and struts action integration (require factory) 
--%>
<hr>
<strong>Test definition set in action, and action forward to another definition</strong>
<br>
<tiles:insert page="/test/testAction.do" >
  <tiles:put name="title"       value="Test definition set in action, and action forward to another definition. Title is overloaded from insert" />
    <%-- header and body values come from definition used in action's forward --%>
    <%-- name of definition to use in action --%>
  <tiles:put name="set-definition"   value="test.layout.test1" />
</tiles:insert>

<hr>
<strong>Test overload parameter in action</strong>
<br>
<tiles:insert page="/test/testAction.do" >
  <tiles:put name="title"                 value="Test overload parameter in action" />
  <tiles:put name="header"                value="header.jsp" />
  <tiles:put name="body"                  value="body.jsp" />
    <%-- name and value of attribute to set in action --%>
  <tiles:put name="set-attribute"         value="title" />
  <tiles:put name="set-attribute-value"   value="Test overload parameter in action : Overloaded title" />
</tiles:insert>

<hr>
<strong>Test definition set in action, and action forward directly to jsp</strong>
<br>
<tiles:insert page="/test/testActionForwardJsp.do" >
  <tiles:put name="title"            value="Test definition set in action, and action forward directly to jsp" />
  <tiles:put name="header"           value="header.jsp" />
  <tiles:put name="body"             value="body.jsp" />
</tiles:insert>


