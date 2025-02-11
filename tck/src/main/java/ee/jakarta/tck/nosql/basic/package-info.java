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
 * Provides the basic CRUD (Create, Read, Update, Delete) operations for interacting with NoSQL databases.
 * <p>
 * This package includes the fundamental operations that all NoSQL databases should support, including:
 * </p>
 * <ul>
 *     <li>{@link jakarta.nosql.Template#insert(Object)}: Insert an entity into the database.</li>
 *     <li>{@link jakarta.nosql.Template#update(Object)}: Update an existing entity in the database.</li>
 *     <li>{@link jakarta.nosql.Template#delete(Class, Object)}: Delete an entity from the database by its ID.</li>
 *     <li>{@link jakarta.nosql.Template#find(Class, Object)}: Retrieve an entity by its ID.</li>
 * </ul>
 * <p>
 * These operations are designed to work seamlessly across different NoSQL database implementations. The methods in this
 * package provide a uniform API for interacting with NoSQL databases, abstracting the underlying complexity of each NoSQL provider.
 * </p>
 * <p>
 * This package supports both single entity operations and batch operations for managing multiple entities at once.
 * It is part of a larger framework designed to simplify NoSQL operations and provide a consistent, high-level API for common use cases.
 * </p>
 *
 * @since 1.0.0
 * @see jakarta.nosql.Template
 */
package ee.jakarta.tck.nosql.basic;
