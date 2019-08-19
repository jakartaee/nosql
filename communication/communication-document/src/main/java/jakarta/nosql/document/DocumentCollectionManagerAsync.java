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

package jakarta.nosql.document;


import jakarta.nosql.ExecuteAsyncQueryException;
import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.QueryException;
import jakarta.nosql.Result;
import jakarta.nosql.ServiceLoaderProvider;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Interface used to interact with the persistence context to {@link DocumentEntity}
 * The DocumentCollectionManager API is used to create and remove persistent {@link DocumentEntity} instances,
 * to select entities by their primary key, and to select over entities. The main difference to {@link DocumentCollectionManager}
 * is because all the operation works asynchronously.
 */
public interface DocumentCollectionManagerAsync extends AutoCloseable {


    /**
     * Saves an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entity is null
     */
    void insert(DocumentEntity entity);

    /**
     * Saves an entity asynchronously with time to live
     *
     * @param entity entity to be saved
     * @param ttl    the time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or ttl are null
     */
    void insert(DocumentEntity entity, Duration ttl);

    /**
     * Saves entities asynchronously, by default it's just run for each saving using
     * {@link DocumentCollectionManagerAsync#insert(DocumentEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entities is null
     */
    void insert(Iterable<DocumentEntity> entities);

    /**
     * Saves entities asynchronously with time to live, by default it's just run for each saving using
     * {@link DocumentCollectionManagerAsync#insert(DocumentEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param ttl      time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entities or ttl are null
     */
    void insert(Iterable<DocumentEntity> entities, Duration ttl);

    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the saved entity within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either callback or entity are null
     */
    void insert(DocumentEntity entity, Consumer<DocumentEntity> callBack);

    /**
     * Saves an entity asynchronously with time to live
     *
     * @param entity   entity to be saved
     * @param ttl      time to live
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the saved entity within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or ttl or callback are null
     */
    void insert(DocumentEntity entity, Duration ttl, Consumer<DocumentEntity> callBack);


    /**
     * Updates an entity asynchronously
     *
     * @param entity entity to be updated
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entity is null
     */
    void update(DocumentEntity entity);

    /**
     * Updates entities asynchronously, by default it's just run for each saving using
     * {@link DocumentCollectionManagerAsync#update(DocumentEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entities is null
     */
    void update(Iterable<DocumentEntity> entities);

    /**
     * Updates an entity asynchronously
     *
     * @param entity   entity to be updated
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the updated entity within parametersa
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or callback are null
     */
    void update(DocumentEntity entity, Consumer<DocumentEntity> callBack);


    /**
     * Deletes an entity asynchronously
     *
     * @param query select to delete an entity
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void delete(DocumentDeleteQuery query);

    /**
     * Deletes an entity asynchronously
     *
     * @param query    select to delete an entity
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the null within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either select or callback are null
     */
    void delete(DocumentDeleteQuery query, Consumer<Void> callBack);

    /**
     * Finds {@link DocumentEntity} from select asynchronously
     *
     * @param query    select to select entities
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the result of select within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either select or callback are null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void select(DocumentQuery query, Consumer<Result<DocumentEntity>> callBack);


    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param callBack the callback result
     * @param query    the query as {@link String}
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws IllegalStateException    when there is not {@link DocumentQueryParserAsync}
     * @throws QueryException           when there is error in the syntax
     */
    default void query(String query, Consumer<Result<DocumentEntity>> callBack) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(callBack, "callBack is required");
        DocumentQueryParserAsync parser = ServiceLoaderProvider.get(DocumentQueryParserAsync.class);
        parser.query(query, this, callBack, DocumentObserverParser.EMPTY);
    }

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return a {@link DocumentPreparedStatement} instance
     * @throws NullPointerException  when there is parameter null
     * @throws IllegalStateException when there is not {@link DocumentQueryParserAsync}
     * @throws QueryException        when there is error in the syntax
     */
    default DocumentPreparedStatementAsync prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        DocumentQueryParserAsync parser = ServiceLoaderProvider.get(DocumentQueryParserAsync.class);
        return parser.prepare(query, this, DocumentObserverParser.EMPTY);
    }


    /**
     * Returns a single entity from select
     *
     * @param query    - select to figure out entities
     * @param callBack the callback
     * @throws NonUniqueResultException      when the result has more than 1 entity
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    default void singleResult(DocumentQuery query, Consumer<Optional<DocumentEntity>> callBack) {

        select(query, entities -> {
            final Iterator<DocumentEntity> iterator = entities.iterator();
            if (!iterator.hasNext()) {
                callBack.accept(Optional.empty());
                return;
            }
            final DocumentEntity entity = iterator.next();
            if (!iterator.hasNext()) {
                callBack.accept(Optional.of(entity));
            } else {
                throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
            }
        });

    }

    /**
     * Returns the number of elements from document collection
     *
     * @param documentCollection the document collection
     * @param callback           the callback with the response
     * @throws NullPointerException          when there is null parameter
     * @throws UnsupportedOperationException when the database dot not have support
     */
    void count(String documentCollection, Consumer<Long> callback);

    /**
     * closes a resource
     */
    void close();

}
