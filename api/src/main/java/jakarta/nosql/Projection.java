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
 * <p>This annotation allows Jakarta NoSQL to map query results to Java {@code record} types
 * without requiring explicit selection of fields in the query string—provided that
 * the record component names match the corresponding fields or properties returned by the query.</p>
 *
 * <p>Matching is strictly based on name: each component in the {@code record} must correspond to a field
 * returned by the query. If a component name does not match any field in the result set,
 * the query will fail at runtime, depending on the Jakarta NoSQL provider.</p>

 *
 * <ul>
 *   <li>If the query omits the {@code FROM} clause, the {@code from()} attribute specifies the default entity class.</li>
 *   <li>If both the query and annotation define a source entity, the query string takes precedence.</li>
 *   <li>The target class must be a Java {@code record}.</li>
 * </ul>
 *
 * <p>Example – using a projection without selecting fields explicitly:</p>
 * <pre>{@code
 * @Projection
 * public record TechProductView(String name, double price) {}

 * List<TechProductView> techProducts = template
 *     .typedQuery("FROM Product WHERE category = 'TECH'", TechProductView.class)
 *     .result();
 * }</pre>
 *
 * <p>Example – defining the source entity with {@code from()} when the query omits {@code FROM}:</p>
 * <pre>{@code
 * @Projection(from = Product.class)
 * public record PromotionalProduct(String name, double price, String type) {}

 * List<PromotionalProduct> results = template
 *     .typedQuery("WHERE price < 100", PromotionalProduct.class)
 *     .result();
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Projection {

    /**
     * Specifies the entity class this projection is based on.
     * Used when the query string omits a {@code FROM} clause.
     *
     * <p>Example:</p>
     * <pre>{@code
     * @Projection(from = Product.class)
     * public record PromotionalProduct(String name, double price, String type) {}
     * }</pre>
     *
     * @return the source entity class
     */
    Class<?> from() default void.class;
}
