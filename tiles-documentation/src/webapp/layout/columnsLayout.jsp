<%@ page import="org.apache.struts.tiles.ComponentContext"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Layout component 
  Render a list on severals columns
  parameters : numCols, list0, list1, list2, list3, ... 
--%>

<tiles:useAttribute id="numColsStr" name="numCols" classname="java.lang.String" />


<table>
<tr>
<%
int numCols = Integer.parseInt(numColsStr);
ComponentContext context = ComponentContext.getContext( request );
for( int i=0; i<numCols; i++ )
  {
  java.util.List list=(java.util.List)context.getAttribute( "list" + i );
  pageContext.setAttribute("list", list );
  if(list==null)
    System.out.println( "list is null for " + i  );
%>
<td valign="top">
  <tiles:insert page="/layout/vboxLayout.jsp" flush="true" >
    <tiles:put name="componentsList" beanName="list" beanScope="page" />
  </tiles:insert>
</td>
<%
  } // end loop
%>
</tr>
</table>






