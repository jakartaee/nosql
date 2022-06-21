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
package jakarta.nosql.mapping;

import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Sort;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * This instance represents a group of one or more {@link jakarta.nosql.Sort} instances.
 * It is useful to either define or append sort from a field at a {@link Repository} within a parameter to query by a method.
 */
public interface Sorts {

    /**
     * Appends a {@link jakarta.nosql.Sort} instance of {@link jakarta.nosql.SortType#ASC}
     * from the name
     *
     * @param name the name
     * @return a {@link Sorts} instance
     * @throws NullPointerException when name is not null
     */
    Sorts asc(String name);

    /**
     * Appends a {@link jakarta.nosql.Sort} instance of {@link jakarta.nosql.SortType#DESC}
     * from the name
     *
     * @param name the name
     * @return a {@link Sorts} instance
     * @throws NullPointerException when name is not null
     */
    Sorts desc(String name);

    /**
     * Appends a {@link Sort} instance
     *
     * @param sort the sort
     * @return a {@link Sorts} instance
     * @throws NullPointerException when sort is null
     */
    Sorts add(Sort sort);

    /**
     * Removes a {@link Sort} at {@link Sorts}
     *
     * @param sort the sort to be removed
     * @return a {@link Sorts} instance
     * @throws NullPointerException when sort is null
     */
    Sorts remove(Sort sort);

    /**
     * @return a list of {@link Sort} within in {@link Sorts}
     */
    List<Sort> getSorts();


    /**
     * Creates a new instance of {@link Sorts}
     *
     * @return a {@link Sorts} instance
     */
    static Sorts sorts() {
        return ServiceLoaderProvider.get(SortsProvider.class,
                ()-> ServiceLoader.load(SortsProvider.class))
                .get();
    }

    /**
     * A provider class of {@link Sorts}
     */
    interface SortsProvider extends Supplier<Sorts> {

    }

}
