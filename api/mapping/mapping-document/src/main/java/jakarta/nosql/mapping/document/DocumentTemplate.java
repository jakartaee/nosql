/*
 * Copyright (c) 2022 Otavio Santana and others
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
 *
 * Contributors:
 *     Alessandro Moscatelli
 *
 */
package jakarta.nosql.mapping.document;


import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.criteria.CriteriaQuery;
import jakarta.nosql.criteria.CriteriaQueryResult;
import jakarta.nosql.criteria.ExecutableQuery;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.Template;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * DocumentTemplate is a helper class that increases productivity when performing common DocumentEntity operations.
 * Includes integrated object mapping between documents and POJOs.
 * It represents the common operation between an entity and {@link jakarta.nosql.document.DocumentCollectionManager}
 *
 * @see jakarta.nosql.document.DocumentCollectionManager
 */
public interface DocumentTemplate extends Template {

    /**
     * Create a <code>CriteriaQuery</code> object with the specified result
     * type.
     *
     * @param <T> type of the query result
     * @param type type of the query result
     * @return criteria query object
     */
    public <T extends Object> CriteriaQuery<T> createQuery(Class<T> type);


    /**
     * Deletes an entity
     *
     * @param query query to delete an entity
     * @throws NullPointerException query is null
     */
    void delete(DocumentDeleteQuery query);

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
     * Executes a {@link CriteriaQuery}
     *
     * @param criteriaQuery - the query
     * @param <T>   the instance type of the query {@link jakarta.nosql.criteria.Root}
     * @param <R>   the result type of the query
     * @return query result
     * @throws NullPointerException when criteriaQuery is null
     */
    <T extends Object, R extends CriteriaQueryResult<T>> R query(ExecutableQuery<T, R> criteriaQuery);

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
