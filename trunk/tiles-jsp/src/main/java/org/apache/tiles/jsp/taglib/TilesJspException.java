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

import javax.servlet.jsp.JspException;

/**
 * Indicates that something went wrong during the use of Tiles in JSP pages.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class TilesJspException extends JspException {

    /**
     * Constructor.
     *
     * @since 2.1.0
     */
    public TilesJspException() {
    }

    /**
     * Constructor.
     *
     * @param msg The detail message.
     * @since 2.1.0
     */
    public TilesJspException(String msg) {
        super(msg);
    }

    /**
     * Constructor.
     *
     * @param rootCause The root cause of the exception.
     * @since 2.1.0
     */
    public TilesJspException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Constructor.
     *
     * @param message The detail message.
     * @param rootCause The root cause of the exception.
     * @since 2.1.0
     */
    public TilesJspException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
