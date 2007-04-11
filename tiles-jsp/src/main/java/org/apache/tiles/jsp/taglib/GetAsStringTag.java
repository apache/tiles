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
 */
package org.apache.tiles.jsp.taglib;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesException;

import java.io.IOException;

/**
 * Retrieve the value of the specified definition/template attribute property,
 * and render it to the current JspWriter as a String.
 * The usual toString() conversion is applied on the found value.
 *
 * @version $Rev$ $Date$
 */
public class GetAsStringTag extends InsertAttributeTag {

    /** {@inheritDoc} */
    @Override
    protected void render(Attribute attr)
        throws TilesException, IOException {
        pageContext.getOut().print(attr.getValue().toString());
    }
}
