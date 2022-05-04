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
 * The <code>FunctionQuery</code> interface defines functionality that is
 * specific to function queries.
 *
 * @param <X> the type of the root entity
 */
public interface FunctionQuery<X> extends RestrictedQuery<X, FunctionQueryResult<X>, FunctionQuery<X>> {
    
    /**
     * Return the collection of {@link CriteriaFunction}s to retrieve.
     *
     * @return collection of functions
     */
    Collection<CriteriaFunction<X, ?, ?, ?>> getFunctions();
    
    /**
     * Specify the expressions that are used to form groups over
     * the query results.
     * Replaces the previous specified grouping expressions, if any.
     * If no grouping expressions are specified, any previously 
     * added grouping expressions are simply removed.
     * @param grouping  zero or more grouping expressions
     * @return the aggregated query
     */
    AggregatedQuery<X> groupBy(Expression<X, ?, ?>... grouping);

}
