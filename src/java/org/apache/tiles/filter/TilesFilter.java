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

package org.apache.tiles.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.tiles.ComponentDefinitions;
import org.apache.tiles.DefinitionsFactory;
import org.apache.tiles.ReloadableDefinitionsFactory;
import org.apache.tiles.TilesUtil;
import org.apache.tiles.TilesUtilImpl;

/**
 * Processes Reloadable Tiles Definitions.
 *
 * @version $Rev$ $Date$ 
 */

public class TilesFilter implements Filter {
    
    /**
     * The filter configuration object we are associated with.  If
     * this value is null, this filter instance is not currently
     * configured.
     */
    private FilterConfig filterConfig = null;
    
    public TilesFilter() {
    }
    
    /**
     * Checks whether Tiles Definitions need to be reloaded.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        try {
            DefinitionsFactory factory = TilesUtil.getDefinitionsFactory(request, 
                    filterConfig.getServletContext());
            
            if (factory instanceof ReloadableDefinitionsFactory) {
                if (((ReloadableDefinitionsFactory) factory).refreshRequired()) {
                    if (debug) {
                        log("Updating Tiles definitions.");
                    }
                    
                    ComponentDefinitions newDefs = null;
                    synchronized (factory) {
                         newDefs = factory.readDefinitions();
                    }
                    
                    ComponentDefinitions definitions = (ComponentDefinitions)
                            filterConfig.getServletContext().getAttribute(
                            TilesUtilImpl.DEFINITIONS_OBJECT);
                    synchronized (definitions) {
                        definitions.reset();
                        definitions.addDefinitions(newDefs.getBaseDefinitions());
                    }
                }
            }
            
            chain.doFilter(request, response);
            
        } catch(Exception e) {
            throw new ServletException("Error processing request.", e);
        }
    }
    
    
    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }
    
    
    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        
        this.filterConfig = filterConfig;
    }
    
    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }
    
    
    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        
        if (debug) {
            log("TilesFilter:Initializing filter");
        }
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
    
    private static final boolean debug = true;
}
