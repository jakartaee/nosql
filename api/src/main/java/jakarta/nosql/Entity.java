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
 * Annotates a class as an entity, representing a persistent class corresponding to a database structure.
 * <p>
 * Entity classes must adhere to specific rules:
 * <ul>
 * <li>At least one field must be annotated with {@link Id} or {@link Column}.</li>
 * <li>Constructors must be {@code public} or {@code protected} with no parameters, or with parameters annotated with {@link Column} or {@link Id}.</li>
 * <li>Annotations at the constructor will build the entity and read information from the database, while field annotations are required to write information to the database.</li>
 * <li>If both a non-args constructor and a constructor with annotated parameters exist, the constructor with annotations will be used to create the entity.</li>
 * <li>Constructor parameters without annotations will be ignored, utilizing a non-arg constructor instead.</li>
 * <li>Entities should not have multiple constructors using {@link Id} or {@link Column} annotations.</li>
 * <li>Record classes can also serve as entities.</li>
 * </ul>
 * <p>
 * Enums or interfaces cannot be designated as entities.
 * </p>
 * <p>
 * Each entity must have a unique identifier, typically annotated with {@link Id}.
 * </p>
 * <p>
 * The following example demonstrates two classes, {@code Person} and {@code Address}, where a person has an address:
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
 * Note: NoSQL databases may have varying behaviors regarding serialization, resulting in different storage formats based on the NoSQL vendor.
 * </p>
 * <p>
 * The sample below demonstrates the use of records as entities:
 * </p>
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
 *
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