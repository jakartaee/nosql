/*
 * Copyright (c) 2019 Otavio Santana and others
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
 * This package contains all Id-value domain API.
 * <p>
 * Id-value (KV) stores use the associative array (also known as a map or dictionary) as their fundamental data model.
 * In this model, data is represented as a collection of key-value pairs, such that each possible key appears at most
 * once in the collection.
 * The key-value model is one of the simplest non-trivial data models, and richer data models are often implemented
 * as an extension of it.
 * The key-value model can be extended to a discretely ordered model that maintains keys in lexicographic order.
 * This extension is computationally powerful, in that it can efficiently retrieve selective key ranges.
 * Id-value stores can use consistency models ranging from eventual consistency to serializability.
 * Some databases support ordering of keys. There are various hardware implementations, and some users maintain
 * data in memory (RAM), while others employ solid-state drives or rotating disks.
 */
package jakarta.nosql.mapping.key;