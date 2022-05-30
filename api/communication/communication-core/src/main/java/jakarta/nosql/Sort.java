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
package jakarta.nosql;


import java.util.function.BiFunction;

/**
 * This element represents a required order to be used in a query, it has two attributes:
 * -- The name - the field's name to be sorted
 * -- The type - the way to be sorted
 *
 * @see Sort#of(String, SortType)
 * @see SortType
 */
public interface Sort {

    /**
     * Returns the field name
     *
     * @return the field name
     */
    String getName();

    /**
     * The {@link SortType}
     *
     * @return The {@link SortType}
     */
    SortType getType();

    /**
     * Creates a wew Sort instance to be used in a NoSQL query.
     *
     * @param name - the field name be used in a sort process
     * @param type - the way to be sorted
     * @return a sort instance
     * @throws NullPointerException when there are null parameters
     */
    static Sort of(String name, SortType type) {
        return ServiceLoaderProvider.get(SortProvider.class).apply(name, type);
    }

    /**
     * Creates a new Sort of the type {@link SortType#ASC}
     *
     * @param name the field name be used in a sort process
     * @return a sort instance
     * @throws NullPointerException when name is null
     */
    static Sort asc(String name) {
        return of(name, SortType.ASC);
    }

    /**
     * Creates a new Sort of the type {@link SortType#DESC}
     *
     * @param name the field name be used in a sort process
     * @return a sort instance
     * @throws NullPointerException when name is null
     */
    static Sort desc(String name) {
        return of(name, SortType.DESC);
    }


    /**
     * A provider of {@link Sort} where given a {@link String} and a {@link SortType}
     * it returns a new instance of {@link Sort}
     */
    interface SortProvider extends BiFunction<String, SortType, Sort> {

    }
}
