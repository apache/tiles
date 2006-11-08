<%--
/*
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
<table  width="100%">
<tr>
<th bgcolor="aqua">
  <font size="+1"><strong>Tiles and Struts</STRONG></FONT>
</TH>
</TR>
  <TR>
    <TD>
      <P>
      <FONT size=2>Tiles are actually shipped with Struts</FONT></P>
      <P>             
      <FONT size=2>    
	  Tiles and Struts code are clearly separated. In fact, Tiles can run without Struts. But using 
	  both give you interresting features, like forwarding an action to a tile's definition.  
      </FONT>
      <FONT size=2>Following is a list of actual modifications :</FONT> </P></TD></TR>
  <TR>
    <TD><FONT size=2><b>Tile Action Servlet</B></FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>Add a 
        "processForward" method. </FONT>    
        <LI><FONT size=2>Purpose : be 
        able to subclass servlet, and override the forward mechanism. </FONT>
        <LI><FONT size=2>Needed if you 
        want to forward to a definition in 
  struts-config.xml.</FONT></LI>
        <LI><FONT size=2>A ready to run servlet is provided.</FONT></LI>
     </UL>
  
    </TD>
  </TR>
  <TR>
    <TD><FONT size=2><STRONG> <EM>text</EM> 
    tag</STRONG></FONT></TD></TR>
  <TR>
    <TD>
      <UL>
        <LI><FONT size=2>Not 
        mandatory, can be omitted if not used </FONT>
        <LI><FONT size=2>Add a 
        "prefix" attribute. </FONT>    
        <LI><FONT size=2>Purpose : be 
        able to add a prefix to the name of generated input tag. This 
        modification is not mandatory. It is only useful in some 
        examples.</FONT> </LI></UL></TD></TR></TABLE>