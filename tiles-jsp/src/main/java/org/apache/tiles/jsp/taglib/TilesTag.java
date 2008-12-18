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

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * An abstract common base class to extend for all Tiles JSP tag handlers which
 * do not access body content. Defines useful life cycle extension points.
 *
 * @version $Rev$ $Date$
 * @since 2.1.1
 */
public abstract class TilesTag extends TagSupport implements TryCatchFinally {

    /**
     * Default no-op implementation, but overrideable if needed.
     *
     * @param throwable The throwable object.
     * @throws Throwable The throwable object itself, by default.
     * @see TryCatchFinally#doCatch(Throwable)
     */
    public void doCatch(Throwable throwable) throws Throwable {
        throw throwable;
    }

    /**
     * Called after doEndTag(). This common implementation calls reset() to
     * release any per-invocation resources.
     *
     * @see TryCatchFinally#doFinally()
     */
    public void doFinally() {
        reset();
    }

    /**
     * Release any per-invocation resources, resetting any resources or state
     * that should be cleared between successive invocations of
     * {@link javax.servlet.jsp.tagext.Tag#doEndTag()} and
     * {@link javax.servlet.jsp.tagext.Tag#doStartTag()}.
     */
    protected void reset() {
    }

    /**
     * Release any per-instance resources, releasing any resources or state
     * before this tag instance is disposed.
     *
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        super.release();
        reset();
    }
}
