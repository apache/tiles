/*
 * $Id: ClassUtil.java 637434 2008-03-15 15:48:38Z apetrelli $
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
package org.apache.tiles.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Utilities to work with dynamic class loading and instantiation.
 *
 * @version $Rev: 637434 $ $Date: 2008-03-15 16:48:38 +0100 (sab, 15 mar 2008) $
 * @since 2.0.7
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
     * @throws CannotInstantiateObjectException If something goes wrong during
     * instantiation.
     * @since 2.0.7
     */
    public static Object instantiate(String className) {
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
     * @throws CannotInstantiateObjectException If something goes wrong during instantiation.
     * @since 2.0.7
     */
    public static Object instantiate(String className, boolean returnNull) {
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
            throw new CannotInstantiateObjectException(
                    "Unable to resolve factory class: '" + className + "'", e);
        } catch (IllegalAccessException e) {
            throw new CannotInstantiateObjectException(
                    "Unable to access factory class: '" + className + "'", e);
        } catch (InstantiationException e) {
            throw new CannotInstantiateObjectException(
                    "Unable to instantiate factory class: '"
                            + className
                            + "'. Make sure that this class has a default constructor",
                    e);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    /**
     * Gets a method and forces it to be accessible, even if it is not.
     *
     * @param clazz The class from which the method will be got.
     * @param methodName The name of the method.
     * @param parameterTypes The parameter types that the method must match.
     * @return The method, if it is found.
     * @since 2.0.7
     */
    public static Method getForcedAccessibleMethod(Class<?> clazz,
            String methodName, Class<?>... parameterTypes) {
        Method method;
        try {
            method = clazz.getMethod(methodName, parameterTypes);
        } catch (SecurityException e) {
            throw new CannotAccessMethodException("Cannot access method '"
                    + methodName + "' in class '" + clazz.getName()
                    + "' for security reasons", e);
        } catch (NoSuchMethodException e) {
            throw new CannotAccessMethodException("The method '"
                    + methodName + "' in class '" + clazz.getName()
                    + "' does not exist", e);
        }
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method;
    }

    /**
     * Invokes a method, masking with a runtime exception all the exceptions.
     *
     * @param obj The object from which a method will be called.
     * @param method The method to call.
     * @param args The arguments of the method.
     * @return The object returned, if the method is not "void".
     * @since 2.0.7
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (IllegalArgumentException e) {
            throw new CannotAccessMethodException("The arguments for '"
                    + method.getName() + "' in class '"
                    + obj.getClass().getName() + "' are not valid", e);
        } catch (IllegalAccessException e) {
            throw new CannotAccessMethodException("Cannot access '"
                    + method.getName() + "' in class '"
                    + obj.getClass().getName() + "'", e);
        } catch (InvocationTargetException e) {
            throw new CannotAccessMethodException(
                    "An exception has been thrown inside '" + method.getName()
                    + "' in class '" + obj.getClass().getName() + "'", e);
        }
    }
}
