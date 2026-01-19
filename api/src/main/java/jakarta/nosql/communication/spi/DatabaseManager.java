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
     * Document order = new Document()
     *         .put("_id", "A1")
     *         .put("total", 100);
     *
     * Document persisted = manager.insert(order);
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
     * List<Document> orders = List.of(
     *     new Document().put("_id", "A1"),
     *     new Document().put("_id", "A2")
     * );
     *
     * Iterable<Document> persisted = manager.insert(orders);
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
     * Document order = manager.findById("A1").orElseThrow();
     * order.put("total", 200);
     *
     * Document updated = manager.update(order);
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
     * Iterable<Document> updated =
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
     * Optional<Document> order =
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
}
