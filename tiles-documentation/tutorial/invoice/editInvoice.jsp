<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld"    prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:errors/>

<html:form action="/invoice/editInvoice.do" >

<font size="+1">Edit Customer Informations</font>
			   
<table border="0" width="100%">

  <tr>
    <th align="right" width="30%">
      First Name
    </th>
    <td align="left">
        <html:text property="firstname" size="50"/>
    </td>
  </tr>

  <tr>
    <th align="right">
      Last Name
    </th>
    <td align="left">
	  <html:text property="lastname" size="50"/>
    </td>
  </tr>
 

  <tr>
    <th align="right" >
      Shipping Address
    </th>
    <td align="left">
	  &nbsp;
    </td>
  </tr>
  <tr>
    <td align="center" colspan="2">
	  <%-- Include an "address editor" component.					--%>
	  <%-- Pass the component name and component value as parameter	--%>
	  <%-- Value comes from the form bean --%>
	  <tiles:insert page="/tutorial/invoice/editAddress.jsp" >
	    <tiles:put name="property" value="shippingAddress" />
	    <tiles:put name="bean" beanName="invoiceForm"  />
	  </tiles:insert>
    </td>
  </tr>

  <tr>
    <th align="right" >
      Billing Address
    </th>
    <td align="left">
	  &nbsp;
    </td>
  </tr>
  <tr>
    <td align="center" colspan="2">
  <tiles:insert page="/tutorial/invoice/editAddress.jsp" >
    <tiles:put name="property" value="billAddress" />
	<tiles:put name="bean" beanName="invoiceForm" />
  </tiles:insert>
    </td>
  </tr>

  <tr>
    <td align="right">
        <html:submit>
          save
        </html:submit>
        <html:submit>
          confirm
        </html:submit>
    </td>
    <td align="left">
        <html:reset>
          reset
        </html:reset>
      &nbsp;
      <html:cancel>
        cancel
      </html:cancel>
    </td>
  </tr>
</table>
			   
</html:form>
