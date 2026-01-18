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

    T insert(T entity);

    Iterable<T> insert(Iterable<T> entities);


    T update(T entity);

    void delete(T entity);
}
