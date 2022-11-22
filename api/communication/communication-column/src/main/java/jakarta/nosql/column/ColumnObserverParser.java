/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package jakarta.nosql.column;

/**
 * An observer to a parser; this observer allows checking both the name of an entity and the fields.
 * This observer might be used to the mapper process.
 */
public interface ColumnObserverParser {

    ColumnObserverParser EMPTY = new ColumnObserverParser() {
    };

    /**
     * Fire an event to entity name
     *
     * @param entity the entity
     * @return the field result
     * @throws NullPointerException when the entity is null
     */
    default String fireEntity(String entity) {
        return entity;
    }

    /**
     * Fire an event to each field in case of mapper process
     *
     * @param field  the field
     * @param entity the entity
     * @return the field result
     * @throws NullPointerException when there is null parameter
     */
    default String fireField(String entity, String field) {
        return field;
    }

}