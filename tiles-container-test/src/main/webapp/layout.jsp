<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<table  border="2"  width="300"  bordercolor="Gray">
  <tr>
    <td  bgcolor="Blue"><strong><tiles:getAsString name="title"/></strong></td>
  </tr>
  <tr>
    <td><tiles:attribute name="header"/></td>
  </tr>
  <tr>
    <td><tiles:attribute name="body"/></td>
  </tr>
</table>           