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
 * Provides the Communication API for Jakarta NoSQL.
 *
 * <p>The Communication API provides optional contracts for NoSQL database
 * integration, intended primarily for database providers. Jakarta NoSQL
 * implementations are not required to provide Communication API support to
 * achieve specification compatibility; the Mapping API is the primary
 * programming model.</p>
 *
 * <p>The Communication API defines a minimal and extensible
 * foundation for interacting with NoSQL databases while
 * preserving the native semantics and operational behavior
 * of the underlying database implementation.</p>
 *
 * <p>The Communication API focuses on low-level lifecycle
 * operations such as storing, retrieving, replacing,
 * and removing data structures according to the semantics
 * and capabilities of the target database.</p>
 *
 * <p>The Communication API is organized in two foundational models:</p>
 * <ul>
 * <li>{@link jakarta.nosql.communication.keyvalue} for key-based persistence of values;</li>
 * <li>{@link jakarta.nosql.communication.record} for database-neutral record
 * operations, supporting column, document, graph, time-series, and vector
 * databases.</li>
 * </ul>
 *
 * <p>Specialized communication models may be introduced in future versions.</p>
 *
 * <p>The Communication API is intentionally designed to
 * support extensibility according to the capabilities,
 * consistency models, serialization strategies,
 * and operational characteristics of the underlying
 * database implementation.</p>
 *
 * <p>Depending on the provider implementation, communication
 * operations may support capabilities such as distributed
 * replication, eventual consistency, partitioning,
 * batching optimizations, append-oriented persistence,
 * provider-defined metadata, or provider-specific
 * communication behaviors.</p>
 *
 * <p>Providers may configure communication managers using
 * mechanisms such as Java properties, environment variables,
 * dependency injection, configuration files,
 * provider-defined defaults, or provider-specific
 * communication contexts.</p>
 *
 * <p>In summary, the Communication API defines a minimal,
 * low-level, provider-neutral, and extensible foundation
 * for integrating communication models within Jakarta
 * NoSQL.</p>
 */
package jakarta.nosql.communication;