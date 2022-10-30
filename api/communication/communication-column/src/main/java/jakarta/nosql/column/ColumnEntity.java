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
package jakarta.nosql.column;


import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.TypeSupplier;
import jakarta.nosql.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;

/**
 * A column family is a NoSQL object that contains columns of related data. It is a tuple (pair) that consists
 * of a key-value pair, where the key is mapped to a value that is a set of columns.
 */
public interface ColumnEntity {


    /**
     * Creates a column family instance
     *
     * @param name a name to column family
     * @return a ColumnEntity instance
     */
    static ColumnEntity of(String name) {
        return ServiceLoaderProvider.get(ColumnEntityProvider.class,
                ()-> ServiceLoader.load(ColumnEntityProvider.class))
                .apply(name);
    }

    /**
     * Creates a column family instance
     *
     * @param name    a name to column family
     * @param columns - columns
     * @return a ColumnEntity instance
     * @throws NullPointerException when either name or columns are null
     */
    static ColumnEntity of(String name, List<Column> columns) {
        ColumnEntity entity = ServiceLoaderProvider.get(ColumnEntityProvider.class,
                ()-> ServiceLoader.load(ColumnEntityProvider.class)).apply(name);
        entity.addAll(columns);
        return entity;
    }

    /**
     * Appends all the columns in the column family to the end of this list.
     *
     * @param columns - columns to be added
     * @throws NullPointerException when columns is null
     */
    void addAll(List<Column> columns);

    /**
     * Appends the specified column to the end of this list
     *
     * @param column - column to be added
     * @throws NullPointerException when column is null
     */
    void add(Column column);

    /**
     * add a column within {@link ColumnEntity}
     *
     * @param name  a name of the column
     * @param value the information of the column
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    void add(String name, Object value);

    /**
     * add a column within {@link ColumnEntity}
     *
     * @param name  a name of the column
     * @param value the information of the column
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    void add(String name, Value value);

    /**
     * Converts the columns to a Map where:
     * the key is the name the column
     * The value is the {@link Value#get()} of the map
     *
     * @return a map instance
     */
    Map<String, Object> toMap();

    /**
     * Returns all columns from this Column Family
     *
     * @return an immutable list of columns
     */
    List<Column> getColumns();

    /**
     * Column Family's name
     *
     * @return Column Family's name
     */
    String getName();

    /**
     * Remove a column whose name is informed in parameter.
     *
     * @param name a column name
     * @return if a column was removed or not
     * @throws NullPointerException when column is null
     */
    boolean remove(String name);

    /**
     * Find column a column from columnName
     *
     * @param columnName a column name
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when columnName is null
     */
    Optional<Column> find(String columnName);

    /**
     * Find a column and converts to specific value from {@link Class}
     * It is an alias to {@link Value#get(Class)}
     *
     * @param columnName a name of a column
     * @param type       the type to convert the value
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when there are null parameters
     */
    <T> Optional<T> find(String columnName, Class<T> type);

    /**
     * Find a column and converts to specific value from {@link TypeSupplier}
     * It is an alias to {@link Value#get(TypeSupplier)}
     *
     * @param columnName a name of a column
     * @param type       the type to convert the value
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when there are null parameters
     */
    <T> Optional<T> find(String columnName, TypeSupplier<T> type);

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    int size();

    /**
     * Returns true if the number of columns is zero otherwise false.
     *
     * @return true if there isn't elements to {@link ColumnEntity#getColumns()}
     */
    boolean isEmpty();

    /**
     * make copy of itself
     *
     * @return an instance copy
     */
    ColumnEntity copy();

    /**
     * Returns a Set view of the names of column contained in Column Entity
     *
     * @return the keys
     */
    Set<String> getColumnNames();

    /**
     * Returns a Collection view of the values contained in this ColumnEntity.
     *
     * @return the collection of values
     */
    Collection<Value> getValues();

    /**
     * Returns true if this ColumnEntity contains a column whose the name is informed
     *
     * @param columnName the column name
     * @return true if find a column and otherwise false
     */
    boolean contains(String columnName);

    /**
     * Removes all Columns
     */
    void clear();

    /**
     * A provider of {@link ColumnEntity} where it will return from the column entity name.
     */
    interface ColumnEntityProvider extends Function<String, ColumnEntity> {
    }
}
