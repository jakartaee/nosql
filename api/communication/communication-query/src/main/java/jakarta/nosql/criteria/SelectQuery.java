/*
 * Copyright (c) 2022 Otavio Santana and others
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
 *
 * Contributors:
 *     Alessandro Moscatelli
 *
 */
package jakarta.nosql.criteria;

import java.util.List;

/**
 * The <code>SelectQuery</code> interface defines functionality that is
 * specific to select queries.
 *
 * @param <X> the type of the root entity
 */
public interface SelectQuery<X> extends RestrictedQuery<X, SelectQueryResult<X>, SelectQuery<X>> {

    /**
     * Specify the ordering expressions that are used to order the query
     * results. Replaces the previous ordering expressions, if any. The
     * left-to-right sequence of the ordering expressions determines the
     * precedence, whereby the leftmost has highest precedence.
     *
     * @param sortings zero or more ordering expressions
     * @return the modified query
     */
    public SelectQuery<X> orderBy(List<Order<X, ?>> sortings);

    /**
     * Set the maximum number of results to retrieve.
     *
     * @param maxResults maximum number of results to retrieve
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    public SelectQuery<X> setMaxResults(int maxResults);

    /**
     * Set the position of the first result to retrieve.
     *
     * @param firstResult position of the first result, numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    public SelectQuery<X> setFirstResult(int firstResult);

}
