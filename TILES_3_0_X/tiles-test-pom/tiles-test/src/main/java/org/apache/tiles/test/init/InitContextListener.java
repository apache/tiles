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

package org.apache.tiles.test.init;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.tiles.test.exception.TilesTestRuntimeException;
import org.hsqldb.jdbc.jdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes the data source of the DB.
 *
 * @version $Rev$ $Date$
 */
public class InitContextListener implements ServletContextListener {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory
            .getLogger(InitContextListener.class);

    /** {@inheritDoc} */
    public void contextInitialized(ServletContextEvent event) {
        DataSource dataSource = createDataSource();
        event.getServletContext().setAttribute("dataSource", dataSource);
        String[] commands = getSQLCommands();
        executeCommands(dataSource, commands);
    }

    /** {@inheritDoc} */
    public void contextDestroyed(ServletContextEvent event) {
        // Do nothing
    }

    /**
     * Creates the data source to use.
     *
     * @return The data source.
     */
    private DataSource createDataSource() {
        jdbcDataSource dataSource = new jdbcDataSource();
        dataSource.setDatabase("jdbc:hsqldb:mem:tiles");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    /**
     * Loads and returns the SQL commands to initialize the DB.
     *
     * @return The SQL commands to execute.
     */
    private String[] getSQLCommands() {
        InputStream stream = getClass().getResourceAsStream(
                "/org/apache/tiles/test/db/schema.sql");
        String text;
        try {
            text = IOUtils.toString(stream);
        } catch (IOException e) {
            throw new TilesTestRuntimeException("Cannot read schema SQL text", e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("Error during close of the stream containing the SQL schema", e);
            }
        }
        return text.split(";");
    }

    /**
     * Execute SQL commands.
     *
     * @param dataSource The data source to use.
     * @param commands The commands to execute.
     */
    private void executeCommands(DataSource dataSource, String[] commands) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            for (int i = 0; i < commands.length; i++) {
                stmt = conn.createStatement();
                stmt.executeUpdate(commands[i]);
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                log.error("Error during rollback", e);
            }
            throw new TilesTestRuntimeException("Error during execution of SQL commands", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                log.error("Error during closing resources", e);
            }
        }
    }
}
