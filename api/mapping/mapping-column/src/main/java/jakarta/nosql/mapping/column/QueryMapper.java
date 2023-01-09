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
import jakarta.nosql.mapping.Page;
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
    interface ColumnMapperDeleteFrom extends ColumnMapperDeleteQueryBuild {

        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link ColumnMapperDeleteNameCondition}
         * @throws NullPointerException when name is null
         */
        ColumnMapperDeleteNameCondition where(String name);
    }

    /**
     * The base to delete name condition
     */
    interface ColumnMapperDeleteNameCondition {


        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link ColumnMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperDeleteWhere eq(T value);

        /**
         * Creates the like condition {@link jakarta.nosql.Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link ColumnMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        ColumnMapperDeleteWhere like(String value);

        /**
         * Creates the greater than condition {@link jakarta.nosql.Condition#GREATER_THAN}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link ColumnMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperDeleteWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link jakarta.nosql.Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperDeleteWhere gte(T value);

        /**
         * Creates the lesser than condition {@link jakarta.nosql.Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperDeleteWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link jakarta.nosql.Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperDeleteWhere lte(T value);

        /**
         * Creates the between condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link ColumnMapperDeleteWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> ColumnMapperDeleteWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link jakarta.nosql.Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link ColumnMapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperDeleteWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#NOT}
         *
         * @return {@link ColumnMapperDeleteNotCondition}
         */
        ColumnMapperDeleteNotCondition not();
    }

    /**
     * The column not condition
     */
    interface ColumnMapperDeleteNotCondition extends ColumnMapperDeleteNameCondition {
    }

    /**
     * The last step to the build of {@link ColumnDeleteQuery}.
     * It either can return a new {@link ColumnDeleteQuery} instance or execute a query with
     * {@link ColumnTemplate}
     */
    interface ColumnMapperDeleteQueryBuild {

        /**
         * Creates a new instance of {@link ColumnDeleteQuery}
         *
         * @return a new {@link ColumnDeleteQuery} instance
         */
        ColumnDeleteQuery build();

        /**
         * executes the {@link ColumnTemplate#delete(ColumnDeleteQuery)}
         *
         * @param template the column template
         * @throws NullPointerException when manager is null
         */
        void delete(ColumnTemplate template);

    }

    /**
     * The Column Where whose define the condition in the delete query.
     */
    interface ColumnMapperDeleteWhere extends ColumnMapperDeleteQueryBuild {


        /**
         * Starts a new condition in the select using {@link jakarta.nosql.column.ColumnCondition#and(jakarta.nosql.column.ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnMapperDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnMapperDeleteNameCondition and(String name);

        /**
         * Starts a new condition in the select using {@link jakarta.nosql.column.ColumnCondition#or(jakarta.nosql.column.ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnMapperDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnMapperDeleteNameCondition or(String name);
    }

    /**
     * The ColumnFrom Query
     */
    interface ColumnMapperFrom extends ColumnMapperQueryBuild {

        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link ColumnMapperNameCondition}
         * @throws NullPointerException when name is null
         */
        ColumnMapperNameCondition where(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         */
        ColumnMapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        ColumnMapperLimit limit(long limit);

        /**
         * Add the order how the result will return
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnMapperOrder orderBy(String name);
    }

    /**
     * The Column Order whose define the maximum number of results to retrieve.
     */
    interface ColumnMapperLimit extends ColumnMapperQueryBuild {

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the number of elements to skip
         * @return a query with first result defined
         */
        ColumnMapperSkip skip(long skip);
    }

    /**
     * The base to name condition
     */
    interface ColumnMapperNameCondition {


        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link ColumnMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperWhere eq(T value);

        /**
         * Creates the like condition {@link jakarta.nosql.Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link ColumnMapperWhere}
         * @throws NullPointerException when value is null
         */
        ColumnMapperWhere like(String value);

        /**
         * Creates the greater than condition {@link jakarta.nosql.Condition#GREATER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link jakarta.nosql.Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperWhere gte(T value);

        /**
         * Creates the lesser than condition {@link jakarta.nosql.Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link jakarta.nosql.Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperWhere lte(T value);

        /**
         * Creates the between condition {@link jakarta.nosql.Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link ColumnMapperWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> ColumnMapperWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link jakarta.nosql.Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link ColumnMapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnMapperWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link jakarta.nosql.Condition#NOT}
         *
         * @return {@link ColumnMapperNotCondition}
         */
        ColumnMapperNotCondition not();
    }

    /**
     * The Column name order a query
     */
    interface ColumnMapperNameOrder extends ColumnMapperQueryBuild {

        /**
         * Add the order how the result will return
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnMapperOrder orderBy(String name);


        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         */
        ColumnMapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        ColumnMapperLimit limit(long limit);
    }

    /**
     * The column not condition
     */
    interface ColumnMapperNotCondition extends ColumnMapperNameCondition {
    }

    /**
     * The definition to either {@link jakarta.nosql.SortType}
     */
    interface ColumnMapperOrder {


        /**
         * Defines the order as {@link jakarta.nosql.SortType#ASC}
         *
         * @return the {@link ColumnMapperNameOrder} instance
         */
        ColumnMapperNameOrder asc();

        /**
         * Defines the order as {@link jakarta.nosql.SortType#DESC}
         *
         * @return the {@link ColumnMapperNameOrder} instance
         */
        ColumnMapperNameOrder desc();
    }

    /**
     * The last step to the build of {@link ColumnQuery}.
     * It either can return a new {@link ColumnQuery} instance or execute a query with
     * {@link ColumnTemplate}
     */
    interface ColumnMapperQueryBuild {

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
         * @param template the column template
         * @return the result of {@link ColumnTemplate#select(ColumnQuery)}
         * @throws NullPointerException when manager is null
         */
        <T> Stream<T> getResult(ColumnTemplate template);

        /**
         * Executes {@link ColumnTemplate#singleResult(ColumnQuery)}
         *
         * @param <T>      the entity type
         * @param template the column template
         * @return the result of {@link ColumnTemplate#singleResult(ColumnQuery)}
         * @throws NullPointerException when manager is null
         */
        <T> Optional<T> getSingleResult(ColumnTemplate template);

        /**
         * Executes {@link ColumnTemplate#select(ColumnQuery)} using {@link Pagination}
         *
         * @param <T>        the entity type
         * @param template   the column template
         * @param pagination the pagination
         * @return the result of {@link ColumnTemplate#select(ColumnQuery)}
         * @throws NullPointerException when there are null parameters
         */
        <T> Stream<T> getResult(ColumnTemplate template, Pagination pagination);

        /**
         * Executes {@link ColumnTemplate#singleResult(ColumnQuery)} using {@link Pagination}
         *
         * @param <T>        the entity type
         * @param template   the column template
         * @param pagination the pagination
         * @return the result of {@link ColumnTemplate#singleResult(ColumnQuery)}
         * @throws NullPointerException when there are null parameters
         */
        <T> Optional<T> getSingleResult(ColumnTemplate template, Pagination pagination);


        /**
         * Creates a {@link Page} from pagination
         *
         * @param pagination the pagination
         * @param template   the template
         * @param <T>        the type
         * @return a {@link Page} from instance
         * @throws NullPointerException when there are null parameters
         */
        <T> Page<T> page(ColumnTemplate template, Pagination pagination);
    }

    /**
     * The Column Order whose define the position of the first result to retrieve.
     */
    interface ColumnMapperSkip extends ColumnMapperQueryBuild {


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        ColumnMapperLimit limit(long limit);
    }

    /**
     * The Column Where whose define the condition in the query.
     */
    interface ColumnMapperWhere extends ColumnMapperQueryBuild {


        /**
         * Starts a new condition in the select using
         * {@link jakarta.nosql.column.ColumnCondition#and(jakarta.nosql.column.ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnMapperNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnMapperNameCondition and(String name);

        /**
         * Appends a new condition in the select using
         * {@link jakarta.nosql.column.ColumnCondition#or(jakarta.nosql.column.ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnMapperNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnMapperNameCondition or(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         */
        ColumnMapperSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        ColumnMapperLimit limit(long limit);

        /**
         * Add the order how the result will return
         *
         * @param name the name to order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnMapperOrder orderBy(String name);
    }

    /**
     * @param <T> the entity type
     */
    interface ColumnPage<T> extends Page<T> {

        /**
         * The query of the current {@link Page}
         *
         * @return {@link ColumnQueryPagination}
         */
        ColumnQueryPagination getQuery();

        /**
         * Returns the {@link Page} requesting the next {@link Page}.
         *
         * @return the next {@link Page}
         */
        ColumnPage<T> next();


    }
}
