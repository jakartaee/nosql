/*
 * Copyright (c) 2019 Otavio Santana and others
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
package jakarta.nosql.column;

import jakarta.nosql.Condition;
import jakarta.nosql.ServiceLoaderProvider;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A unit that has the columnFamily and condition to delete from conditions.
 * This instance will be used on:
 * <p>{@link ColumnFamilyManager#delete(ColumnDeleteQuery)}</p>
 */
public interface ColumnDeleteQuery {


    /**
     * getter the columnFamily name
     *
     * @return the columnFamily name
     */
    String getColumnFamily();

    /**
     * getter the condition
     * If empty, {@link Optional#empty()} is true, the implementation might either return an unsupported exception or delete same elements in the database.
     *
     * @return the condition
     */
    Optional<ColumnCondition> getCondition();

    /**
     * Defines which columns will be removed, the database provider might use this information
     * to remove just these fields instead of all entity from {@link ColumnDeleteQuery}
     *
     * @return the columns
     */
    List<String> getColumns();

    /**
     * It starts the first step of {@link ColumnDelete} API using a fluent-API way.
     * This first step will inform the fields to delete in the query instead of the whole record.
     * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
     *
     * @param columns the column fields to delete query
     * @return a new {@link ColumnDelete} instance
     * @throws NullPointerException when there is a null element
     */
    static ColumnDelete delete(String... columns) {
        return ServiceLoaderProvider.get(ColumnDeleteProvider.class).apply(columns);
    }

    /**
     * It starts the first step of {@link ColumnDelete} API using a fluent-API way.
     * Once there is no field, it will remove the whole record instead of some fields on the database.
     *
     * @return a new {@link ColumnDelete} instance
     */
    static ColumnDelete delete() {
        return ServiceLoaderProvider.get(ColumnDeleteProvider.class).get();
    }

    /**
     * It starts the first step of {@link ColumnDeleteQuery} creation using a builder pattern.
     * This first step will inform the fields to delete in the query instead of the whole record.
     * This behavior might be different for each NoSQL database provider; therefore,
     * it might be ignored for some implementations.
     *
     * @param documents the column fields to delete query
     * @return a {@link ColumnDeleteQueryBuilder} instance
     * @throws NullPointerException when there is a null element
     */
    static ColumnDeleteQueryBuilder builder(String... documents) {
        return ServiceLoaderProvider.get(ColumnDeleteQueryBuilderProvider.class).apply(documents);
    }

    /**
     * It starts the first step of {@link ColumnDeleteQueryBuilder} creation using a builder pattern.
     * Once there is no field, it will remove the whole record instead of some fields on the database.
     *
     * @return a {@link ColumnDeleteQueryBuilder} instance
     */
    static ColumnDeleteQueryBuilder builder() {
        return ServiceLoaderProvider.get(ColumnDeleteQueryBuilderProvider.class).get();
    }


    /**
     * The initial element in the Column delete query
     */
    interface ColumnDelete {

        /**
         * Defines the column family in the delete query
         *
         * @param columnFamily the column family to query
         * @return a {@link ColumnDeleteFrom query}
         * @throws NullPointerException when columnFamily is null
         */
        ColumnDeleteFrom from(String columnFamily);
    }

    /**
     * A supplier class of {@link ColumnDelete}
     */
    interface ColumnDeleteProvider extends Function<String[], ColumnDelete>, Supplier<ColumnDelete> {
    }

    interface ColumnDeleteQueryBuilderProvider extends Function<String[], ColumnDeleteQueryBuilder>,
            Supplier<ColumnDeleteQueryBuilder> {

    }

    /**
     * The Column Delete Query
     */
    interface ColumnDeleteFrom extends ColumnDeleteQueryBuild {


        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link ColumnDeleteNameCondition}
         * @throws NullPointerException when name is null
         */
        ColumnDeleteNameCondition where(String name);

    }

    /**
     * The base to delete name condition
     */
    interface ColumnDeleteNameCondition {


        /**
         * Creates the equals condition {@link Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link ColumnDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnDeleteWhere eq(T value);

        /**
         * Creates the like condition {@link Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link ColumnDeleteWhere}
         * @throws NullPointerException when value is null
         */
        ColumnDeleteWhere like(String value);

        /**
         * Creates the greater than condition {@link Condition#GREATER_THAN}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link ColumnDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnDeleteWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnDeleteWhere gte(T value);

        /**
         * Creates the lesser than condition {@link Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnDeleteWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnDeleteWhere lte(T value);

        /**
         * Creates the between condition {@link Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link ColumnDeleteWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> ColumnDeleteWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link ColumnDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnDeleteWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link Condition#NOT}
         *
         * @return {@link ColumnDeleteNotCondition}
         */
        ColumnDeleteNotCondition not();


    }

    /**
     * The column not condition
     */
    interface ColumnDeleteNotCondition extends ColumnDeleteNameCondition {
    }

    /**
     * The last step to the build of {@link ColumnDeleteQuery}.
     * It either can return a new {@link ColumnDeleteQuery} instance or execute a query with
     * {@link ColumnFamilyManager}
     */
    interface ColumnDeleteQueryBuild {

        /**
         * Creates a new instance of {@link ColumnDeleteQuery}
         *
         * @return a new {@link ColumnDeleteQuery} instance
         */
        ColumnDeleteQuery build();

        /**
         * executes the {@link ColumnFamilyManager#delete(ColumnDeleteQuery)}
         *
         * @param manager the entity manager
         * @throws NullPointerException when manager is null
         */
        void delete(ColumnFamilyManager manager);

    }

    /**
     * The Column Where whose define the condition in the delete query.
     */
    interface ColumnDeleteWhere extends ColumnDeleteQueryBuild {


        /**
         * Starts a new condition in the select using {@link ColumnCondition#and(ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnDeleteNameCondition and(String name);

        /**
         * Starts a new condition in the select using {@link ColumnCondition#or(ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnDeleteNameCondition or(String name);

    }

    /**
     * Besides the fluent-API with the select {@link ColumnDeleteQuery#delete()} ()}, the API also has support for creating
     * a {@link ColumnDeleteQuery} instance using a builder pattern.
     * The goal is the same; however, it provides more possibilities, such as more complex queries.
     * <p>
     * The goal is the same; however, it provides more possibilities, such as more complex queries.
     * The ColumnQueryBuilder is not brighter than a fluent-API; it has the same validation in the creation method.
     * It is a mutable and non-thread-safe class.
     */
    interface ColumnDeleteQueryBuilder {
        /**
         * Append a new column in to delete query.
         * It informs the fields to delete in the query instead of the whole record.
         * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
         *
         * @param column a column field to delete query
         * @return the {@link ColumnDeleteQueryBuilder}
         * @throws NullPointerException when the document is null
         */
        ColumnDeleteQueryBuilder delete(String column);

        /**
         * Append a new column in to delete query.
         * This first step will inform the fields to delete in the query instead of the whole record.
         * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
         *
         * @param columns The columns fields to delete query
         * @return the {@link ColumnDeleteQueryBuilder}
         * @throws NullPointerException when there is a null element
         */
        ColumnDeleteQueryBuilder delete(String... columns);

        /**
         * Define the column family in the query, this element is mandatory to build
         * the {@link ColumnDeleteQueryBuilder}
         *
         * @param columnFamily the column family to query
         * @return the {@link ColumnDeleteQueryBuilder}
         * @throws NullPointerException when columnFamily is null
         */
        ColumnDeleteQueryBuilder from(String columnFamily);

        /**
         * Either add or replace the condition in the query. It has a different behavior than the previous method
         * because it won't append it. Therefore, it will create when it is the first time or replace when it was executed once.
         *
         * @param condition the {@link ColumnCondition} in the query
         * @return the {@link ColumnDeleteQueryBuilder}
         * @throws NullPointerException when condition is null
         */
        ColumnDeleteQueryBuilder where(ColumnCondition condition);

        /**
         * It will validate and then create a {@link ColumnDeleteQuery} instance.
         *
         * @return {@link ColumnDeleteQuery}
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link ColumnDeleteQueryBuilder#from(String)} method was not called.
         */
        ColumnDeleteQuery build();

        /**
         * executes the {@link ColumnFamilyManager#delete(ColumnDeleteQuery)}
         *
         * @param manager the entity manager
         * @throws NullPointerException when manager is null
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link ColumnDeleteQueryBuilder#from(String)} method was not called.
         */
        void delete(ColumnFamilyManager manager);
    }
}
