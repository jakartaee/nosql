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
 * A type-safe variant of {@link Query} that maps results to a target type {@code T}, which can be either:
 * <ul>
 *   <li>An entity class annotated with {@code @Entity}</li>
 *   <li>A projection class annotated with {@code @Projection}</li>
 * </ul>
 *
 * <p>This interface provides a fluent and type-safe API for querying using the Jakarta Query Core language.
 * It avoids the need for casting when processing results, since the return type is already defined as {@code T}.</p>
 *
 * <p>When using Jakarta Query Core, the {@code FROM} clause can be omitted in the query string if the target class
 * is specified via the {@code from} attribute of the {@link jakarta.nosql.Projection} annotation.</p>
 *
 * <p>The target class {@code T} must match the structure of the query result:
 * <ul>
 *   <li>If {@code T} is an entity class, and the query contains a {@code FROM} clause, the entity type in the query must match {@code T}.</li>
 *   <li>If {@code T} is a projection, its components must match the query result by name, or be annotated with {@link jakarta.nosql.Column} to map fields explicitly.</li>
 * </ul>
 * Failing to match the entity or projection class with the result may cause runtime exceptions, depending on the provider.</p>
 *
 * <p>Example – query using a projection:</p>
 * <pre>{@code
 * @Projection
 * public record TechProductView(String name, double price) {}
 *
 * List<TechProductView> products = template
 *     .typedQuery("FROM Product WHERE category = 'TECH'", TechProductView.class)
 *     .result();
 * }</pre>
 *
 * <p>Example – query using an entity class:</p>
 * <pre>{@code
 * List<Product> products = template
 *     .typedQuery("WHERE price < 100", Product.class)
 *     .result();
 * }</pre>
 *
 * @param <T> the type of the result objects
 * @since 1.1.0
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
     * try (Stream<TechProductView> stream = template
     *         .typedQuery("FROM Product WHERE active = true", TechProductView.class)
     *         .stream()) {
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
     *     .typedQuery("FROM Product WHERE id = :id", Product.class)
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
