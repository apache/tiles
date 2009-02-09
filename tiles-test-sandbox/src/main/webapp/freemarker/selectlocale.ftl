<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<#--
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Select your locale</title>
</head>
<body>
<form action="servlets/selectLocaleServlet">
Select your locale:
<select name="locale">
<option selected="selected" value="">Default</option>
<option value="en_US">American English</option>
<option value="en_GB">British English</option>
<option value="fr_FR">French</option>
<option value="it_IT">Italian</option>
</select>
<input type="submit" value="Submit" />
</form>
<div id="defaultLocaleMessage">Your default Locale is ${.locale}</div>
</body>
</html>
