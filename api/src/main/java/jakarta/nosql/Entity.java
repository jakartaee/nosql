/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
 * Declares that the annotated class is an entity. An entity represents a persistent class that corresponds to a structure in the database.
 * <p>
 * An entity class must adhere to certain rules:
 * <ul>
 * <li>It must have at least one field annotated with {@link Id} or {@link Column}.</li>
 * <li>It must have a {@code public} or {@code protected} constructor with no parameters, or a constructor with parameters annotated with {@link Column} or {@link Id}.</li>
 * <li>The annotations at the constructor will work to build the entity and, thus, read information from the database. The field annotations are still required to write information to the database.</li>
 * <li>In the case of both a non-args constructor and a constructor with parameters with annotations, it will use the constructor with annotation to create the entity. </li>
 * <li>It will ignore the constructor parameter without annotations. Thus, it will use a non-arg constructor.</li>
 * <li>An entity should not have multiple constructor using {@link Id} or {@link Column}</li>
 * <li>A record class can be an entity</li>
 * </ul>
 * <p>
 * Enums or interfaces cannot be designated as entities.
 * </p>
 * <p>
 * Each entity must have a unique identifier, typically a field annotated with {@link Id}.
 * </p>
 * <p>
 * The state of an entity is represented by its persistent fields and properties. By default, fields or properties of an entity class are not persistent.
 * </p>
 * <p>
 * The sample below demonstrates two entities, {@code Person} and {@code Address}, where a person has an address:
 * </p>
 * <pre>{@code
 * @Entity
 * public class Person {
 *
 *     @Id
 *     private Long id;
 *
 *     @Column
 *     private String name;
 *
 *     @Column
 *     private Address address;
 * }
 *
 * @Embeddable
 * public class Address {
 *     @Column
 *     private String street;
 *
 *     @Column
 *     private String city;
 * }
 * }</pre>
 * <p>
 * However, it’s important to note that NoSQL databases may have varying behaviors, so the serialization method may differ depending on the NoSQL vendor.
 * For example, in a document database, these entities may be converted into a sub-document, while in a key-value store, they will be stored as the value:
 * </p>
 * <pre>{@code
 * {
 *     "_id": 10,
 *     "name": "Ada Lovelace",
 *     "address": {
 *          "city": "São Paulo",
 *          "street": "Av Nove de Julho"
 *     }
 * }
 * }</pre>
 * The record sample:
 * <pre>{@code
 * @Entity
 * public record Person(@Id Long id, @Column String name, @Column Address address) {
 * }
 *
 * @Embeddable
 * public class Address {
 *     @Column
 *     private String street;
 *
 *     @Column
 *     private String city;
 *
 *     public Address(@Column String street, @Column String city) {
 *         this.street = street;
 *         this.city = city;
 *     }
 * }
 * }</pre>
 * @see Id
 * @see Column
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    /**
     * The name of the entity. If not specified, defaults to the unqualified simple name of the class.
     * This name is used to refer to the entity in queries and also to represent the name in the NoSQL database structure, such as the table name or collection name.
     * <p>
     * For example, given the class {@code org.jakarta.nosql.demo.Person}, the default name will be {@code Person}.
     * </p>
     * <p>
     * To customize the name, set the value of the {@code @Entity} annotation to the desired name, as shown below:
     * <pre>{@code
     * @Entity("ThePerson")
     * public class Person {
     * }
     * }</pre>
     *
     * @return the entity name (optional)
     */
    String value() default "";
}
