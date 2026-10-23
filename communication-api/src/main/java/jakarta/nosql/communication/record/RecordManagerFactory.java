/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package jakarta.nosql.communication.record;

import jakarta.nosql.communication.Record;

/**
 * Creates {@link RecordManager} instances for record communication.
 *
 * <p>A {@code RecordManagerFactory} is the entry point to the record
 * communication model of the Jakarta NoSQL Communication API. It provides
 * access to managers associated with provider-defined logical storage
 * contexts.</p>
 *
 * <p>The interpretation of the supplied name, as well as manager creation,
 * reuse, and lifecycle semantics, are determined by the provider
 * implementation. Depending on the database, the name may represent, for
 * example, a document collection, a column family or table, a graph
 * namespace, a time-series measurement, or a vector collection or index.
 * These are examples, not mandatory mappings.</p>
 */
public interface RecordManagerFactory {

    /**
     * Returns a {@link RecordManager} associated with the provided name.
     *
     * <p>The interpretation of the name is provider-specific, and this
     * method does not require a universal database container model.
     * The provider determines how the manager is created, reused, and
     * managed.</p>
     *
     * <p>The record type is chosen by the caller and is not guaranteed
     * to be checked when this method is invoked.</p>
     *
     * <pre>{@code
     * RecordManagerFactory factory = ...
     *
     * RecordManager<MyRecord> manager = factory.get("products");
     * }</pre>
     *
     * @param name the provider-defined logical storage context name
     * @param <T>  the record type
     * @return the manager associated with the name
     * @throws NullPointerException if name is null
     */
    <T extends Record> RecordManager<T> get(String name);

    /**
     * Returns a {@link RecordManager} associated with the provider-defined
     * default or configured communication context.
     *
     * <p>The context resolution is provider-specific. Providers may use a
     * configured default, dependency injection, environment configuration,
     * or other mechanisms. This method does not require databases to support
     * a default collection or namespace.</p>
     *
     * <pre>{@code
     * RecordManagerFactory factory = ...
     *
     * RecordManager<MyRecord> manager = factory.get();
     * }</pre>
     *
     * @param <T> the record type
     * @return the manager associated with the provider-defined context
     * @throws IllegalStateException when the provider cannot resolve a
     *                               communication context or when the
     *                               underlying configuration is incomplete
     *                               or invalid
     */
    <T extends Record> RecordManager<T> get();
}
