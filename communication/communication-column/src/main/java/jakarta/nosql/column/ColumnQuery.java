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
import jakarta.nosql.Sort;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Class that contains information to do a select to {@link ColumnEntity}
 *
 * @see ColumnFamilyManager#select(ColumnQuery)
 * @see ColumnCondition
 * @see Sort
 */
public interface ColumnQuery {


    /**
     * @return The maximum number of results the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long getLimit();

    /**
     * @return The position of the first result the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long getSkip();

    /**
     * The column family name
     *
     * @return the column family name
     */
    String getColumnFamily();

    /**
     * The conditions that contains in this {@link ColumnQuery}
     * If empty, {@link Optional#empty()} is true, the implementation might either return an unsupported exception or returns same elements in the database.
     *
     * @return the conditions
     */
    Optional<ColumnCondition> getCondition();

    /**
     * Returns the columns to returns in that query if empty will return all elements in the query.
     * The implementation might ignore this option.
     *
     * @return the columns
     */
    List<String> getColumns();

    /**
     * The sorts that contains in this {@link ColumnQuery}
     * The implementation might ignore this option.
     *
     * @return the sorts
     */
    List<Sort> getSorts();

    /**
     * Creates a query to Column
     *
     * @param columns - The column fields to query, optional.
     * @return a new {@link ColumnSelect} instance
     * @throws NullPointerException when there is a null element
     */
    static ColumnSelect select(String... columns) {
        return ServiceLoaderProvider.get(ColumnSelectProvider.class).apply(columns);
    }

    /**
     * Creates a query to Column
     *
     * @return a new {@link ColumnSelect} instance
     * @throws NullPointerException when there is a null element
     */
    static ColumnSelect select() {
        return ServiceLoaderProvider.get(ColumnSelectProvider.class).get();
    }

    /**
     * The ColumnFrom Query
     */
    interface ColumnFrom extends ColumnQueryBuild {


        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link ColumnNameCondition}
         * @throws NullPointerException when name is null
         */
        ColumnNameCondition where(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrive
         * @return a query with first result defined
         */
        ColumnSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        ColumnLimit limit(long limit);

        /**
         * Add the order how the result will returned
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnOrder orderBy(String name);


    }

    /**
     * The Column Order whose define the the maximum number of results to retrieve.
     */
    interface ColumnLimit extends ColumnQueryBuild {

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the number of elements to skip
         * @return a query with first result defined
         */
        ColumnSkip skip(long skip);

    }

    /**
     * The Column name order a query
     */
    interface ColumnNameOrder extends ColumnQueryBuild {


        /**
         * Add the order how the result will returned
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnOrder orderBy(String name);


        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrive
         * @return a query with first result defined
         */
        ColumnSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        ColumnLimit limit(long limit);


    }

    /**
     * The definition to either {@link jakarta.nosql.SortType}
     */
    interface ColumnOrder {


        /**
         * Defines the order as {@link jakarta.nosql.SortType#ASC}
         * @return the {@link ColumnNameOrder} instance
         */
        ColumnNameOrder asc();

        /**
         * Defines the order as {@link jakarta.nosql.SortType#DESC}
         * @return the {@link ColumnNameOrder} instance
         */
        ColumnNameOrder desc();
    }

    /**
     * The last step to the build of {@link ColumnQuery}.
     * It either can return a new {@link ColumnQuery} instance or execute a query with
     * {@link ColumnFamilyManager} and {@link ColumnFamilyManagerAsync}
     */
    interface ColumnQueryBuild {

        /**
         * Creates a new instance of {@link ColumnQuery}
         *
         * @return a new {@link ColumnQuery} instance
         */
        ColumnQuery build();

        /**
         * Executes {@link ColumnFamilyManager#select(ColumnQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link ColumnFamilyManager#select(ColumnQuery)}
         * @throws NullPointerException when manager is null
         */
        List<ColumnEntity> execute(ColumnFamilyManager manager);

        /**
         * Executes {@link ColumnFamilyManager#singleResult(ColumnQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link ColumnFamilyManager#singleResult(ColumnQuery)}
         * @throws NullPointerException when manager is null
         */
        Optional<ColumnEntity> executeSingle(ColumnFamilyManager manager);

        /**
         * Executes {@link ColumnFamilyManagerAsync#select(ColumnQuery, Consumer)}
         *
         * @param manager  the entity manager
         * @param callback the callback
         * @throws NullPointerException when there is null parameter
         */
        void execute(ColumnFamilyManagerAsync manager, Consumer<List<ColumnEntity>> callback);

        /**
         * Executes {@link ColumnFamilyManagerAsync#singleResult(ColumnQuery, Consumer)}
         *
         * @param manager  the entity manager
         * @param callback the callback
         * @throws NullPointerException when there is null parameter
         */
        void executeSingle(ColumnFamilyManagerAsync manager, Consumer<Optional<ColumnEntity>> callback);
    }

    /**
     * The initial element in the Column query
     */
    interface ColumnSelect {

        /**
         * Defines the column family in the query
         *
         * @param columnFamily the column family to query
         * @return a {@link ColumnFrom query}
         * @throws NullPointerException when columnFamily is null
         */
        ColumnFrom from(String columnFamily);
    }

    /**
     * A provider class of {@link ColumnSelect}
     */
    interface ColumnSelectProvider extends Function<String[], ColumnSelect>, Supplier<ColumnSelect> {
    }


    /**
     * The Column Order whose define the position of the first result to retrieve.
     */
    interface ColumnSkip extends ColumnQueryBuild {


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        ColumnLimit limit(long limit);

    }

    /**
     * The Column Where whose define the condition in the query.
     */
    interface ColumnWhere extends ColumnQueryBuild {


        /**
         * Starts a new condition in the select using
         * {@link ColumnCondition#and(ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnNameCondition and(String name);

        /**
         * Appends a new condition in the select using
         * {@link ColumnCondition#or(ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnNameCondition or(String name);

        /**
         * Defines the position of the first result to retrieve.
         *
         * @param skip the first result to retrive
         * @return a query with first result defined
         */
        ColumnSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         *
         * @param limit the limit
         * @return a query with the limit defined
         */
        ColumnLimit limit(long limit);

        /**
         * Add the order how the result will returned
         *
         * @param name the name to order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnOrder orderBy(String name);

    }

    /**
     * The base to name condition
     */
    interface ColumnNameCondition {


        /**
         * Creates the equals condition {@link Condition#EQUALS}
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere eq(T value);

        /**
         * Creates the like condition {@link Condition#LIKE}
         *
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        ColumnWhere like(String value);

        /**
         * Creates the greater than condition {@link Condition#GREATER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere gt(T value);

        /**
         * Creates the greater equals than condition {@link Condition#GREATER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere gte(T value);

        /**
         * Creates the lesser than condition {@link Condition#LESSER_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere lt(T value);

        /**
         * Creates the lesser equals than condition {@link Condition#LESSER_EQUALS_THAN}
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere lte(T value);

        /**
         * Creates the between condition {@link Condition#EQUALS}
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> ColumnWhere between(T valueA, T valueB);

        /**
         * Creates in condition {@link Condition#IN}
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere in(Iterable<T> values);

        /**
         * Creates the equals condition {@link Condition#NOT}
         *
         * @return {@link ColumnNotCondition}
         */
        ColumnNotCondition not();

    }

    /**
     * The column not condition
     */
    interface ColumnNotCondition extends ColumnNameCondition {
    }
}
