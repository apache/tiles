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

package org.apache.tiles.test.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.dao.DefinitionDAO;
import org.apache.tiles.request.locale.LocaleUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Stub definition DAO to demonstrate that Tiles definitions can be stored in a
 * Database.
 *
 * @version $Rev$ $Date$
 */
public class LocaleDbDefinitionDAO extends JdbcDaoSupport implements
        DefinitionDAO<Locale> {

    /**
     * Selects a customization by its name.
     */
    private static final String SELECT_CUSTOMIZATION_BY_NAME_SQL =
        "select ID, PARENT_ID, NAME from CUSTOMIZATION "
            + "where NAME = ? ";

    /**
     * Selects a customization by its Id.
     */
    private static final String SELECT_CUSTOMIZATION_BY_ID_SQL =
        "select ID, PARENT_ID, NAME from CUSTOMIZATION "
            + "where ID = ? ";

    /**
     * Selects a definition by its name and a customization Id.
     */
    private static final String SELECT_DEFINITION_SQL =
        "select ID, PARENT_NAME, NAME, PREPARER, TEMPLATE from DEFINITION "
            + "where NAME = ? and CUSTOMIZATION_ID = ? ";

    /**
     * Selects attributes of a definition, given the definition Id.
     */
    private static final String SELECT_ATTRIBUTES_SQL =
        "select ID, NAME, TYPE, VALUE, CASCADE_ATTRIBUTE from ATTRIBUTE "
            + "where DEFINITION_ID = ? ";

    /**
     * Maps a row of a {@link ResultSet} to a {@link Definition}.
     */
    private final DefinitionRowMapper definitionRowMapper = new DefinitionRowMapper();

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Definition getDefinition(String name, Locale locale) {
        List<Map<String, Object>> customizations = null;
        Long customizationId = null, parentCustomizationId = null;
        do {
            customizations = getJdbcTemplate().queryForList(
                    SELECT_CUSTOMIZATION_BY_NAME_SQL,
                    new Object[] { locale.toString() });
            if (!customizations.isEmpty()) {
                Map<String, Object> customization = customizations.get(0);
                customizationId = ((Number) customization.get("ID")).longValue();
                parentCustomizationId = numberToLong((Number) customization.get("PARENT_ID"));
            } else {
                locale = LocaleUtil.getParentLocale(locale);
            }
        } while (customizations.isEmpty());

        return getDefinition(name, customizationId, parentCustomizationId,
                locale);
    }

    /** {@inheritDoc} */
    public Map<String, Definition> getDefinitions(Locale locale) {
        throw new UnsupportedOperationException(
                "Currently the 'getDefinitions' method is not supported");
    }

    /**
     * Loads a definition from the DB.
     *
     * @param name The name of the definition.
     * @param baseCustomizationId The id of the customization item.
     * @param baseParentCustomizationId The id of the parent customization item.
     * @param locale The locale.
     * @return The definition.
     */
    @SuppressWarnings("unchecked")
    protected DbDefinition getDefinition(String name, Long baseCustomizationId,
            Long baseParentCustomizationId, @SuppressWarnings("unused") Locale locale) {
        DbDefinition definition = null;
        Long customizationId = baseCustomizationId;
        Long parentCustomizationId = baseParentCustomizationId;
        List<DbDefinition> definitions = null;
        boolean finished = false;
        do {
            definitions = getJdbcTemplate()
                    .query(SELECT_DEFINITION_SQL,
                            new Object[] { name, customizationId },
                            definitionRowMapper);
            if (definitions.isEmpty()) {
                if (parentCustomizationId != null) {
                    Map<String, Object> customization = getJdbcTemplate().queryForMap(
                            SELECT_CUSTOMIZATION_BY_ID_SQL,
                            new Object[] { parentCustomizationId });
                    customizationId = ((Number) customization.get("ID")).longValue();
                    parentCustomizationId = numberToLong((Number) customization.get("PARENT_ID"));
                } else {
                    finished = true;
                }
            } else {
                definition = definitions.get(0);
                finished = true;
            }
        } while (!finished);

        if (definition != null) {
            AttributeRowMapper attributeRowMapper = new AttributeRowMapper(definition);
            getJdbcTemplate().query(SELECT_ATTRIBUTES_SQL,
                    new Object[] { definition.getId() }, attributeRowMapper);
        }
        return definition;
    }

    /**
     * Returns a {@link Long} object only if the number is not null.
     *
     * @param number The number to convert.
     * @return The number converted into {@link Long} if not null,
     * <code>null</code> otherwise.
     */
    private static Long numberToLong(Number number) {
        Long retValue = null;
        if (number != null) {
            retValue = number.longValue();
        }
        return retValue;
    }

    /**
     * A definition with the new property "id".
     */
    private static class DbDefinition extends Definition {

        /**
         * The id of the definition.
         */
        private Long id;

        /**
         * The default constructor.
         */
        public DbDefinition() {
            super();
        }

        /**
         * Returns the Id of the definition.
         *
         * @return The id.
         */
        public Long getId() {
            return id;
        }

        /**
         * Sets the id of the definition.
         *
         * @param id The id to set
         */
        public void setId(Long id) {
            this.id = id;
        }

    }

    /**
     * Maps a row of a {@link ResultSet} to a {@link Definition}.
     */
    private static final class DefinitionRowMapper implements RowMapper {

        /** {@inheritDoc} */
        public Object mapRow(ResultSet rs, int row) throws SQLException {
            DbDefinition definition = new DbDefinition();
            definition.setId(numberToLong((Number) rs.getObject("ID")));
            definition.setName(rs.getString("NAME"));
            definition.setTemplateAttribute(Attribute
                    .createTemplateAttribute(rs.getString("TEMPLATE")));
            definition.setPreparer(rs.getString("PREPARER"));
            definition.setExtends(rs.getString("PARENT_NAME"));
            return definition;
        }

    }

    /**
     * Maps a row of a {@link ResultSet} to an {@link Attribute}. It stores the
     * attributes directly in their definition.
     */
    private static final class AttributeRowMapper implements RowMapper {

        /**
         * The definition in which the attributes will be stored.
         */
        private Definition definition;

        /**
         * Constructor.
         *
         * @param definition The definition in which the attributes will be
         * stored.
         */
        private AttributeRowMapper(Definition definition) {
            this.definition = definition;
        }

        /** {@inheritDoc} */
        public Object mapRow(ResultSet rs, int row) throws SQLException {
            Attribute attribute = new Attribute();
            attribute.setRenderer(rs.getString("TYPE"));
            attribute.setValue(rs.getString("VALUE"));
            definition.putAttribute(rs.getString("NAME"), attribute, rs
                    .getBoolean("CASCADE_ATTRIBUTE"));
            return attribute;
        }

    }
}
