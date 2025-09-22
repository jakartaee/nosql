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
 * Declares a Java {@code record} as a projection for query results in Jakarta NoSQL.
 *
 * <p>This annotation allows Jakarta NoSQL to map query results to Java record types
 * without requiring explicit selection of attributes in the query string, provided that:
 * <ul>
 *   <li>Each projection component matches a property returned by the query by name, or</li>
 *   <li>The component is annotated with {@link jakarta.nosql.Column} to map to a different or nested path.</li>
 * </ul>
 *
 * <p>If the {@code FROM} clause is omitted in the query, the optional {@link #from()} attribute can be
 * used to specify the source entity class. If both the query and the annotation define an entity, the query takes precedence.</p>
 *
 * <p>The target class must be a {@code record} with components that match the query result set either by name
 * or by explicit column mapping using {@code @Column}.</p>
 *
 * <p>This mapping is <strong>read-only</strong> and is only applicable for query results.
 * It does not affect inserts, updates, or write operations.</p>
 *
 * <p><b>Examples:</b></p>
 *
 * <p>Using implicit mapping where component names match entity properties:</p>
 * <pre>{@code
 * @Projection
 * public record TechProductView(String name, double price) {}
 *
 * List<TechProductView> techProducts = template
 *     .typedQuery("FROM Product WHERE category = 'TECH'", TechProductView.class)
 *     .result();
 * }</pre>
 *
 * <p>Using the {@code from} attribute to specify the source entity when omitted from the query:</p>
 * <pre>{@code
 * @Projection(from = Product.class)
 * public record PromotionalProduct(String name, double price, String category) {}
 *
 * List<PromotionalProduct> promotions = template
 *     .typedQuery("WHERE price < 100", PromotionalProduct.class)
 *     .result();
 * }</pre>
 *
 * <p>Mapping nested or renamed attributes using {@link jakarta.nosql.Column}:</p>
 * <pre>{@code
 * @Projection(from = Order.class)
 * public record OrderSummary(
 *     String id,
 *     @Column("user.name") String customerName,
 *     @Column("user.address.city") String city,
 *     @Column("total") BigDecimal amount
 * ) {}
 * }</pre>
 *
 * <p>You can also override simple field names with {@code @Column} even when they are not nested:</p>
 * <pre>{@code
 * @Projection(from = Product.class)
 * public record ProductBonus(
 *     @Column("price") BigDecimal bonusAmount
 * ) {}
 * }</pre>
 *
 * @see jakarta.nosql.Column
 * @since 1.1.0
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
     * <p>This enables simpler query definitions such as:</p>
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
     *
     * <p>You may also use query parameters as usual:</p>
     * <pre>{@code
     * List<PromotionalProduct> results = template
     *     .typedQuery("WHERE price < :maxPrice", PromotionalProduct.class)
     *     .bind("maxPrice", 100)
     *     .result();
     * }</pre>
     *
     * @return the source entity class, which must be annotated with {@code @Entity}
     */
    Class<?> from() default void.class;
}
