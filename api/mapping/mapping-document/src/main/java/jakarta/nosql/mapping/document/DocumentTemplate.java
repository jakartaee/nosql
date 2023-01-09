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
package jakarta.nosql.mapping.document;


import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentManager;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.Template;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * DocumentTemplate is a helper class that increases productivity when performing common DocumentEntity operations.
 * Includes integrated object mapping between documents and POJOs.
 * It represents the common operation between an entity and {@link DocumentManager}
 *
 * @see DocumentManager
 */
public interface DocumentTemplate extends Template {


    /**
     * Deletes an entity
     *
     * @param query query to delete an entity
     * @throws NullPointerException query is null
     */
    void delete(DocumentDeleteQuery query);

    /**
     * It starts a query using the fluent-API journey. It is a mutable and non-thread-safe instance.
     *
     * @param type the entity class
     * @param <T>         the entity type
     * @return a {@link DocumentQueryMapper.DocumentMapperDeleteFrom} instance
     * @throws NullPointerException when type is null
     */
    <T> DocumentQueryMapper.DocumentMapperDeleteFrom delete(Class<T> type);

    /**
     * Finds entities from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    <T> Stream<T> select(DocumentQuery query);

    /**
     * Returns a {@link DocumentQueryMapper.DocumentMapperFrom} implementation that does the object mapper API.
     *
     * @param type the entity class
     * @param <T>         the entity type
     * @return a {@link DocumentQueryMapper.DocumentMapperFrom} instance
     * @throws NullPointerException when type is null
     */
    <T> DocumentQueryMapper.DocumentMapperFrom select(Class<T> type);

    /**
     * Finds entities from query using pagination
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    <T> Page<T> select(DocumentQueryPagination query);

    /**
     * Returns the number of elements from document collection
     *
     * @param documentCollection the document collection
     * @return the number of elements
     * @throws NullPointerException          when document collection is null
     * @throws UnsupportedOperationException when the database dot not have support
     */
    long count(String documentCollection);

    /**
     * Returns the number of elements from document collection
     *
     * @param <T>        entityType
     * @param entityType the document collection
     * @return the number of elements
     * @throws NullPointerException          when document collection is null
     * @throws UnsupportedOperationException when the database dot not have support
     */
    <T> long count(Class<T> entityType);

    /**
     * Returns a single entity from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException     when query is null
     */
    <T> Optional<T> singleResult(DocumentQuery query);

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
