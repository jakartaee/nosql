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

package jakarta.nosql.document;


import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Stream;

/**
 * The persistence context to {@link DocumentEntity}.
 * The DocumentCollectionManager API is used to create and remove persistent {@link DocumentEntity} instances,
 * to select entities by their primary key, and to select over entities.
 * Thus, ColumnFamilyManager enables CRUD Operation for {@link DocumentEntity}.
 */
public interface DocumentCollectionManager extends AutoCloseable {

    /**
     * Saves document collection entity
     *
     * @param entity entity to be saved
     * @return the entity saved
     * @throws NullPointerException when document is null
     */
    DocumentEntity insert(DocumentEntity entity);

    /**
     * Saves document collection entity with time to live
     *
     * @param entity entity to be saved
     * @param ttl    the time to live
     * @return the entity saved
     * @throws NullPointerException          when either entity or ttl are null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    DocumentEntity insert(DocumentEntity entity, Duration ttl);

    /**
     * Saves documents collection entity, by default it's just run for each saving using
     * {@link DocumentCollectionManager#insert(DocumentEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    Iterable<DocumentEntity> insert(Iterable<DocumentEntity> entities);

    /**
     * Saves documents collection entity with time to live, by default it's just run for each saving using
     * {@link DocumentCollectionManager#insert(DocumentEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException          when entities is null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    Iterable<DocumentEntity> insert(Iterable<DocumentEntity> entities, Duration ttl);

    /**
     * Updates an entity
     *
     * @param entity entity to be updated
     * @return the entity updated
     * @throws NullPointerException when entity is null
     */
    DocumentEntity update(DocumentEntity entity);

    /**
     * Updates documents collection entity, by default it's just run for each saving using
     * {@link DocumentCollectionManager#update(DocumentEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    Iterable<DocumentEntity> update(Iterable<DocumentEntity> entities);

    /**
     * Deletes an entity
     *
     * @param query select to delete an entity
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void delete(DocumentDeleteQuery query);

    /**
     * Finds {@link DocumentEntity} from select
     *
     * @param query - select to figure out entities
     * @return entities found by select
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    Stream<DocumentEntity> select(DocumentQuery query);

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws IllegalStateException    when there is not {@link DocumentQueryParser}
     * @throws QueryException           when there is error in the syntax
     */
    default Stream<DocumentEntity> query(String query) {
        Objects.requireNonNull(query, "query is required");
        DocumentQueryParser parser = ServiceLoaderProvider.get(DocumentQueryParser.class,
                ()-> ServiceLoader.load(DocumentQueryParser.class));
        return parser.query(query, this, DocumentObserverParser.EMPTY);
    }

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return a {@link DocumentPreparedStatement} instance
     * @throws NullPointerException  when there is parameter null
     * @throws IllegalStateException when there is not {@link DocumentQueryParser}
     * @throws QueryException        when there is error in the syntax
     */
    default DocumentPreparedStatement prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        DocumentQueryParser parser = ServiceLoaderProvider.get(DocumentQueryParser.class,
                ()-> ServiceLoader.load(DocumentQueryParser.class));
        return parser.prepare(query, this, DocumentObserverParser.EMPTY);
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
    default Optional<DocumentEntity> singleResult(DocumentQuery query) {
        Objects.requireNonNull(query, "query is required");
        Stream<DocumentEntity> entities = select(query);
        final Iterator<DocumentEntity> iterator = entities.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final DocumentEntity entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
    }

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
     * closes a resource
     */
    void close();

}
