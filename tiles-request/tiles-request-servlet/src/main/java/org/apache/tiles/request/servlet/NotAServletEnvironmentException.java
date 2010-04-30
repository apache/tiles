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

package org.apache.tiles.request.servlet;


/**
 * Exception that indicates that a resource could not be used because it is not
 * in a servlet environment.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class NotAServletEnvironmentException extends RuntimeException {

    /**
     * Constructor.
     *
     * @since 2.2.0
     */
    public NotAServletEnvironmentException() {
    }

    /**
     * Constructor.
     *
     * @param message The detail message.
     * @since 2.2.0
     */
    public NotAServletEnvironmentException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param e The exception to be wrapped.
     * @since 2.2.0
     */
    public NotAServletEnvironmentException(Throwable e) {
        super(e);
    }

    /**
     * Constructor.
     *
     * @param message The detail message.
     * @param e The exception to be wrapped.
     * @since 2.2.0
     */
    public NotAServletEnvironmentException(String message, Throwable e) {
        super(message, e);
    }

}
