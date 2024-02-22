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
package jakarta.nosql;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Defines a class whose mapping information is applied to entities that inherit from it.
 * Unlike regular entities, a mapped superclass does not imply the existence of separate storage
 * structures.
 *
 * <p>Declares a class that is not itself an entity, but whose mappings are inherited by entities that extend it.
 * A mapped superclass is not a persistent type and is not mapped to a specific database structure.
 *
 * <p>The persistent fields and properties of a mapped superclass are declared and mapped using the same mapping
 * annotations used to map {@linkplain Entity entity classes}. However, these mappings are interpreted in the context
 * of each entity class that inherits the mapped superclass, since the mapped superclass itself does not have a
 * predefined storage structure in NoSQL databases.
 *
 * <p>Example:</p>
 * <pre>{@code
 * @MappedSuperclass
 * public class Employee {
 *     @Id
 *     protected Integer id;
 *     @Column
 *     protected Address address;
 * }
 *
 * @Entity
 * public class Programmer extends Employee {
 *     @Column
 *     protected Money salary;
 * }
 * }</pre>
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedSuperclass {

}


