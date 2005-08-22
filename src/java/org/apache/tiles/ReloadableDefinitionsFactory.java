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

/**
 * Indicates support for reloading Tiles configuration when it changes.
 *
 * @version $Rev$ $Date$ 
 */
public interface ReloadableDefinitionsFactory {
    
    /**
     * Indicates whether the DefinitionsFactory is out of date and needs to be
     * reloaded.
     */
    public boolean refreshRequired();
}
