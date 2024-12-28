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
 * Provides delete operations for interacting with NoSQL databases using fluent API.
 * <p>
 * This package includes delete operations that enable entities to be removed from NoSQL databases based on specific
 * conditions. The fluent API provides a simple and flexible way to perform delete operations, such as deleting entities
 * by their IDs or applying various conditions to filter the entities to be deleted.
 * </p>
 * <p>
 * The following operations are part of this package:
 * </p>
 * <ul>
 *     <li>Deleting entities based on conditions using a fluent API with the method {@link jakarta.nosql.Template#delete(java.lang.Class)}.</li>
 *     <li>Performing logical conjunctions and disjunctions (AND/OR) in delete queries.</li>
 *     <li>Support for batch delete operations that allow multiple entities to be deleted at once.</li>
 * </ul>
 * <p>
 * It is important to note that the delete operations in this package are not guaranteed to be supported by all NoSQL
 * databases. For example, key-value databases typically do not support condition-based deletions or other advanced
 * query operations that this package offers.
 * </p>
 * <p>
 * This package provides a consistent and high-level abstraction for deleting NoSQL data but requires ensuring that the
 * underlying database supports these operations before using them.
 * </p>
 *
 * @since 1.0.0
 * @see jakarta.nosql.QueryMapper.MapperDeleteFrom
 * @see jakarta.nosql.Template#delete(Class, Object)
 */
package jakarta.nosql.tck.delete;


