/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.nosql;

import java.util.logging.Logger;

/**
 * Enum representing various types of NoSQL databases supported by Jakarta NoSQL.
 * Each type is associated with a flexibility level, which indicates the database's
 * capability to perform queries beyond basic operations.
 * Flexibility Level:
 * The flexibility level reflects the ability of a NoSQL database type to retrieve
 * and query information beyond the key itself:
 * <ul>
 *     <li><b>KEY_VALUE (1):</b> Key-value stores generally support queries using only the key,
 *     resulting in lower flexibility.</li>
 *     <li><b>COLUMN (2):</b> Column-family databases offer more advanced querying capabilities,
 *     such as filtering rows based on column values, providing moderate flexibility.</li>
 *     <li><b>DOCUMENT (3):</b> Document-based databases support rich querying capabilities
 *     over nested structures and fields within documents, leading to higher flexibility.</li>
 *     <li><b>GRAPH (4):</b> Graph databases excel in querying relationships, relationship
 *     directions, and properties, making them the most flexible type among NoSQL databases.</li>
 *     <li><b>OTHER (0):</b> Represents database types that do not fit into the other categories
 *     or have unknown flexibility.</li>
 * </ul>
 */
public enum NoSQLType {
    /**
     * Key-value stores are databases optimized for simple key-based lookups.
     * These databases have limited flexibility because they typically cannot
     * perform advanced queries beyond retrieving values by keys.
     */
    KEY_VALUE(1),

    /**
     * Column-family databases organize data into rows and columns and allow
     * advanced filtering and operations at the column level. These databases
     * offer moderate flexibility due to their ability to query based on column
     * values.
     */
    COLUMN(2),

    /**
     * Document-based databases store data in structured documents. These databases
     * enable rich queries over nested structures and fields, providing a high degree
     * of flexibility for complex queries.
     */
    DOCUMENT(3),

    /**
     * Graph databases specialize in querying relationships and properties between
     * entities. These databases offer the highest flexibility due to their ability
     * to perform complex queries on nodes, edges, relationships, directions, and
     * properties.
     */
    GRAPH(4),

    /**
     * Represents database types that do not fall into the predefined categories or
     * have an unknown level of flexibility. This category is used for custom or
     * less common database types.
     */
    OTHER(0);

    /**
     * The system property used to define the NoSQL database type.
     */
    public static final String DATABASE_TYPE_PROPERTY = "jakarta.nosql.database.type";

    /**
     * The default database type, used when no type is explicitly specified.
     */
    public static final String DEFAULT_DATABASE_TYPE = "KEY_VALUE";

    private static final Logger LOGGER = Logger.getLogger(NoSQLType.class.getName());

    private final int flexibility;

    /**
     * Constructor to initialize the flexibility level of each database type.
     *
     * @param flexibility the flexibility level of the NoSQL database type
     */
    NoSQLType(int flexibility) {
        this.flexibility = flexibility;
    }

    /**
     * Returns the flexibility level of the current database type.
     * The flexibility score is determined based on the type's capability to
     * perform queries beyond simple key-based lookups. For example:
     * <ul>
     *     <li>Key-value stores typically only allow key-based lookups, resulting
     *     in a lower flexibility score.</li>
     *     <li>Graph databases enable complex queries involving relationships,
     *     directions, and properties, resulting in the highest flexibility score.</li>
     * </ul>
     *
     * @return the flexibility level
     */
    public int getFlexibility() {
        return flexibility;
    }

    /**
     * Retrieves the NoSQLType from the system property {@code DATABASE_TYPE_PROPERTY}.
     * If the property is not set, the {@code DEFAULT_DATABASE_TYPE} is used.
     *
     * @return the NoSQLType corresponding to the system property or default type
     */
    public static NoSQLType get() {
        String type = System.getProperty(DATABASE_TYPE_PROPERTY);
        LOGGER.info("Getting the NoSQL type: " + type + " from the system property: " + DATABASE_TYPE_PROPERTY);
        if (type == null) {
            LOGGER.info("No NoSQL type found in the system property: " + DATABASE_TYPE_PROPERTY + ". Using the default type: " + DEFAULT_DATABASE_TYPE);
            type = DEFAULT_DATABASE_TYPE;
            return get(type);
        }
        return get(type);
    }

    /**
     * Retrieves the NoSQLType based on the given type name.
     * If the type name is invalid, the {@code DEFAULT_DATABASE_TYPE} is used.
     *
     * @param type the name of the NoSQL type
     * @return the corresponding NoSQLType, or the default type if invalid
     */
    public static NoSQLType get(String type) {
        LOGGER.info("Getting the NoSQL type: " + type);
        try {
            return NoSQLType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.warning("No NoSQL type found for: " + type + ". Using the default type: " + DEFAULT_DATABASE_TYPE);
            return NoSQLType.valueOf(DEFAULT_DATABASE_TYPE);
        }
    }
}
