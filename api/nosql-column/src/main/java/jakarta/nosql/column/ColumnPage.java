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

/**
 * @param <T> the entity type
 * @deprecated Use the Jakarta Data Page instead.
 */
public interface ColumnPage<T> extends Page<T> {

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
