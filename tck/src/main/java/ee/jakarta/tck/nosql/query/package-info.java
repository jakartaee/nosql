/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/**
 * Provides Technology Compatibility Kit (TCK) tests for validating the
 * integration between Jakarta Query and Jakarta NoSQL.
 *  This package focuses on verifying that Jakarta NoSQL implementations
 * correctly interpret and execute Jakarta Query constructs when mapped
 * to NoSQL data stores. The tests exercise the core query semantics,
 * ensuring that query parsing, mapping, parameter binding, and execution
 * behave consistently with the Jakarta Query specification.
 * The tests in this package are intentionally database-agnostic and do
 * not assume support for advanced or vendor-specific query capabilities.
 * Instead, they validate the common contract defined by Jakarta Query
 * when used through Jakarta NoSQL abstractions, such as typed queries,
 * fluent query builders, and metadata-driven execution.
 *
 * <p>Typical scenarios covered include:</p>
 * <ul>
 *   <li>Execution of typed queries defined through Jakarta Query APIs</li>
 *   <li>Correct mapping of query parameters and result types</li>
 *   <li>Interaction between query metadata and Jakarta NoSQL templates</li>
 *   <li>Behavior of query execution in the Jakarta NoSQL core layer</li>
 * </ul>
 *
 * <p>
 * These tests are designed to be reused across different Jakarta NoSQL
 * implementations to ensure specification compliance without coupling
 * the TCK to a particular storage model or query language.
 * </p>
 *
 * @see jakarta.nosql.Template
 * @see jakarta.nosql.Query
 * @see jakarta.nosql.TypedQuery
 * @see jakarta.nosql.Projection
 */
package ee.jakarta.tck.nosql.query;