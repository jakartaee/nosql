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

package jakarta.nosql.column;


import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.QueryException;
import jakarta.nosql.Result;
import jakarta.nosql.ServiceLoaderProvider;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * Interface used to interact with the persistence context to {@link ColumnEntity}
 * The ColumnFamilyManager API is used to create and remove persistent {@link ColumnEntity} instances,
 * to select entities by their primary key, and to select over entities.
 */
public interface ColumnFamilyManager extends AutoCloseable {

    /**
     * Saves a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    ColumnEntity insert(ColumnEntity entity);

    /**
     * Updates a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    ColumnEntity update(ColumnEntity entity);

    /**
     * Updates a Column family entities, by default it's just run for each saving using
     * {@link ColumnFamilyManager#update(ColumnEntity)}, each NoSQL vendor might
     * replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    Iterable<ColumnEntity> update(Iterable<ColumnEntity> entities);

    /**
     * Saves a Column family entity with time to live
     *
     * @param entity column family to be saved
     * @param ttl    time to live
     * @return the entity saved
     * @throws NullPointerException          when either entity or ttl are null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    ColumnEntity insert(ColumnEntity entity, Duration ttl);

    /**
     * Saves a Column family entities, by default it's just run for each saving using
     * {@link ColumnFamilyManager#insert(ColumnEntity)}, each NoSQL vendor might
     * replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    Iterable<ColumnEntity> insert(Iterable<ColumnEntity> entities);

    /**
     * Saves a Column family entity with time to live, by default it's just run for each saving using
     * {@link ColumnFamilyManager#insert(ColumnEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException          when either entity or ttl are null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    Iterable<ColumnEntity> insert(Iterable<ColumnEntity> entities, Duration ttl);


    /**
     * Deletes an entity
     *
     * @param query the select to delete an entity
     * @throws NullPointerException          when either select or collection are null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void delete(ColumnDeleteQuery query);

    /**
     * Finds {@link ColumnEntity} from select
     *
     * @param query - select to figure out entities
     * @return entities found by select
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    Result<ColumnEntity> select(ColumnQuery query);

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws IllegalStateException           when there is not {@link ColumnQueryParser}
     * @throws QueryException when there is error in the syntax
     */
    default Result<ColumnEntity> query(String query) {
        Objects.requireNonNull(query, "query is required");
        ColumnQueryParser parser = ServiceLoaderProvider.get(ColumnQueryParser.class);
        return parser.query(query, this, ColumnObserverParser.EMPTY);
    }

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return a {@link ColumnPreparedStatement} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalStateException           when there is not {@link ColumnQueryParser}
     * @throws QueryException when there is error in the syntax
     */
    default ColumnPreparedStatement prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        ColumnQueryParser parser = ServiceLoaderProvider.get(ColumnQueryParser.class);
        return parser.prepare(query, this, ColumnObserverParser.EMPTY);
    }

    /**
     * Returns a single entity from select
     *
     * @param query - select to figure out entities
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException      when the result has more than 1 entity
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    Optional<ColumnEntity> singleResult(ColumnQuery query);

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
     * closes a resource
     */
    void close();

}
