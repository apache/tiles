<%@ taglib uri="http://jakarta.apache.org/tiles"
        prefix="tiles" %>

      <%-- One table lays out all of the content for this attribute --%>
      <table width='100%' height='100%'>
         <tr>

            <%-- Sidebar section --%>
            <td width='150' valign='top' align='left'>
               <tiles:insert attribute='sidebar'/>
            </td>

            <%-- Main content section --%>
            <td height='100%' width='*'>
               <table width='100%' height='100%'>
                  <tr>
                     <%-- Header section --%>
                     <td valign='top' height='15%'>
                        <tiles:insert attribute='header'/>
                     </td>
                  <tr>

                  <tr>
                     <%-- Content section --%>
                     <td valign='top' height='*'>
                        <tiles:insert attribute='content'/>
                     </td>
                  </tr>

                  <tr>
                     <%-- Footer section --%>
                     <td valign='bottom' height='15%'>
                        <tiles:insert attribute='footer'/>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>
      </table>
