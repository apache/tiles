<%@ page session="false" %>
<!--
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
-->
<html>
<head>
    <title>Tiles 2 Test Application</title>
</head>

<body>
    <h1>Tiles 2 Test Application</h1>

    <h1>JSP-based tests</h1>

    <h2>Features in Tiles 2.0.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="testinsertdefinition.jsp">Test Insert Configured Definition</a><br/>
    <a href="testinsertdefinitionexpr.jsp">Test Insert Configured Definition With Expression</a><br/>
    <a href="testinsertdefinition_ignore.jsp">Test Insert Configured Definition with Ignore</a><br/>
    <a href="testinsertdefinition_flush.jsp">Test Insert Configured Definition with Flush</a><br/>
    <a href="testinsertdefinition_preparer.jsp">Test Insert Configured Definition with Preparer</a><br/>
    <a href="testinsertdefinition_preparer_configured.jsp">Test Insert Configured Definition with Preparer configured in the definition itself</a><br/>
    <a href="testinsertdefinition_classpath.jsp">Test Insert Configured Classpath Definition</a><br/>
    <a href="testinsertdefinition_override.jsp">Test Insert Configured Definition with an overridden content</a><br/>
    <a href="testinsertdefinition_override_and_not.jsp">Test Insert Configured Definition with an overridden content and one with original content</a><br/>
    <a href="testinsertdefinition_inline.jsp">Test Insert Configured Definition with an inline content</a><br/>
    <a href="testinsertdefinition_composite.jsp">Test Insert Configured Definition that contains another definition inside</a><br/>
    <a href="testinsertdefinition_exception.jsp">Test Insert Configured Definition with an exception in an attribute page</a><br/>
    <a href="testinsertdefinition_freemarker.jsp">Test Insert Configured Definition with FreeMarker</a><br/>
    <a href="testinsertdefinition_openbody.jsp">Test Insert Configured Definition with Open Body</a><br/>
    <a href="testput.jsp">Test Put Tag</a><br/>
    <a href="testput_flush.jsp">Test Put Tag with Flush</a><br/>
    <a href="testput_el.jsp">Test Put Tag using EL</a><br/>
    <a href="testput_servlet.jsp">Test Put Tag using a servlet mapping as a template</a><br/>
    <a href="testputlist.jsp">Test Put List Tag</a><br/>
    <a href="testimportattribute.jsp">Test importAttribute Tag</a><br/>
    <a href="testimportattribute_all.jsp">Test importAttribute Tag with no name</a><br/>
    <a href="testdecorationfilter.jsp">Test Tiles Definition Filter</a><br/>
    <a href="testdispatchservlet.tiles">Test Tiles Dispatch Servlet</a><br/>
    <a href="selectlocale.jsp">Test Localization</a><br/>

    <h3>Mutable Container Tests</h3>
    <a href="testdef.jsp">Test Definition Tag</a><br/>
    <a href="testdef_extend.jsp">Test Definition Tag extending configured and custom definitions</a><br/>
    <a href="testdef_preparer.jsp">Test Definition Tag with Preparer</a><br/>
    <a href="testinsertdefinition_composite_tags_includes_configured.jsp">Test Insert Definition that contains another definition inside (configured via tiles-defs.xml) using JSP tags</a><br/>
    <a href="testinsertdefinition_composite_tags.jsp">Test Insert Definition that contains another definition inside using JSP tags</a><br/>
    <a href="testinsertdefinition_composite_tags_includes_configured_notype.jsp">Test Insert Definition that contains another definition inside (configured via tiles-defs.xml) using JSP tags without types</a><br/>
    <a href="testinsertdefinition_composite_tags_notype.jsp">Test Insert Definition that contains another definition inside using JSP tags without types</a><br/></body>

    <h3>Roles Verification tests</h3>
    <a href="testinsertdefinition_role.jsp">Test Insert Configured Definition with Specified Role</a><br/>
    <a href="testinsertdefinition_role_tag.jsp">Test Insert Configured Definition with Specified Role in Tag</a><br/>
    <a href="testinsertdefinition_attribute_roles.jsp">Test Insert Configured Definition with Attribute that have Roles</a><br/>
    <a href="testinsertdefinition_attribute_roles_tags.jsp">Test Insert Configured Definition with Attribute that have Roles in Tags</a><br/>

    <h2>Features in Tiles 2.1.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="testinsertdefinition_override_template.jsp">Test Insert Configured Definition with an overridden template</a><br/>
    <a href="testinsertdefinition_old.jsp">Test Insert Configured Definition in Old Format</a><br/>
    <a href="testinsertdefinition_cascaded.jsp">Test Insert Configured Cascaded Definition</a><br/>
    <a href="testinsertdefinition_cascaded_overridden.jsp">Test Insert Configured Cascaded Definition with Override</a><br/>
    <a href="testinsertdefinition_cascaded_template.jsp">Test Insert Configured Cascaded Definition with Template</a><br/>
    <a href="testinsertdefinition_cascaded_list.jsp">Test Insert Configured Cascaded Definition with List</a><br/>
    <a href="testinsertdefinition_reversed.jsp">Test Insert Configured Definition with Reversed Attribute</a><br/>
    <a href="testinsertdefinition_attribute_preparer.jsp">Test Insert Configured Definition with Attribute Preparer</a><br/>
    <a href="testinsertnesteddefinition.jsp">Test Insert Nested Definition</a><br/>
    <a href="testinsertnesteddefinition_tags.jsp">Test Insert Nested Definition only using JSP tags</a><br/>
    <a href="testinsertnestedlistdefinition.jsp">Test Insert Nested List Definition</a><br/>
    <a href="testinsertnestedlistdefinition_tags.jsp">Test Insert Nested List Definition only using JSP tags</a><br/>
    <a href="testinsertdefinition_el.jsp">Test Insert Configured Definition with EL</a><br/>
    <a href="testinsertdefinition_el_singleeval.jsp">Test Insert Configured Definition with EL to test Single Evaluation</a><br/>
    <a href="testinsertdefinition_wildcard.jsp">Test Insert Configured Definition with Wildcards</a><br/>
    <a href="testinsertdefinition_defaultvalues.jsp">Test Insert Configured Definition with Default Values</a><br/>
    <a href="testput_cascaded.jsp">Test Put Tag with Cascaded Attributes</a><br/>
    <a href="testput_cascaded_overridden.jsp">Test Put Tag with Overridden Cascaded Attributes</a><br/>
    <a href="testput_cascaded_template.jsp">Test Put Tag with Cascaded Attributes and Template</a><br/>
    <a href="testput_el_singleeval.jsp">Test Put Tag using EL to test Single Evaluation</a><br/>
    <a href="testput_reversed.jsp">Test Put Tag with Reversed Attribute</a><br/>
    <a href="testputlist_cascaded.jsp">Test Put List Cascaded Tag</a><br/>
    <a href="testputlist_inherit.jsp">Test Put List Tag with Inherit</a><br/>
    <a href="testimportattribute_inherit.jsp">Test importAttribute Tag with List Inherit</a><br/>
    <a href="testsetcurrentcontainer.jsp">Test setCurrentContainer Tag</a><br/>

    <h3>Mutable Container Tests</h3>
    <a href="testdef_list_inherit.jsp">Test Definition Tag with a List Inherit</a><br/>

    <h3>Database Verification tests</h3>
    <a href="testinsertdefinition_db.jsp">Test Insert Configured Definition from DB</a><br/>
    <a href="testinsertdefinition_extended_db.jsp">Test Insert Extended Configured Definition from DB</a><br/>
    <a href="selectlocale_db.jsp">Test Localization from DB</a><br/>

    <h2>Features in Tiles 2.2.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="testinsertdefinition_mvel.jsp">Test Insert Configured Definition with MVEL</a><br/>
    <a href="testinsertdefinition_ognl.jsp">Test Insert Configured Definition with OGNL</a><br/>
    <a href="testinsertdefinition_regexp.jsp">Test Insert Configured Definition with Regular Expression</a><br/>

    <h2>Features in Tiles 3.0.x</h2>

    <h3>TILES-571</h3>
    <a href="testunderscores_nolocale.jsp">Test underscores without localization</a>

	<h1>FreeMarker-based tests</h1>

    <h2>Features in Tiles 2.0.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="freemarker/testinsertdefinition.ftl">FreeMarker: Test Insert Configured Definition</a><br/>
    <a href="freemarker/testinsertdefinition_ignore.ftl">FreeMarker: Test Insert Configured Definition with Ignore</a><br/>
    <a href="freemarker/testinsertdefinition_flush.ftl">FreeMarker: Test Insert Configured Definition with Flush</a><br/>
    <a href="freemarker/testinsertdefinition_preparer.ftl">FreeMarker: Test Insert Configured Definition with Preparer</a><br/>
    <a href="freemarker/testinsertdefinition_preparer_configured.ftl">FreeMarker: Test Insert Configured Definition with Preparer configured in the definition itself</a><br/>
    <a href="freemarker/testinsertdefinition_classpath.ftl">FreeMarker: Test Insert Configured Classpath Definition</a><br/>
    <a href="freemarker/testinsertdefinition_override.ftl">FreeMarker: Test Insert Configured Definition with an overridden content</a><br/>
    <a href="freemarker/testinsertdefinition_override_and_not.ftl">FreeMarker: Test Insert Configured Definition with an overridden content and one with original content</a><br/>
    <a href="freemarker/testinsertdefinition_inline.ftl">FreeMarker: Test Insert Configured Definition with an inline content</a><br/>
    <a href="freemarker/testinsertdefinition_composite.ftl">FreeMarker: Test Insert Configured Definition that contains another definition inside</a><br/>
    <a href="freemarker/testinsertdefinition_exception.ftl">FreeMarker: Test Insert Configured Definition with an exception in an attribute page</a><br/>
    <a href="freemarker/testinsertdefinition_openbody.ftl">FreeMarker: Test Insert Configured Definition with Open Body</a><br/>
    <a href="freemarker/testput.ftl">FreeMarker: Test Put Tag</a><br/>
    <a href="freemarker/testput_flush.ftl">FreeMarker: Test Put Tag with Flush</a><br/>
    <a href="freemarker/testput_el.ftl">FreeMarker: Test Put Tag using EL</a><br/>
    <a href="freemarker/testput_servlet.ftl">FreeMarker: Test Put Tag using a servlet mapping as a template</a><br/>
    <a href="freemarker/testputlist.ftl">FreeMarker: Test Put List Tag</a><br/>
    <a href="freemarker/testimportattribute.ftl">FreeMarker: Test importAttribute Tag</a><br/>
    <a href="freemarker/testimportattribute_all.ftl">FreeMarker: Test importAttribute Tag with no name</a><br/>
    <a href="freemarker/testdecorationfilter.ftl">FreeMarker: Test Tiles Definition Filter</a><br/>
    <a href="freemarker.testdispatchservlet.tiles">FreeMarker: Test Tiles Dispatch Servlet</a><br/>
    <a href="freemarker/selectlocale.ftl">FreeMarker: Test Localization</a><br/>

    <h3>Mutable Container Tests</h3>
    <a href="freemarker/testdef.ftl">FreeMarker: Test Definition Tag</a><br/>
    <a href="freemarker/testdef_extend.ftl">FreeMarker: Test Definition Tag extending configured and custom definitions</a><br/>
    <a href="freemarker/testdef_preparer.ftl">FreeMarker: Test Definition Tag with Preparer</a><br/>
    <a href="freemarker/testinsertdefinition_composite_tags_includes_configured.ftl">FreeMarker: Test Insert Definition that contains another definition inside (configured via tiles-defs.xml) using JSP tags</a><br/>
    <a href="freemarker/testinsertdefinition_composite_tags.ftl">FreeMarker: Test Insert Definition that contains another definition inside using JSP tags</a><br/>
    <a href="freemarker/testinsertdefinition_composite_tags_includes_configured_notype.ftl">FreeMarker: Test Insert Definition that contains another definition inside (configured via tiles-defs.xml) using JSP tags without types</a><br/>
    <a href="freemarker/testinsertdefinition_composite_tags_notype.ftl">FreeMarker: Test Insert Definition that contains another definition inside using JSP tags without types</a><br/>

    <h3>Roles Verification tests</h3>
    <a href="freemarker/testinsertdefinition_role.ftl">FreeMarker: Test Insert Configured Definition with Specified Role</a><br/>
    <a href="freemarker/testinsertdefinition_role_tag.ftl">FreeMarker: Test Insert Configured Definition with Specified Role in Tag</a><br/>
    <a href="freemarker/testinsertdefinition_attribute_roles.ftl">FreeMarker: Test Insert Configured Definition with Attribute that have Roles</a><br/>
    <a href="freemarker/testinsertdefinition_attribute_roles_tags.ftl">FreeMarker: Test Insert Configured Definition with Attribute that have Roles in Tags</a><br/>

    <h2>Features in Tiles 2.1.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="freemarker/testinsertdefinition_override_template.ftl">FreeMarker: Test Insert Configured Definition with an overridden template</a><br/>
    <a href="freemarker/testinsertdefinition_old.ftl">FreeMarker: Test Insert Configured Definition in Old Format</a><br/>
    <a href="freemarker/testinsertdefinition_cascaded.ftl">FreeMarker: Test Insert Configured Cascaded Definition</a><br/>
    <a href="freemarker/testinsertdefinition_cascaded_overridden.ftl">FreeMarker: Test Insert Configured Cascaded Definition with Override</a><br/>
    <a href="freemarker/testinsertdefinition_cascaded_template.ftl">FreeMarker: Test Insert Configured Cascaded Definition with Template</a><br/>
    <a href="freemarker/testinsertdefinition_cascaded_list.ftl">FreeMarker: Test Insert Configured Cascaded Definition with List</a><br/>
    <a href="freemarker/testinsertdefinition_reversed.ftl">FreeMarker: Test Insert Configured Definition with Reversed Attribute</a><br/>
    <a href="freemarker/testinsertdefinition_attribute_preparer.ftl">FreeMarker: Test Insert Configured Definition with Attribute Preparer</a><br/>
    <a href="freemarker/testinsertnesteddefinition.ftl">FreeMarker: Test Insert Nested Definition</a><br/>
    <a href="freemarker/testinsertnesteddefinition_tags.ftl">FreeMarker: Test Insert Nested Definition only using JSP tags</a><br/>
    <a href="freemarker/testinsertnestedlistdefinition.ftl">FreeMarker: Test Insert Nested List Definition</a><br/>
    <a href="freemarker/testinsertnestedlistdefinition_tags.ftl">FreeMarker: Test Insert Nested List Definition only using JSP tags</a><br/>
    <a href="freemarker/testinsertdefinition_el.ftl">FreeMarker: Test Insert Configured Definition with EL</a><br/>
    <a href="freemarker/testinsertdefinition_el_singleeval.ftl">FreeMarker: Test Insert Configured Definition with EL to test Single Evaluation</a><br/>
    <a href="freemarker/testinsertdefinition_wildcard.ftl">FreeMarker: Test Insert Configured Definition with Wildcards</a><br/>
    <a href="freemarker/testinsertdefinition_defaultvalues.ftl">FreeMarker: Test Insert Configured Definition with Default Values</a><br/>
    <a href="freemarker/testput_cascaded.ftl">FreeMarker: Test Put Tag with Cascaded Attributes</a><br/>
    <a href="freemarker/testput_cascaded_overridden.ftl">FreeMarker: Test Put Tag with Overridden Cascaded Attributes</a><br/>
    <a href="freemarker/testput_cascaded_template.ftl">FreeMarker: Test Put Tag with Cascaded Attributes and Template</a><br/>
    <a href="freemarker/testput_el_singleeval.ftl">FreeMarker: Test Put Tag using EL to test Single Evaluation</a><br/>
    <a href="freemarker/testput_reversed.ftl">FreeMarker: Test Put Tag with Reversed Attribute</a><br/>
    <a href="freemarker/testputlist_cascaded.ftl">FreeMarker: Test Put List Cascaded Tag</a><br/>
    <a href="freemarker/testputlist_inherit.ftl">FreeMarker: Test Put List Tag with Inherit</a><br/>
    <a href="freemarker/testimportattribute_inherit.ftl">FreeMarker: Test importAttribute Tag with List Inherit</a><br/>
    <a href="freemarker/testsetcurrentcontainer.ftl">FreeMarker: Test setCurrentContainer Tag</a><br/>

    <h3>Mutable Container Tests</h3>
    <a href="freemarker/testdef_list_inherit.ftl">FreeMarker: Test Definition Tag with a List Inherit</a><br/>

    <h3>Database Verification tests</h3>
    <a href="freemarker/testinsertdefinition_db.ftl">FreeMarker: Test Insert Configured Definition from DB</a><br/>
    <a href="freemarker/testinsertdefinition_extended_db.ftl">FreeMarker: Test Insert Extended Configured Definition from DB</a><br/>

    <h2>Features in Tiles 2.2.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="freemarker/testinsertdefinition_mvel.ftl">FreeMarker: Test Insert Configured Definition with MVEL</a><br/>
    <a href="freemarker/testinsertdefinition_ognl.ftl">FreeMarker: Test Insert Configured Definition with OGNL</a><br/>
    <a href="freemarker/testinsertdefinition_regexp.ftl">FreeMarker: Test Insert Configured Definition with Regular Expression</a><br/>
    <a href="org/apache/tiles/test/alt/freemarker/testinsertdefinition_alt.ftl">FreeMarker: Test Insert Configured Definition in Module</a><br/>

    <h2>Features in Tiles 3.0.x</h2>

    <h3>TILES-571</h3>
    <a href="freemarker/testunderscores_nolocale.ftl">FreeMarker: Test underscores without localization</a>

    <h1>Velocity-based tests</h1>

    <h2>Features in Tiles 2.0.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="velocity/testinsertdefinition.vm">Velocity: Test Insert Configured Definition</a><br/>
    <a href="velocity/testinsertdefinition_ignore.vm">Velocity: Test Insert Configured Definition with Ignore</a><br/>
    <a href="velocity/testinsertdefinition_flush.vm">Velocity: Test Insert Configured Definition with Flush</a><br/>
    <a href="velocity/testinsertdefinition_preparer.vm">Velocity: Test Insert Configured Definition with Preparer</a><br/>
    <a href="velocity/testinsertdefinition_preparer_configured.vm">Velocity: Test Insert Configured Definition with Preparer configured in the definition itself</a><br/>
    <a href="velocity/testinsertdefinition_classpath.vm">Velocity: Test Insert Configured Classpath Definition</a><br/>
    <a href="velocity/testinsertdefinition_override.vm">Velocity: Test Insert Configured Definition with an overridden content</a><br/>
    <a href="velocity/testinsertdefinition_override_and_not.vm">Velocity: Test Insert Configured Definition with an overridden content and one with original content</a><br/>
    <a href="velocity/testinsertdefinition_inline.vm">Velocity: Test Insert Configured Definition with an inline content</a><br/>
    <a href="velocity/testinsertdefinition_composite.vm">Velocity: Test Insert Configured Definition that contains another definition inside</a><br/>
    <a href="velocity/testinsertdefinition_exception.vm">Velocity: Test Insert Configured Definition with an exception in an attribute page</a><br/>
    <a href="velocity/testinsertdefinition_openbody.vm">Velocity: Test Insert Configured Definition with Open Body</a><br/>
    <a href="velocity/testput.vm">Velocity: Test Put Tag</a><br/>
    <a href="velocity/testput_flush.vm">Velocity: Test Put Tag with Flush</a><br/>
    <a href="velocity/testput_el.vm">Velocity: Test Put Tag using EL</a><br/>
    <a href="velocity/testput_servlet.vm">Velocity: Test Put Tag using a servlet mapping as a template</a><br/>
    <a href="velocity/testputlist.vm">Velocity: Test Put List Tag</a><br/>
    <a href="velocity/testimportattribute.vm">Velocity: Test importAttribute Tag</a><br/>
    <a href="velocity/testimportattribute_all.vm">Velocity: Test importAttribute Tag with no name</a><br/>
    <a href="velocity/testdecorationfilter.vm">Velocity: Test Tiles Definition Filter</a><br/>
    <a href="velocity.testdispatchservlet.tiles">Velocity: Test Tiles Dispatch Servlet</a><br/>
    <a href="velocity/selectlocale.vm">Velocity: Test Localization</a><br/>

    <h3>Mutable Container Tests</h3>
    <a href="velocity/testdef.vm">Velocity: Test Definition Tag</a><br/>
    <a href="velocity/testdef_extend.vm">Velocity: Test Definition Tag extending configured and custom definitions</a><br/>
    <a href="velocity/testdef_preparer.vm">Velocity: Test Definition Tag with Preparer</a><br/>
    <a href="velocity/testinsertdefinition_composite_tags_includes_configured.vm">Velocity: Test Insert Definition that contains another definition inside (configured via tiles-defs.xml) using JSP tags</a><br/>
    <a href="velocity/testinsertdefinition_composite_tags.vm">Velocity: Test Insert Definition that contains another definition inside using JSP tags</a><br/>
    <a href="velocity/testinsertdefinition_composite_tags_includes_configured_notype.vm">Velocity: Test Insert Definition that contains another definition inside (configured via tiles-defs.xml) using JSP tags without types</a><br/>
    <a href="velocity/testinsertdefinition_composite_tags_notype.vm">Velocity: Test Insert Definition that contains another definition inside using JSP tags without types</a><br/>

    <h3>Roles Verification tests</h3>
    <a href="velocity/testinsertdefinition_role.vm">Velocity: Test Insert Configured Definition with Specified Role</a><br/>
    <a href="velocity/testinsertdefinition_role_tag.vm">Velocity: Test Insert Configured Definition with Specified Role in Tag</a><br/>
    <a href="velocity/testinsertdefinition_attribute_roles.vm">Velocity: Test Insert Configured Definition with Attribute that have Roles</a><br/>
    <a href="velocity/testinsertdefinition_attribute_roles_tags.vm">Velocity: Test Insert Configured Definition with Attribute that have Roles in Tags</a><br/>

    <h2>Features in Tiles 2.1.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="velocity/testinsertdefinition_override_template.vm">Velocity: Test Insert Configured Definition with an overridden template</a><br/>
    <a href="velocity/testinsertdefinition_old.vm">Velocity: Test Insert Configured Definition in Old Format</a><br/>
    <a href="velocity/testinsertdefinition_cascaded.vm">Velocity: Test Insert Configured Cascaded Definition</a><br/>
    <a href="velocity/testinsertdefinition_cascaded_overridden.vm">Velocity: Test Insert Configured Cascaded Definition with Override</a><br/>
    <a href="velocity/testinsertdefinition_cascaded_template.vm">Velocity: Test Insert Configured Cascaded Definition with Template</a><br/>
    <a href="velocity/testinsertdefinition_cascaded_list.vm">Velocity: Test Insert Configured Cascaded Definition with List</a><br/>
    <a href="velocity/testinsertdefinition_reversed.vm">Velocity: Test Insert Configured Definition with Reversed Attribute</a><br/>
    <a href="velocity/testinsertdefinition_attribute_preparer.vm">Velocity: Test Insert Configured Definition with Attribute Preparer</a><br/>
    <a href="velocity/testinsertnesteddefinition.vm">Velocity: Test Insert Nested Definition</a><br/>
    <a href="velocity/testinsertnesteddefinition_tags.vm">Velocity: Test Insert Nested Definition only using JSP tags</a><br/>
    <a href="velocity/testinsertnestedlistdefinition.vm">Velocity: Test Insert Nested List Definition</a><br/>
    <a href="velocity/testinsertnestedlistdefinition_tags.vm">Velocity: Test Insert Nested List Definition only using JSP tags</a><br/>
    <a href="velocity/testinsertdefinition_el.vm">Velocity: Test Insert Configured Definition with EL</a><br/>
    <a href="velocity/testinsertdefinition_el_singleeval.vm">Velocity: Test Insert Configured Definition with EL to test Single Evaluation</a><br/>
    <a href="velocity/testinsertdefinition_wildcard.vm">Velocity: Test Insert Configured Definition with Wildcards</a><br/>
    <a href="velocity/testinsertdefinition_defaultvalues.vm">Velocity: Test Insert Configured Definition with Default Values</a><br/>
    <a href="velocity/testput_cascaded.vm">Velocity: Test Put Tag with Cascaded Attributes</a><br/>
    <a href="velocity/testput_cascaded_overridden.vm">Velocity: Test Put Tag with Overridden Cascaded Attributes</a><br/>
    <a href="velocity/testput_cascaded_template.vm">Velocity: Test Put Tag with Cascaded Attributes and Template</a><br/>
    <a href="velocity/testput_el_singleeval.vm">Velocity: Test Put Tag using EL to test Single Evaluation</a><br/>
    <a href="velocity/testput_reversed.vm">Velocity: Test Put Tag with Reversed Attribute</a><br/>
    <a href="velocity/testputlist_cascaded.vm">Velocity: Test Put List Cascaded Tag</a><br/>
    <a href="velocity/testputlist_inherit.vm">Velocity: Test Put List Tag with Inherit</a><br/>
    <a href="velocity/testimportattribute_inherit.vm">Velocity: Test importAttribute Tag with List Inherit</a><br/>
    <a href="velocity/testsetcurrentcontainer.vm">Velocity: Test setCurrentContainer Tag</a><br/>

    <h3>Mutable Container Tests</h3>
    <a href="velocity/testdef_list_inherit.vm">Velocity: Test Definition Tag with a List Inherit</a><br/>

    <h3>Database Verification tests</h3>
    <a href="velocity/testinsertdefinition_db.vm">Velocity: Test Insert Configured Definition from DB</a><br/>
    <a href="velocity/testinsertdefinition_extended_db.vm">Velocity: Test Insert Extended Configured Definition from DB</a><br/>

    <h2>Features in Tiles 2.2.x</h2>

    <h3>Standard Render/Attribute Tests</h3>
    <a href="velocity/testinsertdefinition_mvel.vm">Velocity: Test Insert Configured Definition with MVEL</a><br/>
    <a href="velocity/testinsertdefinition_ognl.vm">Velocity: Test Insert Configured Definition with OGNL</a><br/>
    <a href="velocity/testinsertdefinition_regexp.vm">Velocity: Test Insert Configured Definition with Regular Expression</a><br/>
    <a href="org/apache/tiles/test/alt/velocity/testinsertdefinition_alt.vm">Velocity: Test Insert Configured Definition in Module</a><br/>

    <h2>Features in Tiles 3.0.x</h2>

    <h3>TILES-571</h3>
    <a href="velocity/testunderscores_nolocale.vm">Velocity: Test underscores without localization</a>

</html>