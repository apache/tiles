/*
 * $Id$
 *
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tiles.preparer;

import org.apache.tiles.ViewPreparer;
import org.apache.tiles.TilesRequestContext;

/**
 * Factory interface used to create/retrieve instances of
 * the {@link ViewPreparer} interface.
 *
 * This factory provides an extension point into the default
 * tiles implementation. Implementors wishing to provide
 * per request initialization of the ViewPreparer (for instance)
 * may provide a custom prerparer.
 *
 * @since 2.0
 * @verion $Id$
 */
public interface PreparerFactory {

    /**
     * Create the named {link ViewPreparer} for the specified context.
     *
     * @param name ViewPreparer name, commonly the qualified classname.
     * @param context the context within which the preparer will be invoked.
     * @return instance of the ViewPreparer
     * @throws NoSuchPreparerException when the named preparer can not be found.
     */
    ViewPreparer getPreparer(String name, TilesRequestContext context)
        throws NoSuchPreparerException;


}
