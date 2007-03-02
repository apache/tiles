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
package org.apache.tiles.locale.impl;

import java.util.Locale;
import java.util.Map;

import org.apache.tiles.TilesException;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.taglib.ComponentConstants;

/**
 * @version $Rev$ $Date$
 */
public class DefaultLocaleResolver implements LocaleResolver {

    public void init(Map<String, String> params)
            throws TilesException {
        // Does nothing.
    }

    public Locale resolveLocale(TilesRequestContext request) {
        Locale retValue = null;
        Map session = request.getSessionScope();
        if (session != null) {
            retValue = (Locale) session.get(ComponentConstants.LOCALE_KEY);
        }
        if (retValue == null) {
            retValue = request.getRequestLocale();
        }
        
        return retValue;
    }
}
