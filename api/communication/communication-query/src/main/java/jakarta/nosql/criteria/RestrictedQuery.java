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

import java.util.Collection;

/**
 * The <code>RestrictedQuery</code> interface defines functionality that is
 * specific to restricted queries.
 *
 * @param <T> the type of the root entity
 * @param <R> the type of the query result
 * @param <Q> the type of the restricted query
 */
public interface RestrictedQuery<T, R extends RestrictedQueryResult<T>, Q extends RestrictedQuery<T, R, Q>> extends ExecutableQuery<T, R> {

    /**
     * Modify the query to restrict the query result according to the
     * conjunction of the specified restriction predicates. Replaces the
     * previously added restriction(s), if any.
     *
     * @param restrictions zero or more restriction predicates
     * @return the modified query
     */
    Q where(Predicate<T>... restrictions);

    /**
     * Retrieves the restriction collection. 
     *
     * @return the restrictions
     */
    Collection<Predicate<T>> getRestrictions();

}
