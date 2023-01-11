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

import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.column.ColumnQuery;

import java.util.ServiceLoader;
import java.util.function.BiFunction;

/**
 * A {@link ColumnQuery} that allows select based on pagination.
 */
public interface ColumnQueryPagination extends ColumnQuery {


    /**
     * Returns the {@link ColumnQueryPagination} requesting the next {@link ColumnQueryPagination}.
     *
     * @return the next {@link ColumnQueryPagination}
     */
    ColumnQueryPagination next();

    /**
     * Returns the {@link Pagination} of the current {@link Page}
     *
     * @return a current {@link Pagination}
     */
    Pagination getPagination();


    /**
     * Creates a new instance of {@link ColumnQueryPagination}
     *
     * @param query      the query
     * @param pagination the pagination
     * @return a {@link ColumnQueryPagination} instance
     * @throws NullPointerException when there is null parameter
     */
    static ColumnQueryPagination of(ColumnQuery query, Pagination pagination) {
        return ServiceLoaderProvider.get(ColumnQueryPaginationProvider.class,
                ()-> ServiceLoader.load(ColumnQueryPaginationProvider.class))
                .apply(query, pagination);
    }

    /**
     * A provider {@link ColumnQueryPagination} of where given two parameters:
     * The first is {@link ColumnQuery}
     * The second is {@link Pagination}
     * it returns an instance {@link ColumnQueryPagination}
     */
    interface ColumnQueryPaginationProvider extends BiFunction<ColumnQuery, Pagination, ColumnQueryPagination> {

    }
}
