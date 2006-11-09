<!--
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
-->
<html>
<head>
    <title>Tiles 2 Test Application</title>
</head>

<body>
    <h1>Tiles 2 Test Application</h1>
    
    <h2>Currently working tests</h2>
    <a href="testinsertdefinition.jsp">Test Insert Configured Definition</a><br/>
    <a href="testinsertdefinition_preparer.jsp">Test Insert Configured Definition with Preparer</a><br/>
    <a href="testinsertdefinition_classpath.jsp">Test Insert Configured Classpath Definition</a><br/>
    <a href="testinsertdefinition_notype.jsp">Test Insert Configured Definition with no type specified</a><br/>
    <a href="testinsertdefinition_override.jsp">Test Insert Configured Definition with an overridden content</a><br/>
    <a href="testinsertdefinition_inline.jsp">Test Insert Configured Definition with an inline content</a><br/>
    <a href="testinsertdefinition_composite.jsp">Test Insert Configured Definition that contains another definition inside</a><br/>
    <!-- Broken Tests Go Here! -->
    <a href="testput.jsp">Test Put Tag</a><br/>
    <a href="testput_servlet.jsp">Test Put Tag using a servlet mapping as a template</a><br/>
    <a href="testimportattribute.jsp">Test importAttribute Tag</a><br/>
    <a href="testimportattribute_all.jsp">Test importAttribute Tag with no name</a><br/>

    <h2>Container Management Tags</h2>
    <a href="testinitcontainer.jsp">Test Initialize Container</a><br/>

    <h2>Currently not working tests</h2>
    <a href="testdef.jsp">Test Definition Tag</a><br/>
    <!-- Currently all the tests work.-->
    <a href="testinsertdefinition_composite_tags_includes_configured.jsp">Test Insert Definition that contains another definition inside (configured via tiles-defs.xml) using JSP tags</a><br/>
    <a href="testinsertdefinition_composite_tags_includes_configured_notype.jsp">Test Insert Definition that contains another definition inside (configured via tiles-defs.xml) using JSP tags without types</a><br/>
    <a href="testinsertdefinition_composite_tags.jsp">Test Insert Definition that contains another definition inside using JSP tags</a><br/>
    <a href="testinsertdefinition_composite_tags_notype.jsp">Test Insert Definition that contains another definition inside using JSP tags without types</a><br/></body>

</html>