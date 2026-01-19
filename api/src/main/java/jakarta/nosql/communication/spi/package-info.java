/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
 * Provides a low-level Service Provider Interface (SPI) for expressing
 * communication intent with NoSQL databases in a structured and provider-neutral
 * manner.
 *
 * <p>This package defines a minimal set of abstractions whose primary purpose
 * is to act as a <em>communication wrapper</em> or <em>adapter layer</em>
 * between higher-level frameworks and underlying NoSQL database providers.</p>
 *
 * <p>The APIs in this package do <strong>not</strong> attempt to standardize
 * query languages, mutation semantics, consistency models, or execution
 * guarantees. Instead, they define the <em>shape</em> of interaction —
 * how selection, update, and delete intentions are expressed — while leaving
 * all semantic interpretation to the provider.</p>
 *
 * <p>This design acknowledges the fundamental diversity of NoSQL systems.
 * Document, key-value, column-family, graph, and other NoSQL databases expose
 * different data models, capabilities, and operational constraints. Any attempt
 * to impose a unified semantic model would either oversimplify these systems or
 * restrict their native strengths.</p>
 *
 * <p>As a result, this package intentionally models operations using opaque,
 * provider-defined tokens (such as {@code Condition}, {@code Order}, and
 * update descriptors) and fluent execution contracts. Providers may fully
 * implement, partially implement, or reject specific operations, typically
 * signaling unsupported capabilities via {@link java.lang.UnsupportedOperationException}.</p>
 *
 * <p>The primary consumers of this SPI are expected to be:</p>
 * <ul>
 *   <li>Frameworks and runtime integrations</li>
 *   <li>Higher-level Jakarta specifications (such as data access or query layers)</li>
 *   <li>Infrastructure components, adapters, and portability layers</li>
 *   <li>Testing, tooling, and observability integrations</li>
 * </ul>
 *
 * <p>This package is <strong>not</strong> intended to be a direct application
 * programming model. Application-facing APIs are expected to be built on top
 * of this SPI, adding domain semantics, validation, and convenience methods
 * as appropriate.</p>
 *
 * <p>In summary, this package defines a stable, minimal, and extensible
 * contract for communicating intent to NoSQL systems, without constraining
 * provider implementations or masking the inherent variability of NoSQL
 * technologies.</p>
 */
package jakarta.nosql.communication.spi;