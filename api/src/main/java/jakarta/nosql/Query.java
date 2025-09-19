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
 * {@code Query} represents a string-based query execution interface used in Jakarta NoSQL.
 * It provides a consistent mechanism for dynamically executing queries using either
 * named or positional parameters, and supports core query operations such as {@code SELECT},
 * {@code UPDATE}, and {@code DELETE}.
 *
 * This API is based on Jakarta Query and supports the Jakarta Query Core language,
 * including clauses such as {@code SELECT}, {@code WHERE}, {@code ORDER BY},
 * {@code UPDATE SET}, and {@code DELETE FROM}.
 *
 * <p>This class is <strong>mutable</strong> and therefore <strong>not thread-safe</strong>. It is intended
 * to be used in a single-threaded or scoped manner per operation or request.</p>
 *
 * <p>Usage constraints:</p>
 * <ul>
 *   <li>{@link #result()}, {@link #stream()}, and {@link #singleResult()} must only be used with {@code SELECT} queries.</li>
 *   <li>{@link #executeUpdate()} must only be used with {@code DELETE} or {@code UPDATE} queries.</li>
 *   <li>If the underlying NoSQL provider does not support a feature or query type, an {@link UnsupportedOperationException} will be thrown.</li>
 * </ul>
 *
 * <p>Example usage for SELECT:</p>
 * <pre>{@code
 *   List<Person> people = template.query("SELECT * FROM Person WHERE active = true").result();
 * }</pre>
 *
 * <p>Example usage for DELETE:</p>
 * <pre>{@code
 * template.query("DELETE FROM Person WHERE active = false")
 *         .executeUpdate();
 * }</pre>
 *
 * <p>Example usage for UPDATE:</p>
 * <pre>{@code
 * template.query("UPDATE Person SET active = true WHERE name = 'Ada'")
 *         .executeUpdate();
 * }</pre>
 */
public interface Query {

    /**
     * Executes a write operation query (such as {@code UPDATE} or {@code DELETE}).
     *
     * <p>This method performs non-select operations. If the query is a {@code SELECT},
     * it will throw an {@link UnsupportedOperationException}.</p>
     *
     * <p>Note: Since some NoSQL databases use eventual consistency or append-only write models,
     * the effects of this operation may not be immediately visible. The propagation of the update or delete
     * may vary depending on the database implementation and its consistency guarantees.</p>
     *
     * <p>If required parameters (named or positional) are not bound, this operation will fail,
     * and the provider may throw an exception.</p>
     *
     * <pre>{@code
     * template.query("DELETE FROM Person WHERE age < :minAge")
     *         .bind("minAge", 18)
     *         .executeUpdate();
     * }</pre>
     *
     * @throws UnsupportedOperationException if the query is a {@code SELECT}, or the operation is not supported by the provider
     */
    void executeUpdate();

    /**
     * Executes a {@code SELECT} query and returns the result as a {@link List}.
     *
     * <p>This method must only be used for queries that begin with {@code SELECT}.
     * If the query is an {@code UPDATE} or {@code DELETE}, an {@link UnsupportedOperationException} will be thrown.</p>
     *
     * <p>If required parameters are not bound before execution, the query will fail and
     * the provider will raise an exception or error.</p>
     *
     * <pre>{@code
     * List<Person> adults = template.query("SELECT * FROM Person WHERE age >= :minAge")
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
     * <p>This method must only be used for queries that begin with {@code SELECT}.
     * If the query is an {@code UPDATE} or {@code DELETE}, an {@link UnsupportedOperationException} will be thrown.</p>
     *
     * <p>If required parameters are not bound before execution, the query will fail and
     * the provider will raise an exception or error.</p>
     *
     * <pre>{@code
     * Stream<Person> stream = template.query("SELECT * FROM Person WHERE active = true")
     *                                  .stream();
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
     * the behavior is provider-specific and may throw an exception.</p>
     *
     * <p>This method must only be used for queries that begin with {@code SELECT}.
     * If the query is an {@code UPDATE} or {@code DELETE}, an {@link UnsupportedOperationException} will be thrown.</p>
     *
     * <p>If required parameters are not bound before execution, the query will fail and
     * the provider will raise an exception or error.</p>
     *
     * <pre>{@code
     * Optional<Person> person = template.query("SELECT * FROM Person WHERE id = :id")
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
