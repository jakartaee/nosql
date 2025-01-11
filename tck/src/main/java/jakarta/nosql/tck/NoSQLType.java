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
package jakarta.nosql.tck;

import java.util.logging.Logger;

public enum NoSQLType {
    KEY_VALUE(1), COLUMN(2), DOCUMENT(3), GRAPH(4), OTHER(0);

    public static final String DATABASE_TYPE_PROPERTY = "jakarta.nosql.database.type";
    public static final String DEFAULT_DATABASE_TYPE = "KEY_VALUE";

    private static final Logger LOGGER = Logger.getLogger(NoSQLType.class.getName());

    private final int flexibility;


    NoSQLType(int flexibility) {
        this.flexibility = flexibility;
    }

    public int getFlexibility() {
        return flexibility;
    }

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
