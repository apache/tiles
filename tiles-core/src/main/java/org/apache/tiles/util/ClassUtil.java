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

import org.apache.tiles.TilesException;


/**
 * Utilities to work with dynamic class loading and instantiation.
 *
 * @version $Rev$ $Date$
 */
public final class ClassUtil {

    /**
     * Constructor, private to avoid instantiation.
     */
    private ClassUtil() {
    }

    /**
     * Returns an instance of the given class name, by calling the default
     * constructor.
     *
     * @param className The class name to load and to instantiate.
     * @return The new instance of the class name.
     * @throws TilesException If something goes wrong during instantiation.
     */
    public static Object instantiate(String className) throws TilesException {
        return instantiate(className, false);
    }

    /**
     * Returns an instance of the given class name, by calling the default
     * constructor.
     *
     * @param className The class name to load and to instantiate.
     * @param returnNull If <code>true</code>, if the class is not found it
     * returns <code>true</code>, otherwise it throws a
     * <code>TilesException</code>.
     * @return The new instance of the class name.
     * @throws TilesException If something goes wrong during instantiation.
     */
    public static Object instantiate(String className, boolean returnNull)
        throws TilesException {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        if (original == null) {
            Thread.currentThread().setContextClassLoader(ClassUtil.class.getClassLoader());
        }
        try {
            Class<?> namedClass = Class.forName(className);
            return namedClass.newInstance();
        } catch (ClassNotFoundException e) {
            if (returnNull) {
                return null;
            }
            throw new TilesException("Unable to resolve factory class: '" + className + "'");
        } catch (IllegalAccessException e) {
            throw new TilesException("Unable to access factory class: '" + className + "'");
        } catch (InstantiationException e) {
            throw new TilesException("Unable to instantiate factory class: '"
                + className + "'. Make sure that this class has a default constructor");
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

}
