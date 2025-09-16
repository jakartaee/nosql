/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
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
 * This interface defines the Fluent API for selecting and deleting NoSQL entities.
 * The query API provides a way to define queries using a fluent API.
 * It's important to check the compatibility of the database to see if it supports the query API.
 * For example, Key-Value databases typically do not support the query API.
 * Additionally, not all methods in the query API may be supported by all databases.
 * For instance, a document database may not support the "between" method.
 *
 * @see jakarta.nosql.Template#select(Class)
 * @see jakarta.nosql.Template#delete(Class)
 * @since 1.0.0
 */
public interface QueryMapper {


    /**
     * Represents the first step in the delete query fluent API.
     */
    interface MapperDeleteFrom extends MapperDeleteQueryBuild {

        /**
         * Starts a new delete condition by specifying a column name.
         *
         * @param name the column name
         * @return a new {@link MapperDeleteNameCondition}
         * @throws NullPointerException when name is null
         */
        MapperDeleteNameCondition where(String name);
    }

    /**
     * Represents a delete condition based on a column name.
     */
    interface MapperDeleteNameCondition {


        /**
         * Creates a delete condition where the specified column name equals the provided value.
         *
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere eq(T value);

        /**
         * Creates a delete condition where the specified column name is like the provided value.
         *
         * @param value the value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere like(String value);

        /**
         * Creates a delete condition where the specified column name is greater than the provided value.
         *
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gt(T value);

        /**
         * Creates a delete condition where the specified column name is greater than or equal to the provided value.
         *
         * @param <T>   the type
         * @param value the value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gte(T value);

        /**
         * Creates a delete condition where the specified column name is less than the provided value.
         *
         * @param <T>   the type
         * @param value the value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lt(T value);

        /**
         * Creates a delete condition where the specified column name is less than or equal to the provided value.
         *
         * @param <T>   the type
         * @param value the value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lte(T value);

        /**
         * Creates a delete condition where the specified column name is between the provided values.
         *
         * @param <T>    the type
         * @param valueA the lower bound of the range
         * @param valueB the upper bound of the range
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when either valueA or valueB is null
         */
        <T> MapperDeleteWhere between(T valueA, T valueB);

        /**
         * Creates a delete condition where the specified column name is in the provided iterable values.
         *
         * @param values the values for the condition
         * @param <T>    the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when values is null
         */
        <T> MapperDeleteWhere in(Iterable<T> values);

        /**
         * Creates a NOT delete condition for the specified column name.
         *
         * @return {@link MapperDeleteNotCondition}
         */
        MapperDeleteNotCondition not();
    }

    /**
     * Represents a NOT delete condition in the delete query fluent API.
     */
    interface MapperDeleteNotCondition extends MapperDeleteNameCondition {
    }

    /**
     * Represents the last step of the delete query fluent API execution.
     */
    interface MapperDeleteQueryBuild {


        /**
         * Executes the query.
         * @throws UnsupportedOperationException If a NoSQL database does not support a specific operation or if the
         *                                       database does not support certain query conditions, an exception will be raised. For example, a wide-column
         *                                       may not support the OR operator, or a document database may not support the BETWEEN operator.
         *                                       The level of NoSQL database support for various conditions may vary depending on the database provider.
         */
        void execute();

    }

    /**
     * Represents a step where it's possible to perform a logical conjunction or disjunction,
     * add one more delete condition, or end up performing the built query.
     */
    interface MapperDeleteWhere extends MapperDeleteQueryBuild {

        /**
         * Create a new delete condition performing logical conjunction (AND) by specifying a column name.
         *
         * @param name the column name
         * @return the same {@link MapperDeleteNameCondition} with the delete condition appended
         * @throws NullPointerException when name is null
         */
        MapperDeleteNameCondition and(String name);

        /**
         * Create a new delete condition performing logical disjunction (OR) by specifying a column name.
         *
         * @param name the column name
         * @return the same {@link MapperDeleteNameCondition} with the delete condition appended
         * @throws NullPointerException when name is null
         */
        MapperDeleteNameCondition or(String name);
    }

    /**
     * Represents the first step in the query fluent API.
     */
    interface MapperFrom extends MapperQueryBuild {

        /**
         * Starts a new condition by specifying a column name.
         *
         * @param name the column name
         * @return a new {@link MapperNameCondition}
         * @throws NullPointerException when name is null
         */
        MapperNameCondition where(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with the first result defined
         * @throws IllegalArgumentException when skip is negative
         */
        MapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         * @throws IllegalArgumentException when limit is negative
         */
        MapperLimit limit(long limit);

        /**
         * Add the order how the result will return based on a given column name.
         *
         * @param name the column name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        MapperOrder orderBy(String name);
    }

    /**
     * Represents the step in the query fluent API where it's possible to define the maximum number of results to retrieve or to perform the query execution.
     */
    interface MapperLimit extends MapperQueryBuild {

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the number of elements to skip
         * @return a query with the first result defined
         * @throws IllegalArgumentException when skip is negative
         */
        MapperSkip skip(long skip);
    }

    /**
     * Represents a condition based on a column name.
     */
    interface MapperNameCondition {


        /**
         * Creates a condition where the specified column value equals the given value.
         * Example:
         * <pre>
         * template.select(User.class)
         *         .where("username").eq("alice")
         *         .result();
         * </pre>
         *
         * @param value the value for the comparison
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere eq(T value);

        /**
         * Creates a condition where the specified column value is greater than the given value.
         * Example:
         * <pre>
         * template.select(Product.class)
         *         .where("price").gt(50)
         *         .result();
         * </pre>
         *
         * @param value the value for the comparison
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere gt(T value);

        /**
         * Creates a condition where the specified column value is greater than or equal to the given value.
         * Example:
         * <pre>
         * template.select(Product.class)
         *         .where("stock").gte(10)
         *         .result();
         * </pre>
         *
         * @param value the value for the comparison
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere gte(T value);

        /**
         * Creates a condition where the specified column value is less than the given value.
         * Example:
         * <pre>
         * template.select(Order.class)
         *         .where("totalAmount").lt(500)
         *         .result();
         * </pre>
         *
         * @param value the value for the comparison
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere lt(T value);

        /**
         * Creates a condition where the specified column value is less than or equal to the given value.
         * Example:
         * <pre>
         * template.select(Customer.class)
         *         .where("age").lte(30)
         *         .result();
         * </pre>
         *
         * @param value the value for the comparison
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere lte(T value);

        /**
         * Creates a condition where the specified column value matches the provided pattern.
         * Example:
         * <pre>
         * template.select(Book.class)
         *         .where("title").like("Java")
         *         .result();
         * </pre>
         *
         * @param value the pattern value to match
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        MapperWhere like(String value);

        /**
         * Creates a condition where the specified column contains the given substring.
         *
         * <p>This is useful for filtering results where a column includes the given text fragment anywhere within its value.</p>
         *
         * @param value the substring to search for within the column
         * @return the {@link MapperWhere} instance for further condition chaining
         * @throws NullPointerException if {@code value} is {@code null}
         *
         * <p><b>Example:</b></p>
         * <pre>{@code
         * template.select(Book.class)
         *         .where("title").contains("Java")
         *         .result();
         * }</pre>
         */
        MapperWhere contains(String value);

        /**
         * Creates a condition where the specified column starts with the given prefix.
         *
         * <p>This is useful for filtering results where a column begins with a specific value.</p>
         *
         * @param value the prefix to match at the beginning of the column
         * @return the {@link MapperWhere} instance for further condition chaining
         * @throws NullPointerException if {@code value} is {@code null}
         *
         * <p><b>Example:</b></p>
         * <pre>{@code
         * template.select(User.class)
         *         .where("username").startWith("admin")
         *         .result();
         * }</pre>
         */
        MapperWhere startWith(String value);

        /**
         * Creates a condition where the specified column ends with the given suffix.
         *
         * <p>This is useful for filtering results where a column ends with a specific value.</p>
         *
         * @param value the suffix to match at the end of the column
         * @return the {@link MapperWhere} instance for further condition chaining
         * @throws NullPointerException if {@code value} is {@code null}
         *
         * <p><b>Example:</b></p>
         * <pre>{@code
         * template.select(Document.class)
         *         .where("filename").endsWith(".pdf")
         *         .result();
         * }</pre>
         */
        MapperWhere endsWith(String value);


        /**
         * Creates a condition where the specified column value is between the two provided bounds.
         * Example:
         * <pre>
         * template.select(Product.class)
         *         .where("price").between(10, 100)
         *         .result();
         * </pre>
         *
         * @param valueA the lower bound
         * @param valueB the upper bound
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if either valueA or valueB is null
         */
        <T> MapperWhere between(T valueA, T valueB);

        /**
         * Creates a condition where the specified column value exists within the given collection.
         * Example:
         * <pre>
         * template.select(Product.class)
         *         .where("category").in(List.of("book", "electronics"))
         *         .result();
         * </pre>
         *
         * @param values the collection of values to match
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if values is null
         */
        <T> MapperWhere in(Iterable<T> values);

        /**
         * Creates a negated condition for the current column, allowing inverse logic.
         * Example:
         * <pre>
         * template.select(User.class)
         *         .where("active").not().eq(true)
         *         .result();
         * </pre>
         *
         * @return the {@link MapperNotCondition} to continue building a negated expression
         */
        MapperNotCondition not();
    }

    /**
     * Represents the step in the query fluent API where it's possible to define the order of the results or to perform the query execution.
     */
    interface MapperNameOrder extends MapperQueryBuild {

        /**
         * Adds an ordering rule based on the specified column name.
         * Example:
         * <pre>
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .orderBy("title")
         *         .result();
         * </pre>
         *
         * @param name the column name to order by
         * @return the {@link MapperOrder} instance for defining the sort direction
         * @throws NullPointerException if name is null
         */
        MapperOrder orderBy(String name);


        /**
         * Sets the number of results to skip before starting to return results.
         * Example:
         * <pre>
         * template.select(Book.class)
         *         .where("category").eq("Science")
         *         .skip(10)
         *         .result();
         * </pre>
         *
         * @param skip the number of results to skip
         * @return the {@link MapperSkip} instance for chaining
         */
        MapperSkip skip(long skip);



        /**
         * Sets the maximum number of results to return.
         * Example:
         * <pre>
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .limit(5)
         *         .result();
         * </pre>
         *
         * @param limit the maximum number of results to retrieve
         * @return the {@link MapperLimit} instance for chaining
         * @throws IllegalArgumentException if limit is negative
         */
        MapperLimit limit(long limit);
    }

    /**
     * Represents a NOT condition in the delete query fluent API.
     */
    interface MapperNotCondition extends MapperNameCondition {
    }

    /**
     * Represents the step in the query fluent API where it's possible to define the order of the results or to perform the query execution.
     */
    interface MapperOrder {


        /**
         * Defines the order as ascending.
         *
         * @return the {@link MapperNameOrder} instance
         */
        MapperNameOrder asc();

        /**
         * Defines the order as descending.
         *
         * @return the {@link MapperNameOrder} instance
         */
        MapperNameOrder desc();
    }

    /**
     * Represents the last step of the query fluent API execution.
     */
    interface MapperQueryBuild {

        /**
         * Executes the query and returns the number of entities that match the current filter conditions.
         *
         * <p>This is a terminal operation. Unlike {@code result()} which returns the list of matching
         * entities, {@code count()} returns the total number of matching records.
         *
         * <p>Example usage:
         * <pre>{@code
         * long count = template.select(Person.class)
         *                      .where("active").eq(true)
         *                      .count();
         * }</pre>
         *
         * @return the number of records that match the filter criteria
         */
        long count();

        /**
         * Executes the query and returns the result as a {@link List}.
         *
         * @param <T> the entity type
         * @return the result of the query
         * @throws UnsupportedOperationException If a NoSQL database does not support a specific operation or if the
         *                                       database does not support certain query conditions, an exception will be raised. For example, a wide-column
         *                                       may not support the OR operator, or a document database may not support the BETWEEN operator.
         *                                       The level of NoSQL database support for various conditions may vary depending on the database provider.
         */
        <T> List<T> result();

        /**
         * Executes the query and returns the result as a {@link Stream}.
         *
         * @param <T> the entity type
         * @return the result of the query
         * @throws UnsupportedOperationException If a NoSQL database does not support a specific operation or if the
         *                                       database does not support certain query conditions, an exception will be raised. For example, a wide-column
         *                                       may not support the OR operator, or a document database may not support the BETWEEN operator.
         *                                       The level of NoSQL database support for various conditions may vary depending on the database provider.
         */
        <T> Stream<T> stream();

        /**
         * Executes the query and returns the result as a single element, wrapped in an {@link Optional}.
         * If the query returns exactly one result, that result is returned in the Optional.
         * If no result is found, {@link Optional#empty()} is returned.
         * If more than one result is found, an exception specific to the Jakarta NoSQL provider may be thrown.
         *
         * <p>Use this method when expecting a single result from a query. It provides a safe way to handle the case
         * where zero or one result is expected, while also allowing for exceptional cases where multiple results are returned.</p>
         *
         * @param <T> the type of the entity being queried
         * @return an Optional containing the single result of the query, if present, or empty if no result is found
         * @throws UnsupportedOperationException If a NoSQL database does not support a specific operation or if the
         *                                       database does not support certain query conditions, an exception will be raised. For example, a wide-column
         *                                       may not support the OR operator, or a document database may not support the BETWEEN operator.
         *                                       The level of NoSQL database support for various conditions may vary depending on the database provider.
         */
        <T> Optional<T> singleResult();

    }

    /**
     * Represents the step in the query fluent API where it's possible to define the position of the first result to retrieve or to perform the query execution.
     */
    interface MapperSkip extends MapperQueryBuild {


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        MapperLimit limit(long limit);
    }

    /**
     * Represents a step where it's possible to:
     * <ul>
     *     <li>Create a new condition performing logical conjunction (AND) by specifying a column name</li>
     *     <li>Create a new condition performing logical disjunction (OR) by specifying a column name</li>
     *     <li>Define the position of the first result</li>
     *     <li>Define the maximum number of results to retrieve</li>
     *     <li>Define the order of the results</li>
     *     <li>Perform the query execution</li>
     * </ul>
     */
    interface MapperWhere extends MapperQueryBuild {


        /**
         * Create a new condition performing logical conjunction (AND) by specifying a column name.
         *
         * @param name the column name
         * @return the same {@link MapperNameCondition} with the condition appended
         * @throws NullPointerException when name is null
         */
        MapperNameCondition and(String name);

        /**
         * Create a new condition performing logical disjunction (OR) by specifying a column name.
         *
         * @param name the column name
         * @return the same {@link MapperNameCondition} with the condition appended
         * @throws NullPointerException when name is null
         */
        MapperNameCondition or(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with the first result defined
         */
        MapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        MapperLimit limit(long limit);

        /**
         * Add the order how the result will return.
         *
         * @param name the name to order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        MapperOrder orderBy(String name);
    }

}
