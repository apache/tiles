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

package org.apache.tiles.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>General purpose utility methods related to processing a servlet request
 * with Tiles.</p>
 *
 * @version $Rev$ $Date$
 */
public class RequestUtils {

    // ------------------------------------------------------- Static Variables


    /**
     * <p>Commons Logging instance.</p>
     */
    protected static Log log = LogFactory.getLog(RequestUtils.class);

    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create and return an absolute URL for the specified context-relative
     * path, based on the server and context information in the specified
     * request.</p>
     *
     * @param request The servlet request we are processing
     * @param path    The context-relative path (must start with '/')
     * @return absolute URL based on context-relative path
     * @throws MalformedURLException if we cannot create an absolute URL
     */
    public static URL absoluteURL(HttpServletRequest request, String path)
        throws MalformedURLException {

        return (new URL(serverURL(request), request.getContextPath() + path));

    }


    /**
     * <p>Return the <code>Class</code> object for the specified fully qualified
     * class name, from this web application's class loader.</p>
     *
     * @param className Fully qualified class name to be loaded
     * @return Class object
     * @throws ClassNotFoundException if the class cannot be found
     */
    public static Class applicationClass(String className) throws ClassNotFoundException {
        return applicationClass(className, null);
    }

    /**
     * <p>Return the <code>Class</code> object for the specified fully qualified
     * class name, from this web application's class loader.</p>
     *
     * @param className   Fully qualified class name to be loaded
     * @param classLoader The desired classloader to use
     * @return Class object
     * @throws ClassNotFoundException if the class cannot be found
     */
    public static Class applicationClass(String className, ClassLoader classLoader)
        throws ClassNotFoundException {

        if (classLoader == null) {
            // Look up the class loader to be used
            classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = RequestUtils.class.getClassLoader();
            }
        }

        // Attempt to load the specified class
        return (classLoader.loadClass(className));

    }


    /**
     * <p>Return a new instance of the specified fully qualified class name,
     * after loading the class from this web application's class loader.
     * The specified class <strong>MUST</strong> have a public zero-arguments
     * constructor.</p>
     *
     * @param className Fully qualified class name to use
     * @return new instance of class
     * @throws ClassNotFoundException if the class cannot be found
     * @throws IllegalAccessException if the class or its constructor
     *                                is not accessible
     * @throws InstantiationException if this class represents an
     *                                abstract class, an interface, an array class, a primitive type,
     *                                or void
     * @throws InstantiationException if this class has no
     *                                zero-arguments constructor
     */
    public static Object applicationInstance(String className)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        return applicationInstance(className, null);
    }

    /**
     * <p>Return a new instance of the specified fully qualified class name,
     * after loading the class from this web application's class loader.
     * The specified class <strong>MUST</strong> have a public zero-arguments
     * constructor.</p>
     *
     * @param className   Fully qualified class name to use
     * @param classLoader The desired classloader to use
     * @return new instance of class
     * @throws ClassNotFoundException if the class cannot be found
     * @throws IllegalAccessException if the class or its constructor
     *                                is not accessible
     * @throws InstantiationException if this class represents an
     *                                abstract class, an interface, an array class, a primitive type,
     *                                or void
     * @throws InstantiationException if this class has no
     *                                zero-arguments constructor
     */
    public static Object applicationInstance(String className, ClassLoader classLoader)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        return (applicationClass(className, classLoader).newInstance());

    }

    /**
     * <p>Compute the printable representation of a URL, leaving off the
     * scheme/host/port part if no host is specified. This will typically
     * be the case for URLs that were originally created from relative
     * or context-relative URIs.</p>
     *
     * @param url URL to render in a printable representation
     * @return printable representation of a URL
     */
    public static String printableURL(URL url) {

        if (url.getHost() != null) {
            return (url.toString());
        }

        String file = url.getFile();
        String ref = url.getRef();
        if (ref == null) {
            return (file);
        } else {
            StringBuffer sb = new StringBuffer(file);
            sb.append('#');
            sb.append(ref);
            return (sb.toString());
        }

    }


    /**
     * <p>Return the URL representing the current request. This is equivalent
     * to <code>HttpServletRequest.getRequestURL</code> in Servlet 2.3.</p>
     *
     * @param request The servlet request we are processing
     * @return URL representing the current request
     * @throws MalformedURLException if a URL cannot be created
     */
    public static URL requestURL(HttpServletRequest request) throws MalformedURLException {

        StringBuffer url = requestToServerUriStringBuffer(request);
        return (new URL(url.toString()));

    }


    /**
     * <p>Return the URL representing the scheme, server, and port number of
     * the current request. Server-relative URLs can be created by simply
     * appending the server-relative path (starting with '/') to this.</p>
     *
     * @param request The servlet request we are processing
     * @return URL representing the scheme, server, and port number of
     *         the current request
     * @throws MalformedURLException if a URL cannot be created
     */
    public static URL serverURL(HttpServletRequest request) throws MalformedURLException {

        StringBuffer url = requestToServerStringBuffer(request);
        return (new URL(url.toString()));

    }


    /**
     * <p>Return the string representing the scheme, server, and port number of
     * the current request. Server-relative URLs can be created by simply
     * appending the server-relative path (starting with '/') to this.</p>
     *
     * @param request The servlet request we are processing
     * @return URL representing the scheme, server, and port number of
     *         the current request
     * @since Struts 1.2.0
     */
    public static StringBuffer requestToServerUriStringBuffer(HttpServletRequest request) {

        StringBuffer serverUri = createServerUriStringBuffer(request.getScheme(), request.getServerName(),
            request.getServerPort(), request.getRequestURI());
        return serverUri;

    }

    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server, and port number of
     * the current request. Server-relative URLs can be created by simply
     * appending the server-relative path (starting with '/') to this.</p>
     *
     * @param request The servlet request we are processing
     * @return URL representing the scheme, server, and port number of
     *         the current request
     * @since Struts 1.2.0
     */
    public static StringBuffer requestToServerStringBuffer(HttpServletRequest request) {

        return createServerStringBuffer(request.getScheme(), request.getServerName(), request.getServerPort());

    }


    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server, and port number of
     * the current request.</p>
     *
     * @param scheme The scheme name to use
     * @param server The server name to use
     * @param port   The port value to use
     * @return StringBuffer in the form scheme: server: port
     * @since Struts 1.2.0
     */
    public static StringBuffer createServerStringBuffer(String scheme, String server, int port) {

        StringBuffer url = new StringBuffer();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        url.append(scheme);
        url.append("://");
        url.append(server);
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        return url;

    }


    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server, and port number of
     * the current request.</p>
     *
     * @param scheme The scheme name to use
     * @param server The server name to use
     * @param port   The port value to use
     * @param uri    The uri value to use
     * @return StringBuffer in the form scheme: server: port
     * @since Struts 1.2.0
     */
    public static StringBuffer createServerUriStringBuffer(String scheme, String server, int port, String uri) {

        StringBuffer serverUri = createServerStringBuffer(scheme, server, port);
        serverUri.append(uri);
        return serverUri;

    }
}
