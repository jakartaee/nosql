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
 * {@code Query} represents a string-based query execution API built on top of
 * the Jakarta Query Core language and integrated with Jakarta NoSQL.
 *
 * <p>This interface supports:
 * <ul>
 *     <li>Binding parameters by name or position</li>
 *     <li>Execution of queries using fluent APIs</li>
 *     <li>Result retrieval as {@code List}, {@code Stream}, or {@code Optional}</li>
 *     <li>Write operations via {@link #executeUpdate()}</li>
 * </ul>
 *
 * <h3>Jakarta Query Compatibility</h3>
 * This query engine follows the Jakarta Query Core specification. Its behavior
 * is intended to be portable across different Jakarta NoSQL-compatible databases.
 *
 * <h3>Vendor Support</h3>
 * Not all NoSQL databases support every feature. If a query feature is unsupported by the underlying database provider,
 * this interface will throw a {@link UnsupportedOperationException}.
 *
 * <h3>Query Type Validation</h3>
 * <ul>
 *     <li>{@link #result()}, {@link #stream()}, and {@link #singleResult()} can only be used with {@code SELECT} queries</li>
 *     <li>{@link #executeUpdate()} must be used for {@code UPDATE} or {@code DELETE} queries only</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @Inject
 * Template template;
 *
 * List<Person> people = template.query("SELECT * FROM Person WHERE country = :country", Person.class)
 *                               .bind("country", "Portugal")
 *                               .result();
 * }</pre>
 */
public interface Query {

    /**
     * Executes a write operation query (such as {@code UPDATE} or {@code DELETE}).
     *
     * <p>If the query is a {@code SELECT}, this method will throw an {@link UnsupportedOperationException}.
     * </p>
     *
     * <pre>{@code
     * template.query("DELETE FROM Person WHERE age < :minAge", Person.class)
     *         .bind("minAge", 18)
     *         .executeUpdate();
     * }</pre>
     *
     * @throws UnsupportedOperationException if the query is a {@code SELECT}, or the operation is not supported by the provider.
     */
    void executeUpdate();

    /**
     * Executes a {@code SELECT} query and returns the result as a {@link List}.
     *
     * <p>If the query is an {@code UPDATE} or {@code DELETE}, this method throws an {@link UnsupportedOperationException}.</p>
     *
     * <pre>{@code
     * List<Person> adults = template.query("SELECT * FROM Person WHERE age >= :minAge", Person.class)
     *                               .bind("minAge", 18)
     *                               .result();
     * }</pre>
     *
     * @param <T> the type of the entity
     * @return list of results or an empty list
     * @throws UnsupportedOperationException if the query is not a {@code SELECT}
     */
    <T> List<T> result();

    /**
     * Executes a {@code SELECT} query and returns the result as a {@link Stream}.
     *
     * <p>If the query is an {@code UPDATE} or {@code DELETE}, this method throws an {@link UnsupportedOperationException}.</p>
     *
     * <pre>{@code
     * try (Stream<Person> stream = template.query("SELECT * FROM Person WHERE active = true", Person.class)
     *                                       .stream()) {
     *     stream.forEach(System.out::println);
     * }
     * }</pre>
     *
     * @param <T> the type of the entity
     * @return a stream of results
     * @throws UnsupportedOperationException if the query is not a {@code SELECT}
     */
    <T> Stream<T> stream();

    /**
     * Executes a {@code SELECT} query and returns a single result wrapped in an {@link Optional}.
     *
     * <p>If no result is found, returns {@link Optional#empty()}. If more than one result is found,
     * the behavior is database-specific and may throw an exception.</p>
     *
     * <p>If the query is an {@code UPDATE} or {@code DELETE}, this method throws an {@link UnsupportedOperationException}.</p>
     *
     * <pre>{@code
     * Optional<Person> person = template.query("SELECT * FROM Person WHERE id = :id", Person.class)
     *                                   .bind("id", "p-001")
     *                                   .singleResult();
     * }</pre>
     *
     * @param <T> the type of the entity
     * @return optional result
     * @throws UnsupportedOperationException if the query is not a {@code SELECT}
     */
    <T> Optional<T> singleResult();

    /**
     * Binds a named parameter to the query.
     *
     * <pre>{@code
     * Query query = template.query("SELECT * FROM Book WHERE title = :title", Book.class)
     *                       .bind("title", "Effective Java");
     * }</pre>
     *
     * @param name  the parameter name (without {@code :})
     * @param value the value to bind
     * @return this query instance for fluent chaining
     * @throws NullPointerException if the name is null
     */
    Query bind(String name, Object value);

    /**
     * Binds a positional parameter to the query. Positions are 1-based.
     *
     * <pre>{@code
     * Query query = template.query("SELECT * FROM Person WHERE age > ?1", Person.class)
     *                       .bind(1, 30);
     * }</pre>
     *
     * @param position the parameter position (starting at 1)
     * @param value    the value to bind
     * @return this query instance for fluent chaining
     * @throws IllegalArgumentException if position is less than 1
     */
    Query bind(int position, Object value);
}
