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
package jakarta.nosql.mapping.keyvalue;


import jakarta.nosql.mapping.PreparedStatement;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;


/**
 * This interface that represents the common operation between an entity and KeyValueEntity
 */

/**
 * KeyValueTemplate is a helper class that increases productivity when performing common Bucket operations.
 * Includes integrated object mapping between documents and POJOs.
 * It represents the common operation between an entity and {@link jakarta.nosql.keyvalue.BucketManager}
 *
 * @see jakarta.nosql.keyvalue.BucketManager
 */
public interface KeyValueTemplate {
    /**
     * Saves the entity
     *
     * @param entity the entity to be insert
     * @param <T>    the entity type
     * @return the entity
     * @throws NullPointerException when entity is null
     */
    <T> T put(T entity);

    /**
     * Saves the entity with time to live
     *
     * @param entity the entity to be insert
     * @param ttl    the defined time to live
     * @param <T>    the entity type
     * @return the entity
     * @throws NullPointerException          when entity is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    <T> T put(T entity, Duration ttl);

    /**
     * Saves the {@link Iterable} of entities
     *
     * @param entities keys to be insert
     * @param <T>      the entity type
     * @return the entities
     * @throws NullPointerException when the iterable is null
     */
    default <T> Iterable<T> put(Iterable<T> entities) {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::put).collect(Collectors.toList());
    }

    /**
     * Saves the {@link Iterable} of entities with a defined time to live
     *
     * @param entities entities to be insert
     * @param ttl      the time to entity expire
     * @param <T>      the entity type
     * @return the entities
     * @throws NullPointerException          when the iterable is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    default <T> Iterable<T> put(Iterable<T> entities, Duration ttl) {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false).map(d -> put(d, ttl)).collect(toList());
    }

    /**
     * Finds the Value from a key
     *
     * @param key         the key
     * @param <K>         the key type
     * @param <T>         the entity type
     * @param entityClass the entity class to convert the result
     * @return the {@link Optional} when is not found will return a {@link Optional#empty()}
     * @throws NullPointerException when the key is null
     */
    <K, T> Optional<T> get(K key, Class<T> entityClass);

    /**
     * Executes query in the database
     *
     * @param query       the query
     * @param entityClass the entity class
     * @param <T>         the entity type
     * @return the result list, if either <b>put</b> or <b>remove</b> it will return empty
     * @throws NullPointerException when query is null, if the query is <b>get</b> the entity class is required
     */
    <T> Stream<T> query(String query, Class<T> entityClass);

    /**
     * Executes query in the database then returns as single result
     *
     * @param query       the query
     * @param entityClass the entity class
     * @param <T>         the entity type
     * @return the result {@link Optional}, if either <b>put</b> or <b>remove</b> it will return {@link Optional#empty()}
     * @throws NullPointerException when query is null, if the query is <b>get</b> the entity class is required
     */
    <T> Optional<T> getSingleResult(String query, Class<T> entityClass);

    /**
     * Executes query in the database and don't return result, e.g.: when the query is either <b>remove</b> or
     * <b>put</b>
     *
     * @param query the query
     * @throws NullPointerException when query is null
     */
    void query(String query);

    /**
     * Executes query with {@link PreparedStatement}
     *
     * @param query       the query
     * @param entityClass the entity class
     * @param <T>         the entity type
     * @return a {@link PreparedStatement} instance
     * @throws NullPointerException when query is null, if the query is <b>get</b> the entity class is required
     */
    <T> PreparedStatement prepare(String query, Class<T> entityClass);

    /**
     * Finds a list of values from keys
     *
     * @param entityClass the entity class
     * @param keys        the keys to be used in this query
     * @param <K>         the key type
     * @param <T>         the entity type
     * @return the list of result
     * @throws NullPointerException when either the keys or the entities values are null
     */
    <K, T> Iterable<T> get(Iterable<K> keys, Class<T> entityClass);

    /**
     * Removes an entity from key
     *
     * @param key the key bo be used
     * @param <K> the key type
     * @throws NullPointerException when the key is null
     */
    <K> void delete(K key);

    /**
     * Removes entities from keys
     *
     * @param keys the keys to be used
     * @param <K>  the key type
     * @throws NullPointerException when the key is null
     */
    <K> void delete(Iterable<K> keys);

}
