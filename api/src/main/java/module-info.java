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
/**
 * <b>Jakarta NoSQL</b>
 *
 * <p>Jakarta NoSQL is designed specifically for Java developers who need to build scalable, database-agnostic applications using NoSQL technologies. With Jakarta NoSQL, developers can seamlessly map Java objects to NoSQL databases using familiar paradigms, enabling them to focus on business logic without worrying about database-specific implementation details.</p>
 *
 * <b>Overview</b>
 * <p>Modern applications often require highly scalable and flexible storage solutions. NoSQL databases address these needs with their diverse data models (e.g., document, key-value, column-family, and graph). However, interacting with such databases in a type-safe, database-agnostic way has historically been challenging. Jakarta NoSQL solves this problem by offering:</p>
 * <ul>
 * <li>A standard API for NoSQL database interactions.</li>
 * <li>Comprehensive support for mapping Java objects to NoSQL data models.</li>
 * <li>Seamless integration with Java frameworks and runtime environments.</li>
 * </ul>
 *
 * <b>Annotations</b>
 * <p>Jakarta NoSQL introduces several annotations to make it easier for Java developers to define mappings between Java objects and NoSQL databases. These include:</p>
 * <ul>
 * <li>{@link jakarta.nosql.Entity}: Marks a class as a NoSQL entity, representing a top-level record in the database.</li>
 * <li>{@link jakarta.nosql.Id}: Specifies the primary key or unique identifier for an entity.</li>
 * <li>{@link jakarta.nosql.Column}: Maps a field or property to a column or attribute in the database.</li>
 * <li>{@link jakarta.nosql.Convert}: Specifies a custom converter for handling complex or non-standard data types.</li>
 * <li>{@link jakarta.nosql.Embeddable}: Denotes a reusable embedded structure for entities.</li>
 * <li>{@link jakarta.nosql.DiscriminatorColumn} and {@link jakarta.nosql.DiscriminatorValue}: Enable inheritance mapping for NoSQL entities.</li>
 * <li>{@link jakarta.nosql.MappedSuperclass}: Defines a superclass that provides shared mapping information for its subclasses.</li>
 * </ul>
 *
 * <b>Purpose and Advantages</b>
 * <p>Jakarta NoSQL was created with the following goals in mind:</p>
 * <ol>
 * <li><b>Standardization</b>: Provide a consistent API across different NoSQL database types, enabling portability and reducing vendor lock-in.</li>
 * <li><b>Ease of Use</b>: Allow Java developers to interact with NoSQL databases using familiar paradigms like annotations and type-safe APIs.</li>
 * <li><b>Flexibility</b>: Support diverse NoSQL database models and allow developers to extend functionality through custom converters and mappings.</li>
 * <li><b>Productivity</b>: Reduce boilerplate code and simplify common operations like queries, updates, and object mapping.</li>
 * </ol>
 *
 * <b>Features</b>
 * <ul>
 * <li><b>Object Mapping</b>: Define how Java objects map to NoSQL database structures using annotations like {@code @Entity}, {@code @Column}, and {@code @Id}.</li>
 * <li><b>Custom Conversions</b>: Handle complex data types with the {@code @Convert} annotation and the {@code AttributeConverter} interface.</li>
 * <li><b>Inheritance Support</b>: Use {@code @MappedSuperclass}, {@code @DiscriminatorColumn}, and {@code @DiscriminatorValue} to model entity inheritance.</li>
 * <li><b>Seamless Integration</b>: Integrates with Java EE/Jakarta EE environments and modern frameworks, ensuring compatibility and ease of adoption.</li>
 * </ul>
 *
 * <b>Example Usage</b>
 * <p>Below is an example of a typical entity using Jakarta NoSQL annotations and a simple template integration to perform basic operations:</p>
 *
 * <pre>
 * {@code
 * @Entity
 * public class Product {
 *
 *     @Id
 *     private String id;
 *
 *     @Column
 *     private String name;
 *
 *     @Column
 *     private Category category;
 * }
 * public enum Category {
 *    TECH, FOOD, CLOTHING
 * }
 *
 * @Inject
 * private Template template;
 *
 * public void performOperations() {
 *     Product product = Product.builder().id("1").name("Laptop").category(Category.TECH).build();
 *
 *     // Insert operation
 *     template.insert(product);
 *
 *     // Find operation
 *     Optional<Product> foundProduct = template.find(Product.class, "1");
 *
 *     // Delete operation
 *     template.delete(Product.class, "1");
 *
 *     List<Product> products = template.select(String.class).where("category").eq(Category.TECH).result();
 * }
 * }
 * </pre>
 */
module jakarta.nosql.core {
    exports jakarta.nosql;
}