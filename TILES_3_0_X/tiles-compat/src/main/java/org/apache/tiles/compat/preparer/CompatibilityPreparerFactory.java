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

package org.apache.tiles.compat.preparer;

import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.preparer.factory.BasicPreparerFactory;

/**
 * Factory used to instantiate preparers in a Struts 1 / Tiles 2 environment.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class CompatibilityPreparerFactory extends BasicPreparerFactory {

    /** {@inheritDoc} */
    @Override
    protected ViewPreparer createPreparer(String name) {
        ViewPreparer retValue;

        if (name.startsWith("/")) {
            retValue = new UrlPreparer(name);
        } else {
            retValue = super.createPreparer(name);
        }

        return retValue;
    }

}
