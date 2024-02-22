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
 * Provides classes and interfaces for integrating Java applications with various NoSQL databases.
 * <p>
 * NoSQL databases are a category of database systems that differ from traditional relational databases in their data
 * storage and retrieval mechanisms. Unlike relational databases, which organize data into tables with predefined schemas,
 * NoSQL databases use flexible data models that allow for unstructured or semi-structured data storage. These databases
 * are often preferred for their ability to handle large volumes of data, their scalability, and their flexibility in
 * accommodating evolving data schemas.
 * <p>
 * The {@link jakarta.nosql.Entity} annotation specifies that the class is an entity.
 * <p>
 * The {@link jakarta.nosql.Embeddable} annotation declares a class whose instances are stored as an intrinsic part of an
 * owning entity, sharing the identity of the entity.
 * <p>
 * The {@link jakarta.nosql.Id} annotation specifies the primary key of an entity.
 * <p>
 * The {@link jakarta.nosql.Column} annotation specifies the mapping of a persistent property or field to a database column.
 * <p>
 * The {@link jakarta.nosql.Convert} annotation specifies how the values of a field or property are converted to a basic
 * type or a type that can be persisted by a persistence provider.
 * <p>
 * The {@link jakarta.nosql.MappedSuperclass} annotation specifies a class whose mapping information is applied to entities
 * that inherit from it.
 * <p>
 * The {@link jakarta.nosql.Inheritance} annotation specifies the inheritance mapping strategy for the entity class hierarchy.
 * <p>
 * The {@link jakarta.nosql.DiscriminatorColumn} annotation specifies the discriminator column for the inheritance mapping
 * strategy.
 * <p>
 * The {@link jakarta.nosql.DiscriminatorValue} annotation specifies the value of the discriminator column for the annotated
 * entity type.
 */
package jakarta.nosql;