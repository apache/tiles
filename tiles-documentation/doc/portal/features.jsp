      <table width="100%">
         <tr>
            <th bgcolor="aqua">
               <font size="+1"><strong>Tiles Library<br>
                Features Overview</strong></font> 
            </th>
         </tr>
         <tr>
            <td>
               <p>
                  <font size="2"><strong><font size="2"><strong>Following is an
                  overview of what can be done with Tiles
                  :</strong></font></strong></font>
               </p>
               <ul>
                  <li>
                     <font size="2">Screen definitions</font> 
                     <ul>
                        <li>
                           <font size="2">Create a screen by assembling
                           <strong><em>Tiles</em></strong> : header, footer,
                           menu, body</font>
                        </li>
                        <li>
                           <font size="2">Definitions can take place :</font> 
                           <ul>
                              <li>
                                 <font size="2">in a centralized xml
                                 file</font>
                              </li>
                              <li>
                                 <font size="2">directly in jsp page</font>
                              </li>
                              <li>
                                 <font size="2">in struts action</font>
                              </li>
                           </ul>
                        </li>
                        <li>
                           <font size="2">Definitions provide an inheritance
                           mechanism : a definition can extends another one,
                           and override parameters.</font>
                        </li>
                     </ul>
                  </li>
                  <li>
                     <font size="2">Templating</font> 
                     <ul>
                        <li>
                           <font size="2"><strong><em>Tiles</em></strong>
                           framework is entirely compatible with
                           <em>Templates</em> defined by David Geary and
                           implemented in Struts</font>
                        </li>
                        <li>
                           <font size="2">You can replace <em>Templates</em>
                           library by <strong><em>Tiles</em></strong>
                           one</font>
                        </li>
                     </ul>
                  </li>
                  <li>
                     <font size="2">Layouts</font> 
                     <ul>
                        <li>
                           <font size="2">Define common page layouts and reuse
                           them across your web site</font>
                        </li>
                        <li>
                           <font size="2">Define menu layouts, and use them by
                           passing lists of items and links</font>
                        </li>
                        <li>
                           <font size="2">Define portal layout, use it by
                           passing list of <strong><em>Tiles</em></strong>
                           (pages) to show</font>
                        </li>
                        <li>
                           <font size="2">Reuse existing layouts, or define
                           your owns</font>
                        </li>
                     </ul>
                  </li>
                  <li>
                     <font size="2">Dynamic page building</font> 
                     <ul>
                        <li>
                           <font size="2">Tiles are gather dynamically during
                           page reload. It is possible to change any attributes
                           : layout, list of Tiles in portal, list of menu
                           items, ...</font>
                        </li>
                     </ul>
                  </li>
                  <li>
                     <font size="2">Reuse of <strong><em>Tiles</em></strong> /
                     Components</font> 
                     <ul>
                        <li>
                           <font size="2">If well defined, a
                           <strong><em>Tile</em></strong> can be reused in
                           different location</font>
                        </li>
                        <li>
                           <font size="2">Dynamic attributes are used to
                           parameterized <em><strong>Tiles</strong></em></font>
                        </li>
                        <li>
                           <font size="2">It is possible to define library of
                           reusable <em><strong>Tiles</strong></em>.</font>
                        </li>
                        <li>
                           <font size="2">Build a page by assembling predefined
                           components, give them appropriate parameters</font>
                        </li>
                     </ul>
                  </li>
                  <li>
                     <font size="2">Internationalization (i18n)</font>
                  </li>
				  <ul>
                  <li>
                     <font size="2">It is possible to load different tiles
                     according to Locale</font>
                  </li>
                  <li>
                     <font size="2">A mechanism similar to Java properties
                     files is used for definitions files : you can have one
                     definition file per Locale. The appropriate definition is
                     loaded according to current Locale</font>
                  </li>
				  </ul>
                  <li>
                     <font size="2">Multi-channels</font>
                  </li>
				  <ul>
                  <li>
                     <font size="2">It is possible to load different Tiles
                     according to a key stored in jsp session, or
                     anywhere.</font>
                  </li>
				  
                  <li>
                     <font size="2">For example, key could be user provilege,
                     browser type, ...</font>
                  </li>
				  
                  <li>
                     <font size="2">A mechanism similar to Java properties
                     files is used for definitions files : you can have one
                     definition file per key. The appropriate definition is
                     loaded according to the key.</font>
                  </li>
				  </ul>
               </ul>
            </td>
         </tr>
      </table>


