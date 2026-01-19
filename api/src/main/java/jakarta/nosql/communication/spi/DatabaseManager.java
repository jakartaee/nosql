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
     * Returns the logical name of the managed database.
     *
     * <pre>{@code
     * String name = manager.name();
     * }</pre>
     *
     * @return the name of the database
     */
    String name();

    /**
     * Inserts a structure into the database.
     *
     * <p>If a matching structure already exists and the database enforces
     * transactional or consistency constraints, an exception may be raised.
     * Otherwise, behavior is provider-defined.</p>
     *
     * <p>The returned structure represents the persisted form, which may
     * include generated or updated values.</p>
     *
     * <pre>{@code
     * ProviderStructure order = new ProviderStructure()
     *         .put("_id", "A1")
     *         .put("total", 100);
     *
     * ProviderStructure persisted = manager.insert(order);
     * }</pre>
     *
     * @param entity the structure to insert
     * @return the persisted structure
     * @throws NullPointerException if the structure is null
     */
    T insert(T entity);

    /**
     * Inserts multiple structures into the database.
     *
     * <pre>{@code
     * List<ProviderStructure> orders = List.of(
     *     new ProviderStructure().put("_id", "A1"),
     *     new ProviderStructure().put("_id", "A2")
     * );
     *
     * Iterable<ProviderStructure> persisted = manager.insert(orders);
     * }</pre>
     *
     * @param entities structures to insert
     * @return the persisted structures
     * @throws NullPointerException if the iterable or any element is null
     */
    Iterable<T> insert(Iterable<T> entities);

    /**
     * Updates an existing structure.
     *
     * <p>If no matching structure exists, behavior is provider-defined.
     * Some providers may treat this operation as an insert.</p>
     *
     * <pre>{@code
     * ProviderStructure order = manager.findById("A1").orElseThrow();
     * order.put("total", 200);
     *
     * ProviderStructure updated = manager.update(order);
     * }</pre>
     *
     * @param entity the structure to update
     * @return the updated structure
     * @throws NullPointerException if the structure is null
     */
    T update(T entity);

    /**
     * Updates multiple structures.
     *
     * <pre>{@code
     * Iterable<ProviderStructure> updated =
     *         manager.update(List.of(order1, order2));
     * }</pre>
     *
     * @param entities structures to update
     * @return the updated structures
     * @throws NullPointerException if the iterable or any element is null
     */
    Iterable<T> update(Iterable<T> entities);

    /**
     * Deletes a structure from the database.
     *
     * <pre>{@code
     * manager.delete(order);
     * }</pre>
     *
     * @param entity the structure to delete
     * @throws NullPointerException if the structure is null
     */
    void delete(T entity);

    /**
     * Retrieves a structure by a provider-defined identifier.
     *
     * <p>The identifier type and lookup semantics are entirely provider-defined
     * and may represent a simple value or a composite structure.</p>
     *
     * <pre>{@code
     * Optional<ProviderStructure> order =
     *         manager.findById("A1");
     * }</pre>
     *
     * @param id provider-defined identifier
     * @param <K> identifier type
     * @return the matching structure, if found
     * @throws NullPointerException if the identifier is null
     */
    <K> Optional<T> findById(K id);

    /**
     * Deletes a structure by a provider-defined identifier.
     *
     * <p>If no matching structure exists, the operation is silently ignored.</p>
     *
     * <pre>{@code
     * manager.deleteById("A1");
     * }</pre>
     *
     * @param id provider-defined identifier
     * @param <K> identifier type
     * @throws NullPointerException if the identifier is null
     */
    <K> void deleteById(K id);

    /**
     * Creates a provider-defined select operation for this database.
     *
     * <p>This method returns a {@link SelectExecutor} that defines the
     * structural flow of a select operation. Query semantics, filtering,
     * projection, ordering, pagination, and execution behavior are entirely
     * provider-defined.</p>
     *
     * <p>The returned executor may support conditional selection, ordering,
     * pagination, or streaming depending on the underlying database.
     * Unsupported features may result in {@link UnsupportedOperationException}
     * during execution.</p>
     *
     * @return a provider-defined select executor
     * @throws UnsupportedOperationException if select operations
     * are not supported by the provider
     */
    SelectExecutor<T> select();

    /**
     * Creates a provider-defined select operation scoped to the given
     * logical names.
     *
     * <p>The interpretation of {@code names} is provider-defined. They may
     * represent collections, containers, tables, buckets, graphs, or any
     * other logical structures supported by the underlying database.</p>
     *
     * <p>This specification does not define how multiple names are resolved
     * or combined. Providers may ignore, restrict, or reject multiple names.</p>
     *
     * @param names provider-defined logical identifiers
     * @return a provider-defined select executor scoped to the given names
     * @throws NullPointerException if {@code names} is null or contains null
     * @throws UnsupportedOperationException if the provider
     * does not support named select operations
     */
    SelectExecutor<T> select(String... names);

    /**
     * Creates a provider-defined delete operation for this database.
     *
     * <p>This method returns a {@link DeleteExecutor} that defines the
     * structural flow of a delete operation. Delete semantics, conditional
     * support, and execution behavior are entirely provider-defined.</p>
     *
     * <p>Providers are not required to support conditional or unconditional
     * delete operations. Unsupported delete capabilities may result in
     * {@link UnsupportedOperationException} during execution.</p>
     *
     * @return a provider-defined delete executor
     * @throws UnsupportedOperationException if the provider
     * does not support delete operations
     */
    DeleteExecutor delete();

    /**
     * Creates a provider-defined delete operation scoped to the given
     * logical names.
     *
     * <p>The interpretation of {@code names} is provider-defined. They may
     * represent collections, containers, tables, buckets, graphs, or other
     * logical delete targets.</p>
     *
     * <p>This specification does not mandate support for scoped deletes.
     * Providers may ignore, restrict, or reject the supplied names.</p>
     *
     * @param names provider-defined logical identifiers
     * @return a provider-defined delete executor scoped to the given names
     * @throws NullPointerException if {@code names} is null or contains null
     * @throws UnsupportedOperationException if the provider
     * does not support named delete operations
     */
    DeleteExecutor delete(String... names);

    /**
     * Creates a provider-defined update operation for this database.
     *
     * <p>This method returns an {@link UpdateExecutor} that defines the
     * structural flow of an update operation. Update semantics, mutation
     * models, conditional support, and execution guarantees are entirely
     * provider-defined.</p>
     *
     * <p>The returned executor supports applying one or more provider-defined
     * update tokens followed by optional conditions. Unsupported update
     * capabilities may result in {@link UnsupportedOperationException}
     * during execution.</p>
     *
     * @return a provider-defined update executor
     * @throws UnsupportedOperationException if the provider
     * does not support update operations
     */
    UpdateExecutor update();
}
