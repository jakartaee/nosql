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
 * Defines the core API in mapping level.
 * It brings an easy interface to support key-value, column family, document oriented and graph
 * databases using as base Jakarta NoSQL in CDI based.
 * The API's focus is on simplicity and ease of use.
 * <ul>
 * <li>CDI Based</li>
 * <li>Jakarta NoSQL Communication Based</li>
 * <li>Events to insert, delete, update</li>
 * <li>Supports to Bean Validation</li>
 * <li>Annotation Driven Based</li>
 * <li>Configurable and extensible</li>
 </ul>
 * Developers should only have to know a minimal set of
 * artifacts to work with the solution.
 * The API is built on latest Java 8 features and therefore fit
 * perfectly with the functional features of Java 8.
 */
package jakarta.nosql.mapping;