/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
 * Provides supplier classes for generating test data for NoSQL operations.
 * <p>
 * The supplier classes in this package are responsible for supplying entities or lists of entities
 * for parameterized tests. These classes follow a consistent naming convention:
 * </p>
 * <ul>
 *     <li>{@link ee.jakarta.tck.nosql.factories.VehicleSupplier}: Supplies a single instance of the {@link ee.jakarta.tck.nosql.entities.Vehicle} entity for tests.</li>
 *     <li>{@link ee.jakarta.tck.nosql.factories.VehicleListSupplier}: Supplies a list of {@link ee.jakarta.tck.nosql.entities.Vehicle} entities for batch tests.</li>
 *     <li>{@link ee.jakarta.tck.nosql.factories.PersonSupplier}: Supplies a single instance of the {@link ee.jakarta.tck.nosql.entities.Person} entity for tests.</li>
 *     <li>{@link ee.jakarta.tck.nosql.factories.PersonListSupplier}: Supplies a list of {@link ee.jakarta.tck.nosql.entities.Person} entities for batch tests.</li>
 * </ul>
 * <p>
 * The naming convention used is as follows:
 * </p>
 * <ul>
 *     <li>If the supplier returns a single entity, it is named <code>EntitySupplier</code>, where <code>Entity</code> is the name of the entity
 *     (e.g., <code>VehicleSupplier</code> for the {@link ee.jakarta.tck.nosql.entities.Vehicle} entity).</li>
 *     <li>If the supplier returns a list of entities, it is named <code>EntityListSupplier</code>, where <code>Entity</code>
 *     is the name of the entity (e.g., <code>VehicleListSupplier</code> for a list of {@link ee.jakarta.tck.nosql.entities.Vehicle} entities).</li>
 * </ul>
 * <p>
 * These supplier classes are designed to provide the necessary test data for various NoSQL operations
 * across different database types.
 * </p>
 *
 * @since 1.0.0
 * @see ee.jakarta.tck.nosql.factories.VehicleSupplier
 * @see ee.jakarta.tck.nosql.factories.VehicleListSupplier
 * @see ee.jakarta.tck.nosql.factories.PersonSupplier
 * @see ee.jakarta.tck.nosql.factories.PersonListSupplier
 */
package ee.jakarta.tck.nosql.factories;
