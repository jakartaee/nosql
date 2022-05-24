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

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * An unit condition  to run a column family select
 *
 * @see ColumnFamilyManager#select(ColumnQuery)
 */
public interface ColumnCondition {

    /**
     * Gets the column to be used in the select
     *
     * @return a column instance
     */
    Column getColumn();

    /**
     * Gets the conditions to be used in the select
     *
     * @return a Condition instance
     * @see Condition
     */
    Condition getCondition();

    /**
     * Creates a new {@link ColumnCondition} using the {@link Condition#AND}
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    ColumnCondition and(ColumnCondition condition);

    /**
     * Creates a new {@link ColumnCondition} negating the current one
     *
     * @return the negated condition
     * @see Condition#NOT
     */
    ColumnCondition negate();

    /**
     * Creates a new {@link ColumnCondition} using the {@link Condition#OR}
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    ColumnCondition or(ColumnCondition condition);

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#EQUALS}, it means a select will scanning to a
     * column family that has the same name and equals value informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition eq(Column column) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).apply(column, Condition.EQUALS);
    }

    /**
     * an alias method to {@link ColumnCondition#eq(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     * @param name the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when either name or value is null
     */
    static ColumnCondition eq(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(ColumnConditionProvider.class)
                .apply(Column.of(name, value), Condition.EQUALS);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_THAN}, it means a select will scanning to a
     * column family that has the same name and the value  greater than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition gt(Column column) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).apply(column, Condition.GREATER_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#gt(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     * @param name the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when either name or value is null
     */
    static ColumnCondition gt(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(ColumnConditionProvider.class)
                .apply(Column.of(name, value), Condition.GREATER_THAN);
    }
    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_EQUALS_THAN},
     * it means a select will scanning to a column family that has the same name and the value
     * greater or equals than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition gte(Column column) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).apply(column, Condition.GREATER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#gte(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     * @param name the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when either name or value is null
     */
    static ColumnCondition gte(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(ColumnConditionProvider.class)
                .apply(Column.of(name, value), Condition.GREATER_EQUALS_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_THAN}, it means a select will scanning to a
     * column family that has the same name and the value  lesser than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition lt(Column column) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).apply(column, Condition.LESSER_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#lt(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     * @param name the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when either name or value is null
     */
    static ColumnCondition lt(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(ColumnConditionProvider.class)
                .apply(Column.of(name, value), Condition.LESSER_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_EQUALS_THAN},
     * it means a select will scanning to a column family that has the same name and the value
     * lesser or equals than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition lte(Column column) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).apply(column, Condition.LESSER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#lte(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     * @param name the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when either name or value is null
     */
    static ColumnCondition lte(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(ColumnConditionProvider.class)
                .apply(Column.of(name, value), Condition.LESSER_EQUALS_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#IN}, it means a select will scanning to a
     * column family that has the same name and the value is within informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#IN}
     * @throws NullPointerException     when column is null
     * @throws IllegalArgumentException when the {@link Column#get()} in not an iterable implementation
     */
    static ColumnCondition in(Column column) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).in(column);
    }

    /**
     * an alias method to {@link ColumnCondition#in(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     * @param name the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#IN}
     * @throws NullPointerException when either name or value is null
     */
    static ColumnCondition in(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(ColumnConditionProvider.class)
                .in(Column.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LIKE}, it means a select will scanning to a
     * column family that has the same name and the value  is like than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition like(Column column) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).apply(column, Condition.LIKE);
    }

    /**
     * an alias method to {@link ColumnCondition#like(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     * @param name the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when either name or value is null
     */
    static ColumnCondition like(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(ColumnConditionProvider.class)
                .apply(Column.of(name, value), Condition.LIKE);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#BETWEEN},
     * it means a select will scanning to a column family that is between two values informed
     * on a column name.
     * The column must have a {@link Column#get()} an {@link Iterable} implementation
     * with just two elements.
     *
     * @param column a column instance
     * @return The between condition
     * @throws NullPointerException     when column is null
     * @throws IllegalArgumentException When the column neither has an Iterable instance or two elements on
     *                                  an Iterable.
     */
    static ColumnCondition between(Column column) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).between(column);
    }

    /**
     * an alias method to {@link ColumnCondition#between(Column)} (Column) where it will create a {@link Column}
     * instance first and then apply te condition.
     * @param name the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#BETWEEN}
     * @throws NullPointerException when either name or value is null
     */
    static ColumnCondition between(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return ServiceLoaderProvider.get(ColumnConditionProvider.class)
                .between(Column.of(name, value));
    }


    /**
     * Returns a new {@link ColumnCondition} aggregating ,as "AND", all the conditions as just one condition.
     * The {@link Column} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link java.util.List} of all conditions, in other words.
     * <p>Given:</p>
     * {@code
     * Column age = Column.of("age", 26);
     * Column name = Column.of("name", "otavio");
     * ColumnCondition condition = ColumnCondition.eq(name).and(ColumnCondition.gte(age));
     * }
     * The {@link ColumnCondition#getColumn()} will have "_AND" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link ColumnCondition} instance
     * @throws NullPointerException when the conditions is null
     */
    static ColumnCondition and(ColumnCondition... conditions) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).and(conditions);
    }

    /**
     * Returns a new {@link ColumnCondition} aggregating ,as "OR", all the conditions as just one condition.
     * The {@link Column} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link java.util.List} of all conditions, in other words.
     * <p>Given:</p>
     * {@code
     * Column age = Column.of("age", 26);
     * Column name = Column.of("name", "otavio");
     * ColumnCondition condition = ColumnCondition.eq(name).or(ColumnCondition.gte(age));
     * }
     * The {@link ColumnCondition#getColumn()} will have "_OR" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link ColumnCondition} instance
     * @throws NullPointerException when the condition is null
     */
    static ColumnCondition or(ColumnCondition... conditions) {
        return ServiceLoaderProvider.get(ColumnConditionProvider.class).or(conditions);
    }


    /**
     * A Supplier of {@link ColumnCondition} where it will create from two parameters:
     * The first one is {@link Column}
     * The second one is the Condition
     */
    interface ColumnConditionProvider extends BiFunction<Column, Condition, ColumnCondition> {

        /**
         * Creates a {@link Condition#BETWEEN} operation
         * @param column the column
         * @return a {@link ColumnCondition}
         */
        ColumnCondition between(Column column);

        /**
         * Creates a {@link Condition#AND} operation
         * @param conditions the conditions
         * @return a {@link ColumnCondition}
         */
        ColumnCondition and(ColumnCondition... conditions);

        /**
         * Creates a {@link Condition#OR} operation
         * @param conditions the conditions
         * @return a {@link ColumnCondition}
         */
        ColumnCondition or(ColumnCondition... conditions);

        /**
         * Creates a {@link Condition#IN} operation
         * @param column the column
         * @return a {@link ColumnCondition}
         */
        ColumnCondition in(Column column);
    }
}
