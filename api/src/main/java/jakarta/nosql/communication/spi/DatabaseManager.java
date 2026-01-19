/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package jakarta.nosql.communication.spi;

import java.util.Optional;

/**
 * Provides basic data manipulation operations for a provider-specific
 * database structure.
 *
 * <p>The type parameter {@code T} represents the native data structure
 * used by the underlying NoSQL provider. This specification intentionally
 * does not impose any semantic meaning on this type, such as entity identity,
 * schema, or domain concepts.</p>
 *
 * <p>This interface defines a minimal set of structural write operations.
 * It does not provide query, filtering, or read semantics. Such capabilities
 * are considered provider-specific and are expected to be exposed through
 * higher-level abstractions.</p>
 *
 * <p>The behavior of insert, update, and delete operations is entirely
 * provider-defined. Implementations may enrich, replace, or ignore the
 * returned instances, and applications must not assume any particular
 * mutation or lifecycle semantics.</p>
 *
 * @param <T> the provider-specific database structure
 */
public interface DatabaseManager<T> {

    /**
     * Returns the name of the managed database.
     *
     * @return the name of the database
     */
    String name();

    /**
     * Inserts an entity into the database.
     *
     * <p>If an entity with the same unique identifier already exists in the database and the database
     * supports ACID transactions, this method throws an exception. In databases following the BASE model
     * or using an append model to write data, no exception is thrown.</p>
     *
     * <p>The returned entity includes all values written to the database, including automatically generated
     * or incremented values. After calling this method, avoid using the supplied entity instance, as this
     * method makes no guarantees about its state.</p>
     *
     * @param entity the entity to be saved
     * @return the saved entity
     * @throws NullPointerException when the provided entity is null
     */
    T insert(T entity);

    /**
     * Inserts multiple entities into the database.
     *
     * <p>If any entity with the same unique identifier as any of the given entities already exists in the database
     * and the database supports ACID transactions, this method raises an error. In databases following the BASE model
     * or using an append model to write data, no exception is thrown.</p>
     *
     * <p>The returned iterable contains all inserted entities, including all automatically generated or incremented
     * values. After calling this method, avoid using the supplied entity instances, as this method makes no guarantees
     * about their state.</p>
     *
     * @param entities entities to insert
     * @return an iterable containing the inserted entities
     * @throws NullPointerException if the iterable is null or any element is null
     */
    Iterable<T> insert(Iterable<T> entities);

    /**
     * Modifies an existing entity in the database.
     *
     * <p>To perform an update, a matching entity with the same unique identifier must exist in the database.
     * In databases using an append model to write data or following the BASE model, this method behaves
     * the same as the {@link #insert} method.</p>
     *
     * <p>If the entity is versioned (e.g., with an annotation or by convention from the entity model),
     * the version must also match. The version is automatically incremented during the update.</p>
     *
     * <p>Non-matching entities are ignored and do not cause an error.</p>
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws NullPointerException if the entity is null
     */
    T update(T entity);

    /**
     * Modifies multiple entities that already exist in the database.
     *
     * <p>To perform an update to an entity, a matching entity with the same unique identifier must exist
     * in the database. In databases using an append model to write data or following the BASE model,
     * this method behaves the same as the {@link #insert(Iterable)} method.</p>
     *
     * <p>If the entity is versioned (e.g., with an annotation or by convention from the entity model),
     * the version must also match. The version is automatically incremented during the update.</p>
     *
     * <p>Non-matching entities are ignored and do not cause an error.</p>
     *
     * @param entities entities to update
     * @return the number of matching entities that were found in the database and updated
     * @throws NullPointerException if the iterable is null or any element is null
     */
    Iterable<T> update(Iterable<T> entities);


    /**
     * Deletes a given entity. Deletion is performed by matching the Id, and if
     * the entity is versioned (for example, with
     * {@code jakarta.persistence.Version}), then also the version. Other
     * attributes of the entity do not need to match.
     *
     * @param entity must not be {@code null}.
     * @throws NullPointerException              when the entity is null
     */
    void delete(T entity);

    /**
     * Retrieves an entity by its Id.
     *
     * @param id must not be {@code null}.
     * @return the entity with the given Id or {@link Optional#empty()} if none
     * is found.
     * @throws NullPointerException when the Id is {@code null}.
     */
    <K> Optional<T> findById(K id);

    /**
     * Deletes the entity with the given Id.
     * <p>
     * If the entity is not found in the persistence store it is silently
     * ignored.
     *
     * @param id must not be {@code null}.
     * @throws NullPointerException when the Id is {@code null}.
     */
    <K> void deleteById(K id);
}
