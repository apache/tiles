<%@ page import="org.apache.struts.tiles.ComponentContext"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Multi-columns Layout
  This layout render lists of tiles in multi-columns. Each column renders its tiles
  vertically stacked. 
  The number of columns passed in parameter must correspond to the number of list passed.
  Each list contains tiles. List are named list0, list1, list2, ...
  parameters : numCols, list0, list1, list2, list3, ... 
  @param numCols Number of columns to render and passed as parameter
  @param list1 First list of tiles (url or definition name)
  @param list2 Second list of tiles (url or definition name) [optional]
  @param list3 Third list of tiles (url or definition name) [optional]
  @param listn Niene list of tiles (url or definition name), where n is replaced by column index.
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
  <tiles:insert page="/layouts/vboxLayout.jsp" flush="true" >
    <tiles:put name="list" beanName="list" beanScope="page" />
  </tiles:insert>
</td>
<%
  } // end loop
%>
</tr>
</table>






