/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/**
 * Provides query operations using the fluent API for interacting with NoSQL databases.
 * <p>
 * This package includes query operations that leverage a fluent API, designed to provide an easy way to perform various
 * querying actions on NoSQL databases. The fluent API supports operations such as filtering, ordering, pagination, and more.
 * </p>
 * <p>
 * The following operations are part of this package:
 * </p>
 * <ul>
 *     <li>Querying entities with conditions (e.g., {@link jakarta.nosql.Template#select(Class)}).</li>
 *     <li>Applying filters like equality, greater-than, less-than, LIKE, and range-based queries.</li>
 *     <li>Ordering query results using sorting operations.</li>
 *     <li>Paginating results with skip and limit conditions.</li>
 *     <li>Complex queries with logical conjunctions and disjunctions (AND/OR).</li>
 * </ul>
 * <p>
 * It is important to note that the fluent API querying behavior is not guaranteed across all NoSQL databases. For example,
 * key-value databases typically do not support the complex querying operations provided in this package.
 * Additionally, some NoSQL databases may not support all types of query operations or conditions.
 * </p>
 * <p>
 * This package aims to provide a consistent, high-level abstraction for querying NoSQL data, but it is important to
 * check the compatibility of the underlying NoSQL database when utilizing these operations.
 * </p>
 *
 * @since 1.0.0
 * @see jakarta.nosql.Template#select(Class)
 * @see jakarta.nosql.QueryMapper
 */
package ee.jakarta.tck.nosql.select;
