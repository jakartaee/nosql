/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/**
 * Provides the TCK tests for query function expressions in Jakarta NoSQL.
 * <p>
 * This package includes tests for scalar functions defined in the Jakarta Query specification, including:
 * </p>
 * <ul>
 *     <li>{@link jakarta.nosql.Function#left(String, int)}: Extract leftmost characters.</li>
 *     <li>{@link jakarta.nosql.Function#right(String, int)}: Extract rightmost characters.</li>
 *     <li>{@link jakarta.nosql.Function#upper(String)}: Convert to uppercase.</li>
 *     <li>{@link jakarta.nosql.Function#lower(String)}: Convert to lowercase.</li>
 *     <li>{@link jakarta.nosql.Function#length(String)}: Get string length.</li>
 *     <li>{@link jakarta.nosql.Function#abs(String)}: Absolute value.</li>
 * </ul>
 * <p>
 * These tests ensure that NoSQL providers correctly handle function expressions in SELECT, UPDATE,
 * and DELETE queries, or throw the appropriate {@link jakarta.nosql.UnsupportedFunctionException}
 * if the underlying database does not support them.
 * </p>
 *
 * @since 1.1.0
 * @see jakarta.nosql.Function
 * @see jakarta.nosql.QueryMapper
 */
package ee.jakarta.tck.nosql.function;
