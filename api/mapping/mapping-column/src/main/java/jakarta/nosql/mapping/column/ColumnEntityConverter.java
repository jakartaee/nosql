/*
 * Copyright (c) 2019 Otavio Santana and others
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
package jakarta.nosql.mapping.column;


import jakarta.nosql.column.ColumnEntity;

/**
 * This interface represents the converter between an entity and the {@link ColumnEntity}
 */
public interface ColumnEntityConverter {

    /**
     * Converts the instance entity to {@link ColumnEntity}
     *
     * @param entity the instance
     * @return a {@link ColumnEntity} instance
     * @throws NullPointerException when entity is null
     */
    ColumnEntity toColumn(Object entity);

    /**
     * Converts a {@link ColumnEntity} to entity
     *
     * @param type the entity class
     * @param entity      the {@link ColumnEntity} to be converted
     * @param <T>         the entity type
     * @return the instance from {@link ColumnEntity}
     * @throws NullPointerException when either type or entity are null
     */
    <T> T toEntity(Class<T> type, ColumnEntity entity);

    /**
     * Converts a {@link ColumnEntity} to entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param type the instance
     * @param entity      the {@link ColumnEntity} to be converted
     * @param <T>         the entity type
     * @return the same instance with values set from {@link ColumnEntity}
     * @throws NullPointerException when either type or entity are null
     */
    <T> T toEntity(T type, ColumnEntity entity);

    /**
     * Similar to {@link ColumnEntityConverter#toEntity(Class, ColumnEntity)}, but
     * search the instance type from {@link ColumnEntity#getName()}
     *
     * @param entity the {@link ColumnEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link ColumnEntity}
     * @throws NullPointerException when entity is null
     */
    <T> T toEntity(ColumnEntity entity);
}
