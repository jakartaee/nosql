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
 * Annotates a class as an entity, representing a persistent domain object stored in a NoSQL database.
 *
 * <p>Entity classes are associated with a database structure, such as a <em>collection</em>, <em>bucket</em>,
 * <em>column family</em>, or <em>edge label</em>, depending on the type of NoSQL database.
 * By default, the entity structure name is inferred from the class name, but you can customize it using {@link #value()}.</p>
 *
 * <p>When writing queries using Jakarta Common Query Language (JCQL), the name used in the `FROM` clause is inferred from the class name or
 * explicitly defined using the {@link #name()} attribute. This is useful when multiple classes share the same simple name
 * or when you want a more semantic alias for the query layer.</p>
 *
 * <p>Entity classes must follow these rules:</p>
 * <ul>
 *   <li>Must include at least one field annotated with {@link jakarta.nosql.Id} or {@link jakarta.nosql.Column}.</li>
 *   <li>Can use either fields or constructors to define persistence mappings.</li>
 *   <li>If a constructor is annotated with {@link jakarta.nosql.Column} or {@link jakarta.nosql.Id}, it will be used to instantiate the entity.</li>
 *   <li>If both a no-arg constructor and an annotated constructor are present, the annotated one is preferred.</li>
 *   <li>Parameters without annotations in constructors are ignored.</li>
 *   <li>Only one constructor should contain mapping annotations.</li>
 *   <li>Records may also be annotated as entities.</li>
 * </ul>
 *
 * <p>Interfaces and enums cannot be used as entities.</p>
 *
 * <p>Example using class and embedded entity:</p>
 * <pre>{@code
 * @Entity("people")
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
 *
 *     @Column
 *     private String street;
 *
 *     @Column
 *     private String city;
 * }
 * }</pre>
 *
 * <p>Example using records and a custom query name:</p>
 * <pre>{@code
 * @Entity("Contacts")
 * public record Person(@Id Long id, @Column String name, @Column Address address) {
 * }
 *
 * // Query usage
 * List<Person> people = template.query("FROM Contacts WHERE name = 'Ada'").result();
 * }</pre>
 *
 * <p><strong>Note:</strong> NoSQL providers may serialize entities differently based on the underlying database engine.</p>
 *
 * @see jakarta.nosql.Id
 * @see jakarta.nosql.Column
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {

    /**
     * Defines the name of the NoSQL structure used to persist this entity.
     * <p>If not specified, it defaults to the simple (unqualified) class name.</p>
     *
     * <p>The actual structure name depends on the NoSQL database type, for example:</p>
     * <ul>
     *   <li>Document: {@code collection}</li>
     *   <li>Key-Value: {@code bucket}</li>
     *   <li>Wide-column: {@code column family}</li>
     *   <li>Graph: {@code vertex label} or {@code edge label}</li>
     * </ul>
     *
     * <p>Example:</p>
     * <pre>{@code
     * @Entity("product_bucket")
     * public class Product {
     *     @Id
     *     private String id;
     *
     *     @Column
     *     private String name;
     * }
     * }</pre>
     *
     * @return the NoSQL structure name
     */
    String value() default "";


    /**
     * Defines the logical name used to reference this entity in  Jakarta Common Query Language (JCQL).
     * <p>This is useful when multiple entity classes share the same simple name in different packages,
     * or when you want a custom name for use in queries.</p>
     * <p>If not specified, it defaults to the simple class name.</p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * @Entity(name = "CatalogItem")
     * public class Product {
     *     @Id
     *     private String id;
     *
     *     @Column
     *     private String category;
     * }
     *
     * // Usage in query:
     * List<Product> items = template.query("FROM CatalogItem WHERE category = 'TECH'").result();
     * }</pre>
     *
     * @return the entity name used in query language
     * @since 1.1.0
     */
    String name() default "";
}