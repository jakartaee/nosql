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
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("author").eq("Ada")
         *         .execute();
         * }</pre>
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere eq(T value);

        /**
         * Creates a delete condition where the specified column name is like the provided value.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("author").like("A%")
         *         .execute();
         * }</pre>
         * @param value the value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere like(String value);

        /**
         * Creates a delete condition where the specified column contains the given value.
         * This method is used when you want to delete entities where the column contains the provided substring.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("title").contains("Java")
         *         .execute();
         * }</pre>
         *
         * @param value the substring value to match
         * @return the {@link MapperDeleteWhere} to continue building the query
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere contains(String value);

        /**
         * Creates a delete condition where the specified column starts with the given value.
         * This method is used when you want to delete entities where the column starts with the provided prefix.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("author").startsWith("Ada")
         *         .execute();
         * }</pre>
         *
         * @param value the prefix value to match
         * @return the {@link MapperDeleteWhere} to continue building the query
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere startsWith(String value);

        /**
         * Creates a delete condition where the specified column ends with the given value.
         * This method is used when you want to delete entities where the column ends with the provided suffix.
         *
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("author").endsWith("Lovelace")
         *         .execute();
         * }</pre>
         *
         * @param value the suffix value to match
         * @return the {@link MapperDeleteWhere} to continue building the query
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere endsWith(String value);

        /**
         * Creates a delete condition where the specified column name is greater than the provided value.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("publishedYear").gt(2015)
         *         .execute();
         * }</pre>
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gt(T value);

        /**
         * Creates a delete condition where the specified column name is greater than or equal to the provided value.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("publishedYear").gte(2020)
         *         .execute();
         * }</pre>
         * @param <T>   the type
         * @param value the value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gte(T value);

        /**
         * Creates a delete condition where the specified column name is less than the provided value.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("publishedYear").lt(2000)
         *         .execute();
         * }</pre>
         * @param <T>   the type
         * @param value the value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lt(T value);

        /**
         * Creates a delete condition where the specified column name is less than or equal to the provided value.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("publishedYear").lte(2010)
         *         .execute();
         * }</pre>
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
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("author").in(List.of("Ada", "Grace", "Alan"))
         *         .execute();
         * }</pre>
         * @param values the values for the condition
         * @param <T>    the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when values is null
         */
        <T> MapperDeleteWhere in(Iterable<T> values);

        /**
         * Creates a NOT delete condition for the specified column name.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("author").not().eq("Ada")
         *         .execute();
         * }</pre>
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
         *  Executes the delete query based on the specified conditions.
         *  Use this method to remove entities from the database that match the defined criteria.
         * <pre>{@code
         * template.delete(Book.class)
         *         .where("author").eq("Ada")
         *         .and("publishedYear").gte(2020)
         *         .execute();
         * }</pre>
         *
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
     * Represents the first step in the update query fluent API.
     * <p>
     * This step defines the entity type to be updated and starts the construction
     * of an update operation. From this point, one or more update assignments
     * must be defined using {@code set(...).to(...)} before the operation
     * can be executed.
     * </p>
     *
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * template.from(Book.class)
     *     .set("title").to("Domain-Driven Design with Java")
     *     .set("publishedYear").to(2025)
     *     .where("author").eq("Ada")
     *     .execute();
     * }</pre>
     *
     * The returned instance is mutable and not thread-safe.
     * Support for update operations depends on the underlying database.
     *
     */
    interface MapperUpdateFrom {

        /**
         * Defines an update assignment for the specified field.
         *
         * @param name the field name to be updated
         * @return a step that allows assigning a value to the field
         * @throws NullPointerException when the field name is {@code null}
         */
        MapperUpdateSetTo set(String name);
    }

    /**
     * Represents the value assignment step of the update fluent API.
     * <p>
     * This step completes a field assignment started with {@code set(String)}
     * by defining the value to be applied. After the value is assigned, the
     * update query may define additional assignments, specify conditions,
     * or be executed.
     * </p>
     *
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * template.from(Book.class)
     *     .set("title").to("Domain-Driven Design with Java")
     *     .set("publishedYear").to(2025)
     *     .where("author").eq("Ada")
     *     .execute();
     * }</pre>
     *
     * The returned instance is mutable and not thread-safe.
     * The behavior of the update operation depends on the capabilities of the
     * underlying database.
     */
    interface MapperUpdateSetTo {

        /**
         * Assigns the given value to the previously defined field.
         *
         * @param value the value to assign
         * @param <T>   the value type
         * @return the next step of the update fluent API
         */
        <T> MapperUpdateSetStep to(T value);
    }

    /**
     * Represents the update assignment step of the fluent update API.
     * <p>
     * This step allows defining one or more field assignments for an update
     * operation. From this point, additional assignments may be added, the
     * update scope may be restricted using {@code where(...)}, or the
     * operation may be executed.
     * </p>
     *
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * template.from(Book.class)
     *     .set("title").to("Domain-Driven Design with Java")
     *     .set("publishedYear").to(2025)
     *     .where("author").eq("Ada")
     *     .execute();
     * }</pre>
     *
     * The returned instance is mutable and not thread-safe.
     */
    interface MapperUpdateSetStep extends MapperUpdateQueryBuild {

        /**
         * Starts a new field assignment for the update operation.
         *
         * @param name the field name to be updated
         * @return a step that allows assigning a value to the field
         * @throws NullPointerException when the field name is {@code null}
         */
        MapperUpdateSetTo set(String name);

        /**
         * Defines a condition to restrict which entities will be updated.
         *
         * @param name the field name used in the condition
         * @return the conditional step of the update fluent API
         * @throws NullPointerException when the field name is {@code null}
         */
        MapperUpdateWhereStep where(String name);
    }

    /**
     * Represents the predicate definition step of the fluent update API.
     * <p>
     * This step defines conditional expressions used to restrict which entities
     * will be affected by an update operation. Each method adds a comparison
     * predicate for the previously defined field.
     * </p>
     *
     * <p>
     * Predicate support and evaluation semantics depend on the capabilities of
     * the underlying database. Unsupported predicates may result in an
     * {@link UnsupportedOperationException}.
     * </p>
     *
     * <p>
     * This step is mutable and not thread-safe.
     * </p>
     */
    interface MapperUpdateWhereStep {

        /**
         * Creates an update condition where the specified column name equals the provided value.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("published").to(true)
         *         .where("author").eq("Ada")
         *         .execute();
         * }</pre>
         *
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere eq(T value);

        /**
         * Creates an update condition where the specified column matches the given pattern.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("category").to("classic")
         *         .where("title").like("%Design%")
         *         .execute();
         * }</pre>
         *
         * @param value the pattern value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere like(String value);

        /**
         * Creates an update condition where the specified column contains the given value.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("highlighted").to(true)
         *         .where("description").contains("DDD")
         *         .execute();
         * }</pre>
         *
         * @param value the value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere contains(String value);

        /**
         * Creates an update condition where the specified column starts with the given value.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("featured").to(true)
         *         .where("title").startsWith("Domain")
         *         .execute();
         * }</pre>
         *
         * @param value the prefix value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere startsWith(String value);

        /**
         * Creates an update condition where the specified column ends with the given value.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("archived").to(true)
         *         .where("title").endsWith("Java")
         *         .execute();
         * }</pre>
         *
         * @param value the suffix value for the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere endsWith(String value);

        /**
         * Creates an update condition where the specified column is greater than the provided value.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("discounted").to(true)
         *         .where("price").gt(50)
         *         .execute();
         * }</pre>
         *
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gt(T value);

        /**
         * Creates an update condition where the specified column is greater than or equal to the provided value.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("discounted").to(true)
         *         .where("price").gte(30)
         *         .execute();
         * }</pre>
         *
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gte(T value);

        /**
         * Creates an update condition where the specified column is less than the provided value.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("featured").to(true)
         *         .where("rating").lt(5)
         *         .execute();
         * }</pre>
         *
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lt(T value);

        /**
         * Creates an update condition where the specified column is less than or equal to the provided value.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("featured").to(true)
         *         .where("rating").lte(4)
         *         .execute();
         * }</pre>
         *
         * @param value the value for the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lte(T value);

        /**
         * Creates an update condition where the specified column is between the provided values.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("recommended").to(true)
         *         .where("publishedYear").between(2015, 2025)
         *         .execute();
         * }</pre>
         *
         * @param valueA the lower bound value
         * @param valueB the upper bound value
         * @param <T>    the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when any value is null
         */
        <T> MapperDeleteWhere between(T valueA, T valueB);

        /**
         * Creates an update condition where the specified column value is contained in the provided values.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("available").to(false)
         *         .where("category").in(List.of("legacy", "outdated"))
         *         .execute();
         * }</pre>
         *
         * @param values the values for the condition
         * @param <T>    the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when values is null
         */
        <T> MapperDeleteWhere in(Iterable<T> values);

        /**
         * Negates the next update condition.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("archived").to(true)
         *         .where("author").not().eq("Ada")
         *         .execute();
         * }</pre>
         *
         * @return the {@link MapperUpdateWhereStep}
         */
        MapperUpdateWhereStep not();
    }

    /**
     * Represents the conditional composition step of the fluent update API.
     * <p>
     * This step allows combining multiple update conditions using logical
     * operators before executing the update operation.
     * </p>
     */
    interface MapperUpdateConditionStep extends MapperUpdateQueryBuild {

        /**
         * Adds an AND condition using the specified column name.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("published").to(true)
         *         .where("author").eq("Ada")
         *         .and("publishedYear").gte(2020)
         *         .execute();
         * }</pre>
         *
         * @param name the column name for the condition
         * @return the {@link MapperUpdateWhereStep}
         * @throws NullPointerException when name is null
         */
        MapperUpdateWhereStep and(String name);

        /**
         * Adds an OR condition using the specified column name.
         * <pre>{@code
         * template.from(Book.class)
         *         .set("featured").to(true)
         *         .where("author").eq("Ada")
         *         .or("author").eq("Hermann")
         *         .execute();
         * }</pre>
         *
         * @param name the column name for the condition
         * @return the {@link MapperUpdateWhereStep}
         * @throws NullPointerException when name is null
         */
        MapperUpdateWhereStep or(String name);

    }

    /**
     * Represents the last step of the delete query fluent API execution.
     */
    interface MapperUpdateQueryBuild {


        /**
         *  Executes the update query based on the specified conditions.
         *  Use this method to remove entities from the database that match the defined criteria.
         * <pre>{@code
         * template.from(Book.class)
         *     .set("title").to("Domain-Driven Design with Java")
         *     .set("publishedYear").to(2025)
         *     .where("author").eq("Ada")
         *     .execute();
         * }</pre>
         *
         * @throws UnsupportedOperationException If a NoSQL database does not support a specific operation or if the
         *                                       database does not support certain query conditions, an exception will be raised. For example, a wide-column
         *                                       may not support the OR operator, or a document database may not support the BETWEEN operator.
         *                                       The level of NoSQL database support for various conditions may vary depending on the database provider.
         */
        void execute();

    }

    /**
     * Represents the first step in the query fluent API.
     */
    interface MapperFrom extends MapperQueryBuild {

        /**
         * Starts a new condition by specifying a column name.
         *Use this method to initiate a condition chain for filtering the query.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("title").eq("Domain-Driven Design");
         * }</pre>
         * @param name the column name
         * @return a new {@link MapperNameCondition}
         * @throws NullPointerException when name is null
         */
        MapperNameCondition where(String name);

        /**
         * Defines the position of the first result to retrieve (pagination offset).
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .skip(10);
         * }</pre>
         * @param skip the first result to retrieve
         * @return a query with the first result defined
         * @throws IllegalArgumentException when skip is negative
         */
        MapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve (pagination limit).
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .limit(5);
         * }</pre>
         * @param limit the limit
         * @return a query with the limit defined
         * @throws IllegalArgumentException when limit is negative
         */
        MapperLimit limit(long limit);

        /**
         * Add the order how the result will return based on a given column name.
         * Use this method to specify sorting criteria for query results.
         *
         * <p>Example:
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .orderBy("title").asc();
         * }</pre>
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
         * Defines the position of the first result to retrieve (pagination offset).
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .skip(10);
         * }</pre>
         * @param skip the first result to retrieve
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
         * <pre>{@code
         * template.select(User.class)
         *         .where("username").eq("alice")
         *         .result();
         * }</pre>
         *
         * @param value the value for the comparison
         * @param <T>   the type of the value to compare with the column value
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere eq(T value);

        /**
         * Creates a condition where the specified column value is greater than the given value.
         * Example:
         * <pre>{@code
         * template.select(Product.class)
         *         .where("price").gt(50)
         *         .result();
         * }</pre>
         *
         * @param value the value for the comparison
         * @param <T>   the type of the value to compare with the column value
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere gt(T value);

        /**
         * Creates a condition where the specified column value is greater than or equal to the given value.
         * <pre>{@code
         * template.select(Product.class)
         *         .where("stock").gte(10)
         *         .result();
         * }</pre>
         *
         * @param value the value for the comparison
         * @param <T>   the type of the value to compare with the column value
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere gte(T value);

        /**
         * Creates a condition where the specified column value is less than the given value.
         * <pre>{@code
         * template.select(Order.class)
         *         .where("totalAmount").lt(500)
         *         .result();
         * }</pre>
         *
         * @param value the value for the comparison
         * @param <T>   the type of the value to compare with the column value
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere lt(T value);

        /**
         * Creates a condition where the specified column value is less than or equal to the given value.
         * <pre>{@code
         * template.select(Customer.class)
         *         .where("age").lte(30)
         *         .result();
         * }</pre>
         *
         * @param value the value for the comparison
         * @param <T>   the type of the value to compare with the column value
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        <T> MapperWhere lte(T value);

        /**
         * Creates a condition where the specified column value matches the provided pattern.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("title").like("Java")
         *         .result();
         * }</pre>
         *
         * @param value the pattern value to match
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if value is null
         */
        MapperWhere like(String value);

        /**
         * Creates a condition where the specified column contains the given substring.
         * This is useful for filtering results where a column includes the given text fragment anywhere within its value.
         *
         * @param value the substring to search for within the column
         * @return the {@link MapperWhere} instance for further condition chaining
         * @throws NullPointerException if {@code value} is {@code null}
         *
         * <pre>{@code
         * template.select(Book.class)
         *         .where("title").contains("Java")
         *         .result();
         * }</pre>
         */
        MapperWhere contains(String value);

        /**
         * Creates a condition where the specified column starts with the given prefix.
         * This is useful for filtering results where a column begins with a specific value.
         *
         * @param value the prefix to match at the beginning of the column
         * @return the {@link MapperWhere} instance for further condition chaining
         * @throws NullPointerException if {@code value} is {@code null}
         *
         * <p><b>Example:</b></p>
         * <pre>{@code
         * template.select(User.class)
         *         .where("username").startsWith("admin")
         *         .result();
         * }</pre>
         */
        MapperWhere startsWith(String value);

        /**
         * Creates a condition where the specified column ends with the given suffix.
         * This is useful for filtering results where a column ends with a specific value.
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
         * <pre>{@code
         * template.select(Product.class)
         *         .where("price").between(10, 100)
         *         .result();
         * }</pre>
         *
         * @param valueA the lower bound
         * @param valueB the upper bound
         * @param <T>   the type of the value to compare with the column value
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if either valueA or valueB is null
         */
        <T> MapperWhere between(T valueA, T valueB);

        /**
         * Creates a condition where the specified column value exists within the given collection.
         * <pre>{@code
         * template.select(Product.class)
         *         .where("category").in(List.of("book", "electronics"))
         *         .result();
         * }</pre>
         *
         * @param values the collection of values to match
         * @param <T>   the type of the value to compare with the column value
         * @return the {@link MapperWhere} instance for chaining
         * @throws NullPointerException if values is null
         */
        <T> MapperWhere in(Iterable<T> values);

        /**
         * Creates a negated condition for the current column, allowing inverse logic.
         * <pre>{@code
         * template.select(User.class)
         *         .where("active").not().eq(true)
         *         .result();
         * }</pre>
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
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .orderBy("title").asc()
         *         .result();
         * }</pre>
         *
         * @param name the column name to order by
         * @return the {@link MapperOrder} instance for defining the sort direction
         * @throws NullPointerException if name is null
         */
        MapperOrder orderBy(String name);


        /**
         * Sets the number of results to skip before starting to return results.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("category").eq("Science")
         *         .skip(10)
         *         .result();
         * }</pre>
         *
         * @param skip the number of results to skip
         * @return the {@link MapperSkip} instance for chaining
         */
        MapperSkip skip(long skip);



        /**
         * Sets the maximum number of results to return.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .limit(5)
         *         .result();
         * }</pre>
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
         * Defines the sorting direction as ascending for the previously specified column.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .orderBy("title").asc()
         *         .result();
         * }</pre>
         *
         * @return the {@link MapperNameOrder} instance for further chaining
         */
        MapperNameOrder asc();

        /**
         * Defines the sorting direction as descending for the previously specified column.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .orderBy("title").desc()
         *         .result();
         * }</pre>
         *
         * @return the {@link MapperNameOrder} instance for further chaining
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
         * <pre>{@code
         * List<Book> books = template.select(Book.class)
         *                            .where("author").eq("Ada")
         *                            .result();
         * }</pre>
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
         * <pre>{@code
         * Stream<Book> books = template.select(Book.class)
         *                                   .where("author")
         *                                   .eq("Ada")
         *                                   .stream();
         *     books.forEach(System.out::println);
         * }</pre>
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
         * <pre>{@code
         * Optional<Book> book = template.select(Book.class)
         *                               .where("isbn").eq("978-1234567890")
         *                               .singleResult();
         * }</pre>
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
         * Defines the maximum number of results to retrieve (pagination limit).
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .limit(5);
         * }</pre>
         * @param limit the limit
         * @return a query with the limit defined
         * @throws IllegalArgumentException when limit is negative
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
         * Use this method to combine multiple conditions where all must be satisfied.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .and("publishedYear").gte(2020)
         *         .result();
         * }</pre>
         * @param name the column name
         * @return the same {@link MapperNameCondition} with the condition appended
         * @throws NullPointerException when name is null
         */
        MapperNameCondition and(String name);

        /**
         * Create a new condition performing logical disjunction (OR) by specifying a column name.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .or("author").eq("Otavio")
         *         .result();
         * }</pre>
         * @param name the column name
         * @return the same {@link MapperNameCondition} with the condition appended
         * @throws NullPointerException when name is null
         */
        MapperNameCondition or(String name);

        /**
         * Sets the number of results to skip before starting to return results.
         * <pre>{@code
         * template.select(Book.class)
         *         .where("category").eq("Science")
         *         .skip(10)
         *         .result();
         * }</pre>
         *
         * @param skip the number of results to skip
         * @return the {@link MapperSkip} instance for chaining
         */
        MapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve (pagination limit).
         * <pre>{@code
         * template.select(Book.class)
         *         .where("author").eq("Ada")
         *         .limit(5);
         * }</pre>
         * @param limit the limit
         * @return a query with the limit defined
         * @throws IllegalArgumentException when limit is negative
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
