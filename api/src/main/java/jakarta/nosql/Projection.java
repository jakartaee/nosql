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
 * Declares a record class as a projection for query results.
 *
 * <p>This annotation enables Jakarta NoSQL to map query results to Java {@code record} types
 * using a query string. If the projection's record component names match fields in the entity,
 * you don't need to explicitly list the fields in the query.
 * The query engine will automatically map matching fields.</p>
 *
 * <ul>
 *   <li>If the query omits the {@code FROM} clause, the {@code from()} attribute defines the entity class to use.</li>
 *   <li>If both the query string and the annotation define the source entity, the {@code FROM} clause in the query takes precedence.</li>
 *   <li>The projection class must be a {@code record}.</li>
 * </ul>
 *
 * <p>Example 1 – using an explicit {@code FROM} clause:</p>
 * <pre>{@code
 * @Projection
 * public record TechProductView(String name, double price) {}

 * List<TechProductView> techProducts = template
 *     .typedQuery("FROM Product WHERE category = 'TECH'", TechProductView.class)
 *     .result();
 * }</pre>
 *
 * <p>Example 2 – using only a {@code WHERE} clause and specifying the source via {@code from()}:</p>
 * <pre>{@code
 * @Projection(from = Product.class)
 * public record PromotionalProduct(String name, double price, String type) {}

 * List<PromotionalProduct> results = template
 *     .typedQuery("WHERE price < 100", PromotionalProduct.class)
 *     .result();
 * }</pre>
 *
 * @see jakarta.nosql.TypedQuery
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Projection {

    Class<?> from() default void.class;
}
