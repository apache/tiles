<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<script language="javaScript1.2">
function selectAll( )
{
for( j=0; j<selectAll.arguments.length; j++ )
  {
  col1 = selectAll.arguments[j];
  for(i=0; i<col1.options.length; i++ )
    {
	col1.options[ i ].selected = true;
	}
  } // end loop
  return true;
}

function move( col1, col2)
{
  toMove = col1.options[ col1.selectedIndex ];
  opt = new Option( toMove.text, toMove.value, false, false );
  col1.options[col1.selectedIndex ] = null;
  col2.options[col2.length] = opt;
  return true;
}

function remove( col1)
{
  col1.options[ col1.selectedIndex ] = null;
  return true;
}

function up( col1 )
{
  index = col1.selectedIndex;
  if( index <= 0 )
    return true;
	
  toMove = col1.options[ index ];
  opt = new Option( toMove.text, toMove.value, false, false );
  col1.options[index] = col1.options[index-1];
  col1.options[index-1] = opt;
  return true;
}

function down( col1 )
{
  index = col1.selectedIndex;
  if( index+1 >=  col1.options.length )
    return true;
	
  toMove = col1.options[ index ];
  opt = new Option( toMove.text, toMove.value, false, false );
  col1.options[index] = col1.options[index+1];
  col1.options[index+1] = opt;
  return true;
}

</script>


<html:form action="/actions/myPortalPrefs.do"  >


  
  <html:select property="remaining" multiple="true" >
    <html:options property="choices" />
  </html:select>

  <html:button property="v" value="v" onclick="move(remaining,l0);return true;"/>
  <br>
  
  <table>
  <tr>
    <td>
	  <html:select property="l0" multiple="true" size="10">
	    <html:options property="col[0]" labelProperty="colLabels[0]"/>
	  </html:select>
	</td>
	<td>
	  <html:select property="l1" multiple="true" size="10">
	    <html:options property="col[1]" labelProperty="colLabels[1]"/>
	  </html:select>
	</td>
  </tr>
  <tr>
  <td align="center">
  <html:button property="right" value="^"   onclick="up(l0);return true;"/>
  <html:button property="right" value="del"   onclick="remove(l0);return true;"/>
  <html:button property="right" value="v"   onclick="down(l0);return true;"/>
  <html:button property="left" value=">"     onclick="move(l0,l1);return false;" />
  </td>
  <td align="center">
  <html:button property="right" value="<"    onclick="move(l1,l0);return true;"/>
  <html:button property="right" value="^"   onclick="up(l1);return true;"/>
  <html:button property="right" value="del"   onclick="remove(l1);return true;"/>
  <html:button property="right" value="v"   onclick="down(l1);return true;"/>
  </td>
  </tr>
  <tr>
    <td colspan="2"  align="center">
	  
      <html:submit property="validate" value="validate" onclick="selectAll(l0, l1);return true;"/>
	</td>
  </tr>
  </table>
  
  

  
</html:form>
