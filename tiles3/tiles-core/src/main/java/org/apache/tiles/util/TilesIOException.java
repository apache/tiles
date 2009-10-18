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
package org.apache.tiles.util;

import java.io.IOException;

/**
 * This exception mimics the {@link IOException} class that is present in Java 6
 * but not in Java 5. It contains the same number of constructors.
 *
 * @version $Rev$ $Date$
 * @since 2.0.6
 */
public class TilesIOException extends IOException {

    /**
     * Default constructor.
     * @since 2.0.6
     */
    public TilesIOException() {
    }

    /**
     * Constructor.
     *
     * @param message Message of the exception.
     * @since 2.0.6
     */
    public TilesIOException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The cause of the exception.
     * @since 2.0.6
     */
    public TilesIOException(Throwable cause) {
        super();
        this.initCause(cause);
    }

    /**
     * Constructor.
     *
     * @param message Message of the exception.
     * @param cause The cause of the exception.
     * @since 2.0.6
     */
    public TilesIOException(String message, Throwable cause) {
        super(message);
        this.initCause(cause);
    }
}
