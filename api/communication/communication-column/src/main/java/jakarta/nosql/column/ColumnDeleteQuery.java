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
     * Creates a delete query to Column
     *
     * @param columns - The column fields to query, optional.
     * @return a new {@link ColumnDelete} instance
     * @throws NullPointerException when there is a null element
     */
    static ColumnDelete delete(String... columns) {
        return ServiceLoaderProvider.get(ColumnDeleteProvider.class).apply(columns);
    }

    /**
     * Creates a delete query to Column
     * @return a new {@link ColumnDelete} instance
     * @throws NullPointerException when there is a null element
     */
    static ColumnDelete delete() {
        return ServiceLoaderProvider.get(ColumnDeleteProvider.class).get();
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
}
