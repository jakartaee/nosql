/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package jakarta.nosql.mapping.column;


import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Pagination;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The builder to either select and delete query using an object mapper API.
 */
public interface QueryMapper {


    /**
     * The Column Delete Query
     */
    interface MapperDeleteFrom extends MapperDeleteQueryBuild {

        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link MapperDeleteNameCondition}
         * @throws NullPointerException when name is null
         */
        MapperDeleteNameCondition where(String name);
    }

    /**
     * The base to delete name condition
     */
    interface MapperDeleteNameCondition {


        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere eq(T value);

        /**
         * Creates the like condition {@link jakarta.nosql.Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere like(String value);

        /**
         * Creates the greater than condition {@link jakarta.nosql.Condition#GREATER_THAN}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link jakarta.nosql.Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gte(T value);

        /**
         * Creates the lesser than condition {@link jakarta.nosql.Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link jakarta.nosql.Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lte(T value);

        /**
         * Creates the between condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> MapperDeleteWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link jakarta.nosql.Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#NOT}
         *
         * @return {@link MapperDeleteNotCondition}
         */
        MapperDeleteNotCondition not();
    }

    /**
     * The column not condition
     */
    interface MapperDeleteNotCondition extends MapperDeleteNameCondition {
    }

    /**
     * The last step to the build of {@link ColumnDeleteQuery}.
     * It either can return a new {@link ColumnDeleteQuery} instance or execute a query with
     * {@link ColumnTemplate}
     */
    interface MapperDeleteQueryBuild {

        /**
         * Creates a new instance of {@link ColumnDeleteQuery}
         *
         * @return a new {@link ColumnDeleteQuery} instance
         */
        ColumnDeleteQuery build();

        /**
         * executes the {@link ColumnTemplate#delete(ColumnDeleteQuery)}
         *
         * @throws NullPointerException when manager is null
         */
        void execute();

    }

    /**
     * The Column Where whose define the condition in the delete query.
     */
    interface MapperDeleteWhere extends MapperDeleteQueryBuild {


        /**
         * Starts a new condition in the select using {@link jakarta.nosql.column.ColumnCondition#and(jakarta.nosql.column.ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link MapperDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        MapperDeleteNameCondition and(String name);

        /**
         * Starts a new condition in the select using {@link jakarta.nosql.column.ColumnCondition#or(jakarta.nosql.column.ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link MapperDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        MapperDeleteNameCondition or(String name);
    }

    /**
     * The ColumnFrom Query
     */
    interface MapperFrom extends MapperQueryBuild {

        /**
         * Starts a new condition defining the  column name
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
         * @return a query with first result defined
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
         * Add the order how the result will return
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        MapperOrder orderBy(String name);
    }

    /**
     * The Column Order whose define the maximum number of results to retrieve.
     */
    interface MapperLimit extends MapperQueryBuild {

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the number of elements to skip
         * @return a query with first result defined
         */
        MapperSkip skip(long skip);
    }

    /**
     * The base to name condition
     */
    interface MapperNameCondition {


        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere eq(T value);

        /**
         * Creates the like condition {@link jakarta.nosql.Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        MapperWhere like(String value);

        /**
         * Creates the greater than condition {@link jakarta.nosql.Condition#GREATER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link jakarta.nosql.Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere gte(T value);

        /**
         * Creates the lesser than condition {@link jakarta.nosql.Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link jakarta.nosql.Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere lte(T value);

        /**
         * Creates the between condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link MapperWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> MapperWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link jakarta.nosql.Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#NOT}
         *
         * @return {@link MapperNotCondition}
         */
        MapperNotCondition not();
    }

    /**
     * The Column name order a query
     */
    interface MapperNameOrder extends MapperQueryBuild {

        /**
         * Add the order how the result will return
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        MapperOrder orderBy(String name);


        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         */
        MapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        MapperLimit limit(long limit);
    }

    /**
     * The column not condition
     */
    interface MapperNotCondition extends MapperNameCondition {
    }

    /**
     * The definition to either {@link jakarta.nosql.SortType}
     */
    interface MapperOrder {


        /**
         * Defines the order as {@link jakarta.nosql.SortType#ASC}
         *
         * @return the {@link MapperNameOrder} instance
         */
        MapperNameOrder asc();

        /**
         * Defines the order as {@link jakarta.nosql.SortType#DESC}
         *
         * @return the {@link MapperNameOrder} instance
         */
        MapperNameOrder desc();
    }

    /**
     * The last step to the build of {@link ColumnQuery}.
     * It either can return a new {@link ColumnQuery} instance or execute a query with
     * {@link ColumnTemplate}
     */
    interface MapperQueryBuild {

        /**
         * Creates a new instance of {@link ColumnQuery}
         *
         * @return a new {@link ColumnQuery} instance
         */
        ColumnQuery build();

        /**
         * Creates a new instance of {@link ColumnQuery} from {@link Pagination}
         *
         * @param pagination the pagination
         * @return a new {@link ColumnQuery} instance from {@link Pagination}
         */
        ColumnQuery build(Pagination pagination);

        /**
         * Executes {@link ColumnTemplate#select(ColumnQuery)}
         *
         * @param <T>      the entity type
         * @return the result of {@link ColumnTemplate#select(ColumnQuery)}
         * @throws NullPointerException when manager is null
         */
        <T> Stream<T> getResult();

        /**
         * Executes {@link ColumnTemplate#singleResult(ColumnQuery)}
         *
         * @param <T>      the entity type
         * @return the result of {@link ColumnTemplate#singleResult(ColumnQuery)}
         * @throws NullPointerException when manager is null
         */
        <T> Optional<T> getSingleResult();

        /**
         * Executes {@link ColumnTemplate#select(ColumnQuery)} using {@link Pagination}
         *
         * @param <T>        the entity type
         * @param pagination the pagination
         * @return the result of {@link ColumnTemplate#select(ColumnQuery)}
         * @throws NullPointerException when there are null parameters
         */
        <T> Stream<T> getResult(Pagination pagination);

        /**
         * Executes {@link ColumnTemplate#singleResult(ColumnQuery)} using {@link Pagination}
         *
         * @param <T>        the entity type
         * @param pagination the pagination
         * @return the result of {@link ColumnTemplate#singleResult(ColumnQuery)}
         * @throws NullPointerException when there are null parameters
         */
        <T> Optional<T> getSingleResult(Pagination pagination);

    }

    /**
     * The Column Order whose define the position of the first result to retrieve.
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
     * The Column Where whose define the condition in the query.
     */
    interface MapperWhere extends MapperQueryBuild {


        /**
         * Starts a new condition in the select using
         * {@link jakarta.nosql.column.ColumnCondition#and(jakarta.nosql.column.ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link MapperNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        MapperNameCondition and(String name);

        /**
         * Appends a new condition in the select using
         * {@link jakarta.nosql.column.ColumnCondition#or(jakarta.nosql.column.ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link MapperNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        MapperNameCondition or(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
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
         * Add the order how the result will return
         *
         * @param name the name to order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        MapperOrder orderBy(String name);
    }

}
