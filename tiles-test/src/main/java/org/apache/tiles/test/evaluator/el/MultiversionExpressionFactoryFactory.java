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

package org.apache.tiles.test.evaluator.el;

import javax.el.ExpressionFactory;
import javax.servlet.jsp.JspFactory;

import org.apache.el.ExpressionFactoryImpl;
import org.apache.tiles.evaluator.el.JspExpressionFactoryFactory;

/**
 * Tries to use JSP 2.1 expression factory first, then Tomcat one.
 *
 * @version $Rev$ $Date$
 */
public class MultiversionExpressionFactoryFactory extends
        JspExpressionFactoryFactory {

    /**
     * Minimum servlet version major number.
     */
    private static final int MINIMUM_SERVLET_VERSION_MAJOR = 2;

    /**
     * Minimum servlet version minor number.
     */
    private static final int MINIMUM_SERVLET_VERSION_MINOR = 5;

    /** {@inheritDoc} */
    @Override
    public ExpressionFactory getExpressionFactory() {
        ExpressionFactory efFactory;
        if (servletContext.getMajorVersion() > MINIMUM_SERVLET_VERSION_MAJOR
                || (servletContext.getMajorVersion() == MINIMUM_SERVLET_VERSION_MAJOR && servletContext
                        .getMinorVersion() >= MINIMUM_SERVLET_VERSION_MINOR)) {
            efFactory = JspFactory.getDefaultFactory().getJspApplicationContext(
                    servletContext).getExpressionFactory();
        } else {
            efFactory = new ExpressionFactoryImpl();
        }

        return efFactory;
    }

}
