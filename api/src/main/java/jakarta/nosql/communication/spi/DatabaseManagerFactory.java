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
 * Factory responsible for creating {@link DatabaseManager} instances based on
 * a logical database identifier.
 *
 * <p>The interpretation of the {@code databaseName} parameter is entirely
 * provider-defined. It typically represents a logical name, alias, or
 * configuration key resolved by the underlying NoSQL provider. The specification
 * does not mandate how this value is mapped to physical databases, clusters,
 * endpoints, or client instances.</p>
 *
 * <p>This factory does not expose any lifecycle management contract. Resource
 * allocation, reuse, pooling, and cleanup are considered implementation details
 * of the provider and are typically managed by the runtime environment rather
 * than application code.</p>
 *
 * <p>Providers may return new {@link DatabaseManager} instances, cached
 * instances, or proxies, and may choose to share resources across multiple
 * managers. Applications must not assume any specific lifecycle or resource
 * ownership semantics.</p>
 *
 * <p>This type is intended to serve as an infrastructure-level entry point and
 * deliberately avoids defining configuration or connection semantics. All
 * configuration aspects, including authentication, connectivity, and resource
 * management, are delegated to the provider.</p>
 */
public interface DatabaseManagerFactory  {

    /**
     * Creates or resolves a {@link DatabaseManager} associated with the given
     * logical database name.
     *
     * @param databaseName a provider-defined logical identifier used to resolve
     *                     a database or configuration
     * @param <T> the database structure type
     * @return a {@link DatabaseManager} instance associated with the given name
     * @throws NullPointerException if {@code databaseName} is null
     */
    <T>DatabaseManager<T> create(String databaseName);

}
