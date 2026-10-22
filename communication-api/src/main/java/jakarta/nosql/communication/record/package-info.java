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

/**
 * Provides the record communication model for Jakarta NoSQL.
 *
 * <p>The record communication model is a database-neutral foundation for
 * record-oriented databases, including column, document, graph, time-series,
 * and vector databases. It defines basic lifecycle operations: insertion,
 * update, lookup by key, and deletion by key.</p>
 *
 * <pre>{@code
 * RecordManager<MyRecord> manager = ...
 *
 * MyRecord persisted = manager.insert(record);
 * Optional<MyRecord> found = manager.findByKey("user:10");
 * manager.deleteByKey("user:10");
 * }</pre>
 *
 * <p>A {@link jakarta.nosql.communication.record.RecordManagerFactory} is the
 * entry point to obtain {@code RecordManager} instances:</p>
 *
 * <pre>{@code
 * RecordManagerFactory factory = ...;
 *
 * RecordManager<MyRecord> manager =
 *         factory.get("products");
 *
 * MyRecord persisted = manager.insert(record);
 * }</pre>
 *
 * <p>This model intentionally avoids queries, graph traversal, similarity
 * search, time-series range operations, and other database-specific
 * capabilities. Providers define their own record representations and may
 * offer such capabilities through extensions. Durability, consistency,
 * transactional behavior, and conflict handling are determined by the
 * underlying database.</p>
 */
package jakarta.nosql.communication.record;
