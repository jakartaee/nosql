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


import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.Template;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * ColumnTemplate is a helper class that increases productivity when performing common Column Family operations.
 * Includes integrated object mapping between documents and POJOs.
 * It represents the common operation between an entity and {@link jakarta.nosql.column.ColumnFamilyManager}
 *
 * @see jakarta.nosql.column.ColumnFamilyManager
 */
public interface ColumnTemplate extends Template {

    /**
     * Returns the number of elements from column family
     *
     * @param columnFamily the column family
     * @return the number of elements
     * @throws NullPointerException          when column family is null
     * @throws UnsupportedOperationException when the database dot not have support
     */
    long count(String columnFamily);

    /**
     * Returns the number of elements from column family
     *
     * @param <T>         the entity type
     * @param entityClass the column family
     * @return the number of elements
     * @throws NullPointerException          when column family is null
     * @throws UnsupportedOperationException when the database dot not have support
     */
    <T> long count(Class<T> entityClass);

    /**
     * Deletes an entity
     *
     * @param query query to delete an entity
     * @throws NullPointerException when query is null
     */
    void delete(ColumnDeleteQuery query);

    /**
     * Finds entities from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    <T> Stream<T> select(ColumnQuery query);

    /**
     * Finds entities from query using pagination
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    <T> Page<T> select(ColumnQueryPagination query);

    /**
     * Returns a single entity from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException     when query is null
     */
    <T> Optional<T> singleResult(ColumnQuery query);

    /**
     * Executes a query then bring the result as a {@link Stream}
     *
     * @param query the query
     * @param <T>   the entity type
     * @return the result as {@link Stream}
     * @throws NullPointerException          when the query is null
     * @throws UnsupportedOperationException if the specified template does not support this operation
     */
    <T> Stream<T> query(String query);

    /**
     * Executes a query then bring the result as a unique result
     *
     * @param query the query
     * @param <T>   the entity type
     * @return the result as {@link Optional}
     * @throws NullPointerException                   when the query is null
     * @throws jakarta.nosql.NonUniqueResultException if returns more than one result
     * @throws UnsupportedOperationException          if the specified template does not support this operation
     */
    <T> Optional<T> singleResult(String query);

    /**
     * Creates a {@link PreparedStatement} from the query
     *
     * @param query the query
     * @return a {@link PreparedStatement} instance
     * @throws NullPointerException          when the query is null
     * @throws UnsupportedOperationException if the specified template does not support this operation
     */
    PreparedStatement prepare(String query);

}
