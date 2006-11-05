<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<tiles:importAttribute/>

<c:out value="${one}"/>
<ul>
  <li><c:out value="${two}"/></li>
  <li><c:out value="${three}"/></li>
  <li><c:out value="${four}"/> </li>
</ul>