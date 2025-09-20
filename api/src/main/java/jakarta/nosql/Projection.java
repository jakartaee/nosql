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
package jakarta.nosql;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a record class as a projection for query results in Jakarta NoSQL.
 *
 * <p>This annotation allows Jakarta NoSQL to map query results to Java {@code record} types.
 * You can define projections by matching record components to the entity's fields,
 * or explicitly customize the mapping using {@link jakarta.nosql.Column}.</p>
 *
 * <p>There are two ways to map each projection component:</p>
 * <ul>
 *   <li><strong>By name:</strong> If the component name matches a property in the result set, it will be automatically mapped.</li>
 *   <li><strong>Using {@code @Column}:</strong> For nested attributes (e.g., associations or embedded objects) or to rename properties, use {@code @Column("path")}.
 *       This supports flattening or aliasing query results.</li>
 * </ul>
 *
 * <p>The target class must be a Java {@code record}, and this mapping is <strong>read-only</strong>.
 *
 * <p>If the query string omits the {@code FROM} clause, you can define the source entity using the {@link #from()} attribute.</p>
 *
 * <p>If both the annotation and the query define the entity source, the query string takes precedence.</p>
 *
 * <p>Examples:</p>
 *
 * <p><strong>1. Projection with automatic name matching:</strong></p>
 * <pre>{@code
 * @Projection
 * public record TechProductView(String name, double price) {}
 *
 * List<TechProductView> techProducts = template
 *     .typedQuery("FROM Product WHERE category = 'TECH'", TechProductView.class)
 *     .result();
 * }</pre>
 *
 * <p><strong>2. Projection with implicit {@code FROM} clause:</strong></p>
 * <pre>{@code
 * @Projection(from = Product.class)
 * public record PromotionalProduct(String name, double price, String category) {}
 *
 * List<PromotionalProduct> results = template
 *     .typedQuery("WHERE price < 100", PromotionalProduct.class)
 *     .result();
 * }</pre>
 *
 * <p><strong>3. Projection with nested and renamed attributes:</strong></p>
 * <pre>{@code
 * @Projection(from = Order.class)
 * public record OrderDetail(
 *     String id,
 *     @Column("user.name") String customerName,
 *     @Column("user.address.city") String city
 * ) {}
 * }</pre>
 *
 * <p><strong>4. Projection with renamed field:</strong></p>
 * <pre>{@code
 * @Projection(from = Product.class)
 * public record ProductBonus(@Column("price") BigDecimal money) {}
 * }</pre>
 *
 * <p>If a component name does not match any result field and is not annotated with {@code @Column},
 * an exception may be thrown at runtime depending on the provider.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Projection {

    /**
     * Specifies the source entity class that this projection is based on.
     * This is used when the query string does not explicitly include a {@code FROM} clause.
     *
     * <p>When this attribute is present, Jakarta NoSQL will infer the entity in the query and
     * automatically inject the {@code FROM} clause during query execution.</p>
     *
     * <p><strong>Note:</strong> The specified class must be annotated with {@code @Entity}.</p>
     *
     * <p>This enables simpler query definitions such as:
     * <pre>{@code
     * @Entity
     * public class Product {
     *     @Id
     *     private String id;
     *     @Column
     *     private String name;
     *     @Column
     *     private double price;
     *     @Column
     *     private ProductType type;
     * }
     *
     * @Projection(from = Product.class)
     * public record PromotionalProduct(String name, double price, ProductType type) {}
     *
     * List<PromotionalProduct> results = template
     *     .typedQuery("WHERE price < 100", PromotionalProduct.class)
     *     .result();
     * }</pre>
     * </p>
     *
     * <p>You may also use query parameters as usual:
     * <pre>{@code
     * List<PromotionalProduct> results = template
     *     .typedQuery("WHERE price < :maxPrice", PromotionalProduct.class)
     *     .bind("maxPrice", 100)
     *     .result();
     * }</pre>
     * </p>
     *
     * @return the source entity class, which must be annotated with {@code @Entity}
     */
    Class<?> from() default void.class;
}
