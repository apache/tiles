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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

/**
 * Contains hooks to the application execution environment.
 *
 * @version $Rev$ $Date$
 */
public interface TilesContext {
    
    /**
     * Returns a mutable Map that maps application scope attribute names to 
     * their values.
     */
    public Map getApplicationScope();
    
    /**
     * Return an immutable Map that maps header names to the first (or only) 
     * header value (as a String).
     */
    public Map getHeader();
    
    /**
     * Return an immutable Map that maps header names to the set of all values 
     * specified in the request (as a String array). Header names must be 
     * matched in a case-insensitive manner.
     */
    public Map getHeaderValues();
    
    /**
     * Return an immutable Map that maps context application initialization 
     * parameters to their values.
     */
    public Map getInitParams();
    
    /**
     * Return an immutable Map that maps request parameter names to the first 
     * (or only) value (as a String).
     */
    public Map getParam();
    
    /**
     * Return an immutable Map that maps request parameter names to the set of 
     * all values (as a String array).
     */
    public Map getParamValues();
    
    /**
     * Return a mutable Map that maps request scope attribute names to their 
     * values.
     */
    public Map getRequestScope();
    
    /**
     * Return a mutable Map that maps session scope attribute names to their 
     * values.
     */
    public Map getSessionScope();
    
    /**
     * Dispatches the request to a specified path.
     */
    public void dispatch(String path) throws IOException, Exception;
    
    /**
     * Includes the response from the specified URL in the current response output.
     */
    public void include(String path) throws IOException, Exception;
    
    /**
     * Return a URL for the application resource mapped to the specified path.
     */
    public URL getResource(String path) throws MalformedURLException;
    
    /**
     * Return the preferred Locale in which the client will accept content.
     */
    public Locale getRequestLocale();
}
