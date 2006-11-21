<%--
/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
--%>
<div align="center"><font size="+1"><b>

<TABLE border="0" cellPadding="2" cellSpacing=0 width="100%" >
  
  <TR>
    <TD class=spanhd>Administration</TD>
  </TR>
  <TR>
    <TD class="datagrey">
	<ul>
	  <li>Some administration facilities are provided.</li>
	  <li>It is possible to reload definitions configuration files without restarting
	  the web server.</li>
	  <li>Also, it is possible to view definitions loaded and resolved by the factory.</li>
	  <li>This facilities are used during development. They should be removed or protected with a password 
	  if used in a production environment.</li>
	</ul>
	</TD>
  </TR>
  <TR>
    <td class="datalightblue">  
	<ul>
	  <li><a href="<%=request.getContextPath()%>/admin/tiles/reload.do">Reload Factory</a></li>
	  <li><a href="<%=request.getContextPath()%>/admin/tiles/view.do">View Factory</a></li>
	</ul>
    </TD>
  </TR>
</TABLE>

</b></font></div>