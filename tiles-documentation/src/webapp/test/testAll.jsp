<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


In the right column you can find some basic examples, 
while in the left column you find the corresponding output result.
<table cellspacing=4 cellpadding="2" border="4" >
<tr>
<td><strong>Output Result</strong></td>
<td><strong>Sources</strong></td>
</tr>
<tr>
    <td valign="top"><tiles:insert page="testBasic.jsp" /></td>
    <td valign="top">
	  <tiles:insert page="/common/viewSrcBody.jsp">
	    <tiles:put name="srcPath" value="/test/testBasic.jsp" />
	  </tiles:insert>
	</td>
</tr>
<tr>
    <td valign="top"><tiles:insert page="testList.jsp" /></td>
    <td valign="top">
	  <tiles:insert page="/common/viewSrcBody.jsp">
	    <tiles:put name="srcPath" value="/test/testList.jsp" />
	  </tiles:insert>
	</td>
</tr>
<tr>
    <td valign="top"><tiles:insert page="testDefinitions.jsp" /></td>
    <td valign="top">
	  <tiles:insert page="/common/viewSrcBody.jsp">
	    <tiles:put name="srcPath" value="/test/testDefinitions.jsp" />
	  </tiles:insert>
	</td>
</tr>
</table>
