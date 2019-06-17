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
import jakarta.nosql.mapping.IdNotFoundException;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.PreparedStatement;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * This interface that represents the common operation between an entity
 * and {@link jakarta.nosql.column.ColumnEntity}
 *
 * @see jakarta.nosql.column.ColumnFamilyManager
 */
public interface ColumnTemplate {

    /**
     * Inserts entity
     *
     * @param entity entity to be saved
     * @param <T>    the instance type
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    <T> T insert(T entity);


    /**
     * Inserts entity with time to live
     *
     * @param entity entity to be saved
     * @param ttl    the time to live
     * @param <T>    the instance type
     * @return the entity saved
     */
    <T> T insert(T entity, Duration ttl);


    /**
     * Inserts entity, by default it's just run for each saving using
     * {@link ColumnTemplate#insert(Object)}},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    <T> Iterable<T> insert(Iterable<T> entities);

    /**
     * Inserts entities collection entity with time to live, by default it's just run for each saving using
     * {@link ColumnTemplate#insert(Object, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    <T> Iterable<T> insert(Iterable<T> entities, Duration ttl);


    /**
     * Updates a entity
     *
     * @param entity entity to be updated
     * @param <T>    the instance type
     * @return the entity updated
     * @throws NullPointerException when entity is null
     */
    <T> T update(T entity);


    /**
     * Saves entity, by default it's just run for each saving using
     * {@link ColumnTemplate#update(Object)}},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be updated
     * @param <T>      the instance type
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    <T> Iterable<T> update(Iterable<T> entities);

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
    <T> List<T> select(ColumnQuery query);

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
     * Executes a query then bring the result as a {@link List}
     *
     * @param query the query
     * @param <T>   the entity type
     * @return the result as {@link List}
     * @throws NullPointerException when the query is null
     */
    <T> List<T> query(String query);

    /**
     * Executes a query then bring the result as a unique result
     *
     * @param query the query
     * @param <T>   the entity type
     * @return the result as {@link List}
     * @throws NullPointerException     when the query is null
     * @throws jakarta.nosql.NonUniqueResultException if returns more than one result
     */
    <T> Optional<T> singleResult(String query);

    /**
     * Creates a {@link PreparedStatement} from the query
     *
     * @param query the query
     * @return a {@link PreparedStatement} instance
     * @throws NullPointerException when the query is null
     */
    PreparedStatement prepare(String query);

    /**
     * Finds by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>        the id type
     * @return the entity instance otherwise {@link Optional#empty()}
     * @throws NullPointerException                   when either the entityClass or id are null
     * @throws IdNotFoundException when the entityClass does not have the Id annotation
     */
    <T, K> Optional<T> find(Class<T> entityClass, K id);

    /**
     * Deletes by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>        the id type
     * @throws NullPointerException                   when either the entityClass or id are null
     * @throws IdNotFoundException when the entityClass does not have the Id annotation
     */
    <T, K> void delete(Class<T> entityClass, K id);

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
     * Returns a single entity from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException     when query is null
     */
    default <T> Optional<T> singleResult(ColumnQuery query) {
        List<T> entities = select(query);
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        throw new NonUniqueResultException("The query returns more than one entity, query: " + query);
    }

}
