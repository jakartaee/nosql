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
         * Creates the equals condition
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere eq(T value);

        /**
         * Creates the like condition
         *
         * @param value the value to the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        MapperDeleteWhere like(String value);

        /**
         * Creates the greater than condition
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gt(T value);

        /**
         * Creates the greater equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere gte(T value);

        /**
         * Creates the lesser than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lt(T value);

        /**
         * Creates the lesser equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere lte(T value);

        /**
         * Creates the between condition
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> MapperDeleteWhere between(T valueA, T valueB);

        /**
         * Creates in condition
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link MapperDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperDeleteWhere in(Iterable<T> values);

        /**
         * Creates the equals condition.
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
     * The last step to the build of execution
     */
    interface MapperDeleteQueryBuild {


        /**
         * Executes the query
         */
        void execute();

    }

    /**
     * The Column Where whose define the condition in the delete query.
     */
    interface MapperDeleteWhere extends MapperDeleteQueryBuild {


        /**
         * It starts a new condition performing a logical conjunction using two predicates.
         *
         * @param name a condition to be added
         * @return the same {@link MapperDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        MapperDeleteNameCondition and(String name);

        /**
         * It starts a new condition performing a logical disjunction using two predicates.
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
         * Creates the equals condition
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere eq(T value);

        /**
         * Creates the like condition
         *
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        MapperWhere like(String value);

        /**
         * Creates the greater than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere gt(T value);

        /**
         * Creates the greater equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere gte(T value);

        /**
         * Creates the lesser than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere lt(T value);

        /**
         * Creates the lesser equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere lte(T value);

        /**
         * Creates the between condition
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link MapperWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> MapperWhere between(T valueA, T valueB);

        /**
         * Creates in condition
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link MapperWhere}
         * @throws NullPointerException when value is null
         */
        <T> MapperWhere in(Iterable<T> values);

        /**
         * Creates the equals condition.
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
     * The definition to either
     */
    interface MapperOrder {


        /**
         * Defines the order ascending
         *
         * @return the {@link MapperNameOrder} instance
         */
        MapperNameOrder asc();

        /**
         * Defines the order as descending
         *
         * @return the {@link MapperNameOrder} instance
         */
        MapperNameOrder desc();
    }

    /**
     * The last step to the build select query
     */
    interface MapperQueryBuild {


        /**
         * Executes the query and it returns as a {@link List}
         *
         * @param <T> the entity type
         * @return the result of the query
         */
        <T> List<T> result();

        /**
         * Executes the query and it returns as a Stream
         *
         * @param <T> the entity type
         * @return the result of the query
         */
        <T> Stream<T> stream();

        /**
         * Executes and returns a single result
         *
         * @param <T> the entity type
         * @return the result of the query that may have one or empty result
         */
        <T> Optional<T> singleResult();


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
         * It starts a new condition performing a logical conjunction using two predicates.
         *
         * @param name a condition to be added
         * @return the same {@link MapperNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        MapperNameCondition and(String name);

        /**
         * It starts a new condition performing a logical disjunction using two predicates.
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
