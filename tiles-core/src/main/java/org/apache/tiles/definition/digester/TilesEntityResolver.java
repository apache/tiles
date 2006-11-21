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
 *
 */
package org.apache.tiles.definition.digester;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;

/**
 * Resource resolved used to provide local/classpath access
 * to the tiles dtd.  DefinitionsReaders which validate xml
 * should utilize this resolver to reduce requests to
 * struts.apache.org.
 *
 * @since Tiles 2.0
 * @version $Rev$
 *
 */
class TilesEntityResolver implements EntityResolver {

    public static final String SYSTEM_ID =
        "http://struts.apache.org/dtds/tiles-config_2_0.dtd";

    public static final String PUBLIC_ID =
        "-//Apache Software Foundation//DTD Tiles Configuration 2.0//EN";

    private static TilesEntityResolver instance = new TilesEntityResolver();

    public static TilesEntityResolver getInstance() {
        return instance;
    }

    private URL tilesDtd;

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if(PUBLIC_ID.equals(publicId) || SYSTEM_ID.equals(systemId)) {
            InputSource source = new InputSource();
            source.setSystemId(systemId);
            source.setPublicId(publicId);
            source.setByteStream(getTilesDTD().openStream());
            return source;
        }
        return null;
    }

    public URL getTilesDTD() {
        if(tilesDtd == null) {
            tilesDtd = getClass().getResource("/org/apache/tiles/resources/tiles-config_2_0.dtd");
        }
        return tilesDtd;
    }
}
