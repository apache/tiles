package org.apache.tiles;

import java.util.Map;
import java.util.Locale;
import java.io.IOException;

/**
 * Tiles Request processing Context.
 *
 * @version $Id$
 * @since Sep 21, 2006
 */
public interface TilesRequestContext extends TilesApplicationContext {
    /**
     * Return an immutable Map that maps header names to the first (or only)
     * header value (as a String).
     */
    Map getHeader();

    /**
     * Return an immutable Map that maps header names to the set of all values
     * specified in the request (as a String array). Header names must be
     * matched in a case-insensitive manner.
     */
    Map getHeaderValues();

    /**
     * Return a mutable Map that maps request scope attribute names to their
     * values.
     */
    Map getRequestScope();

    /**
     * Return a mutable Map that maps session scope attribute names to their
     * values.
     */
    Map getSessionScope();

    /**
     * Dispatches the request to a specified path.
     */
    void dispatch(String path) throws IOException, Exception;

    /**
     * Includes the response from the specified URL in the current response output.
     */
    void include(String path) throws IOException, Exception;

    /**
     * Return an immutable Map that maps request parameter names to the first
     * (or only) value (as a String).
     */
    Map getParam();

    /**
     * Return an immutable Map that maps request parameter names to the set of
     * all values (as a String array).
     */
    Map getParamValues();

    /**
     * Return the preferred Locale in which the client will accept content.
     */
    Locale getRequestLocale();
}
