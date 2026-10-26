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
 * Defines the Jakarta NoSQL Communication API module.
 *
 * <p>This module is optional: Jakarta NoSQL implementations are not required
 * to provide Communication API support to achieve specification
 * compatibility.</p>
 *
 * <p>This module provides a minimal and extensible
 * communication foundation for interacting with NoSQL
 * databases while preserving the native semantics and
 * operational behavior of the underlying database
 * implementation.</p>
 *
 * <p>The Communication API focuses on low-level lifecycle
 * operations for storing, retrieving, replacing,
 * and removing data structures according to the
 * capabilities and characteristics of the target database.</p>
 *
 * <p>The APIs exposed by this module are designed to support
 * extensibility, provider neutrality, and interoperability
 * across the key-value and record communication models.</p>
 *
 * <p>Provider implementations may support capabilities such
 * as distributed replication, eventual consistency,
 * partitioning, batching optimizations,
 * provider-defined serialization strategies,
 * or provider-specific
 * communication behaviors.</p>
 */
module jakarta.nosql.communication {
    exports jakarta.nosql.communication;
    exports jakarta.nosql.communication.keyvalue;
    exports jakarta.nosql.communication.record;
}
