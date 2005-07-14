<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert definition="mainLayout" flush="true">
  <tiles:put name="body" value="/tutorial/invoice/editInvoice.jsp" />
  <%--  <tiles:put name="body" value="/tutorial/invoice/editInvoice2.jsp" /> --%>
</tiles:insert>

