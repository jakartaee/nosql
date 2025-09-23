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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A type-safe variant of {@link Query} that maps query results directly to the target type {@code T}.
 * This type can be either:
 * <ul>
 *   <li>An entity class annotated with {@code @Entity}</li>
 *   <li>A projection class annotated with {@link jakarta.nosql.Projection}</li>
 * </ul>
 *
 * <p>One of the key benefits of using {@code TypedQuery} is that the {@code FROM} clause can be omitted
 * when the provider is able to infer the source entity from the {@code T} type:</p>
 * <ul>
 *   <li>If {@code T} is an entity, the {@code FROM} clause is optional.</li>
 *   <li>If {@code T} is a projection with the {@code @Projection(from = ...)} annotation, the entity source is inferred from it.</li>
 * </ul>
 * <p>This simplifies queries significantly and avoids boilerplate.</p>
 *
 * <p><strong>Important:</strong> When using a {@code FROM} clause explicitly in the query string,
 * the entity specified in the query must match the class provided as {@code T}.
 * For example, the following is <strong>invalid</strong> and must raise an error:</p>
 * <pre>{@code
 * // Assuming Cat and Dog are both entities
 * template.typedQuery("FROM Cat", Dog.class); //Must fail
 * }</pre>
 * <p>This validation ensures consistency between the declared return type and the query structure.</p>
 *
 * <p>Examples:</p>
 *
 * <p>Using entity type with inferred FROM clause:</p>
 * <pre>{@code
 * List<Product> products = template
 *     .typedQuery("WHERE category = 'TECH'", Product.class)
 *     .result();
 * }</pre>
 *
 * <p>Using projection type with inferred FROM clause:</p>
 * <pre>{@code
 * @Projection(from = Product.class)
 * public record PromotionalProduct(String name, double price) {}
 *
 * List<PromotionalProduct> promos = template
 *     .typedQuery("WHERE price < 100", PromotionalProduct.class)
 *     .result();
 * }</pre>
 *
 * @param <T> the type of the result objects
 */
public interface TypedQuery<T> extends Query {

    /**
     * Executes the {@code SELECT} query and returns the results as a {@link List} of type {@code T}.
     *
     * <p>This method can only be used with {@code SELECT} queries.
     * It throws an {@link UnsupportedOperationException} if called for a non-select query such as {@code UPDATE} or {@code DELETE}.</p>
     *
     * <pre>{@code
     * List<TechProductView> results = template
     *     .typedQuery("FROM Product WHERE category = 'TECH'", TechProductView.class)
     *     .result();
     * }</pre>
     *
     * @return list of results or an empty list
     * @throws UnsupportedOperationException if the query is not a {@code SELECT}
     */
    @Override
    List<T> result();

    /**
     * Executes the {@code SELECT} query and returns the results as a {@link Stream} of type {@code T}.
     *
     * <p>This is useful for processing large result sets in a streaming fashion.
     * It throws an {@link UnsupportedOperationException} if the query is not a {@code SELECT}.</p>
     *
     * <pre>{@code
     *    Stream<TechProductView> stream = template
     *         .typedQuery("FROM Product WHERE active = true", TechProductView.class)
     *         .stream();
     *     stream.forEach(System.out::println);
     * }
     * }</pre>
     *
     * @return stream of results
     * @throws UnsupportedOperationException if the query is not a {@code SELECT}
     */
    @Override
    Stream<T> stream();

    /**
     * Executes the {@code SELECT} query and returns a single result wrapped in an {@link Optional}.
     *
     * <p>If no result is found, returns {@link Optional#empty()}.
     * If more than one result is found, the behavior is provider-specific and may result in an exception.</p>
     *
     * <pre>{@code
     * Optional<Product> product = template
     *     .typedQuery("WHERE id = :id", Product.class)
     *     .bind("id", "p-42")
     *     .singleResult();
     * }</pre>
     *
     * @return optional containing the result or empty
     * @throws UnsupportedOperationException if the query is not a {@code SELECT}
     */
    @Override
    Optional<T> singleResult();
}
