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


import jakarta.nosql.ExecuteAsyncQueryException;
import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Interface used to interact with the persistence context to {@link ColumnEntity}
 * The ColumnFamilyManager API is used to create and remove persistent {@link ColumnEntity} instances,
 * to select entities by their primary key, and to select over entities. The main difference to {@link ColumnFamilyManager}
 * is because all the operation works asynchronously.
 */
public interface ColumnFamilyManagerAsync extends AutoCloseable {


    /**
     * Saves an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entity is null
     */
    void insert(ColumnEntity entity);

    /**
     * Saves an entity asynchronously with time to live
     *
     * @param entity entity to be saved
     * @param ttl    time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when the entity is null
     */
    void insert(ColumnEntity entity, Duration ttl);

    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning the saved entity
     *                 within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or callback are null
     */
    void insert(ColumnEntity entity, Consumer<ColumnEntity> callBack);


    /**
     * Saves an entities asynchronously, by default it's just run for each saving using
     * {@link ColumnFamilyManagerAsync#insert(ColumnEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entities is null
     */
    void insert(Iterable<ColumnEntity> entities);

    /**
     * Saves an entities asynchronously with time to live, by default it's just run for each saving using
     * {@link ColumnFamilyManagerAsync#insert(ColumnEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @param ttl      time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entities or ttl are null
     */
    void insert(Iterable<ColumnEntity> entities, Duration ttl);


    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param ttl      time to live
     * @param callBack the callback, when the process is finished will call this instance returning the saved entity
     *                 within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or ttl or callback are null
     */
    void insert(ColumnEntity entity, Duration ttl, Consumer<ColumnEntity> callBack);


    /**
     * Updates an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entity is null
     */
    void update(ColumnEntity entity);

    /**
     * Updates an entities asynchronously, by default it's just run for each saving using
     * {@link ColumnFamilyManagerAsync#update(ColumnEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entities is null
     */
    void update(Iterable<ColumnEntity> entities);

    /**
     * Updates an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the updated entity within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity, callback are null
     */
    void update(ColumnEntity entity, Consumer<ColumnEntity> callBack);


    /**
     * Deletes an entity asynchronously
     *
     * @param query select to delete an entity
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when select is null
     */
    void delete(ColumnDeleteQuery query);

    /**
     * Deletes an entity asynchronously
     *
     * @param query    select to delete an entity
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the null within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either select or callback are null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void delete(ColumnDeleteQuery query, Consumer<Void> callBack);


    /**
     * Finds {@link ColumnEntity} from select asynchronously
     *
     * @param query    select to select entities
     * @param callBack the callback, when the process is finished will call this instance returning the
     *                 result of select within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either select or callback are null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void select(ColumnQuery query, Consumer<Stream<ColumnEntity>> callBack);

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param callBack the callback result
     * @param query    the query as {@link String}
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws IllegalStateException    when there is not {@link ColumnQueryParserAsync}
     * @throws QueryException           when there is error in the syntax
     */
    default void query(String query, Consumer<Stream<ColumnEntity>> callBack) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(callBack, "callBack is required");
        ColumnQueryParserAsync parser = ServiceLoaderProvider.get(ColumnQueryParserAsync.class);
        parser.query(query, this, callBack, ColumnObserverParser.EMPTY);
    }


    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return a {@link ColumnPreparedStatementAsync} instance
     * @throws NullPointerException  when there is parameter null
     * @throws IllegalStateException when there is not {@link ColumnQueryParserAsync}
     * @throws QueryException        when there is error in the syntax
     */
    default ColumnPreparedStatementAsync prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        ColumnQueryParserAsync parser = ServiceLoaderProvider.get(ColumnQueryParserAsync.class);
        return parser.prepare(query, this, ColumnObserverParser.EMPTY);
    }


    /**
     * Returns a single entity from select
     *
     * @param query    - select to figure out entities
     * @param callBack the callback
     * @throws NonUniqueResultException      when the result has more than one entity
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    default void singleResult(ColumnQuery query, Consumer<Optional<ColumnEntity>> callBack) {
        Objects.requireNonNull(query, "query is required");
        select(query, entities -> {
            final Iterator<ColumnEntity> iterator = entities.iterator();
            if (!iterator.hasNext()) {
                callBack.accept(Optional.empty());
                return;
            }
            final ColumnEntity entity = iterator.next();
            if (!iterator.hasNext()) {
                callBack.accept(Optional.of(entity));
                return;
            }
            throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
        });
    }

    /**
     * Returns the number of elements from column family
     *
     * @param columnFamily the column family
     * @param callback     the callback with the response
     * @throws NullPointerException          when there is null parameter
     * @throws UnsupportedOperationException when the database dot not have support
     */
    void count(String columnFamily, Consumer<Long> callback);

    /**
     * closes a resource
     */
    void close();

}
