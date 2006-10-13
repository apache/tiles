/*
 * $Id$ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
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

package org.apache.tiles;

import java.io.IOException;

/**
 * Tiles preparer including a local URL.
 */
public class UrlViewPreparer implements ViewPreparer {

    /** 
     * URL associated with this preparer. 
     */
    protected String url = null;

    /**
     * Constructor.
     * @param url URL.
     */
    public UrlViewPreparer(String url) {
            this.url = url;
    }

    /**
     * @see org.apache.tiles.ViewPreparer#execute(org.apache.tiles.TilesContext, org.apache.tiles.ComponentContext)
     */
    public void execute(TilesContext tilesContext, 
            ComponentContext componentContext) 
            throws Exception {
        
        tilesContext.include(url);
    }

}
