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
 *
 * Contributors:
 *     Alessandro Moscatelli
 *
 */
package jakarta.nosql.criteria;

import java.util.Collection;
import java.util.List;

/**
 * The <code>CriteriaQuery</code> interface defines functionality that is
 * specific to top-level queries.
 *
 * @param <T> the type of the defined result
 */
public interface CriteriaQuery<T extends Object> {

    /**
     * Returns the from clause to be restricted
     *
     * @return from clause
     */
    public Root<T> getRoot();

    /**
     * Modify the query to restrict the query result according to the
     * conjunction of the specified restriction predicates. Replaces the
     * previously added restriction(s), if any.
     *
     * @param restrictions zero or more restriction predicates
     * @return the modified query
     */
    public CriteriaQuery<T> where(Collection<Predicate<T>> restrictions);

    /**
     * Specify the ordering expressions that are used to order the query
     * results. Replaces the previous ordering expressions, if any. The
     * left-to-right sequence of the ordering expressions determines the
     * precedence, whereby the leftmost has highest precedence.
     *
     * @param o zero or more ordering expressions
     * @return the modified query
     */
    public CriteriaQuery<T> orderBy(List<Order<T>> o);

    /**
     * Set the maximum number of results to retrieve.
     *
     * @param maxResult maximum number of results to retrieve
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    public CriteriaQuery<T> setMaxResults(int maxResult);

    /**
     * Set the position of the first result to retrieve.
     *
     * @param startPosition position of the first result, numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    public CriteriaQuery<T> setFirstResult(int startPosition);

}
