/*
 * Copyright (c) 2021 Otavio Santana and others
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
package jakarta.nosql.mapping;

import java.time.Duration;
import java.util.Optional;

/**
 * Templates are a helper class that increases productivity when performing common NoSQL operations.
 * Includes integrated object mapping between documents and POJOs.
 */
public interface Template {

    /**
     * Inserts entity
     *
     * @param entity entity to insert
     * @param <T>    the instance type
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    <T> T insert(T entity);

    /**
     * Inserts entity with time to live
     *
     * @param entity entity to insert
     * @param ttl    the time to live
     * @param <T>    the instance type
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    <T> T insert(T entity, Duration ttl);

    /**
     * Inserts entity, by default it's just run for each saving using
     * {@link Template#insert(Object)}},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to insert
     * @param <T>      the instance type
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    <T> Iterable<T> insert(Iterable<T> entities);

    /**
     * Inserts entities collection entity with time to live, by default it's just run for each saving using
     * {@link Template#insert(Object, Duration)},
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
     * Updates an entity
     *
     * @param entity entity to update
     * @param <T>    the instance type
     * @return the entity updated
     * @throws NullPointerException when entity is null
     */
    <T> T update(T entity);

    /**
     * Saves entity, by default it's just run for each saving using
     * {@link Template#update(Object)}},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to update
     * @param <T>      the instance type
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    <T> Iterable<T> update(Iterable<T> entities);

    /**
     * Finds by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>         the id type
     * @return the entity instance otherwise {@link Optional#empty()}
     * @throws NullPointerException when either the entityClass or id are null
     * @throws IdNotFoundException  when the entityClass does not have the Id annotation
     */
    <T, K> Optional<T> find(Class<T> entityClass, K id);

    /**
     * Deletes by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>         the id type
     * @throws NullPointerException when either the entityClass or id are null
     * @throws IdNotFoundException  when the entityClass does not have the Id annotation
     */
    <T, K> void delete(Class<T> entityClass, K id);
}
