<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<tiles:importAttribute name="stringTest"/>
<tiles:importAttribute name="list"/>
Single attribute "stringTest" value: <c:out value="${stringTest}" /> <br/><br/>
The attribute "list" contains these values:
<ul>
<c:forEach var="item" items="${list}">
<li><c:out value="${item}" /></li>
</c:forEach>
</ul>