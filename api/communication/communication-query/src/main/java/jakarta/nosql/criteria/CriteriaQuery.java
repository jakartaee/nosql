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

/**
 * The <code>CriteriaQuery</code> interface defines functionality that is
 * specific to top-level queries.
 *
 * @param <T> the type of the root entity
 */
public interface CriteriaQuery<T extends Object> {

    /**
     * Returns the query root
     *
     * @return from clause
     */
    public Root<T> from();

    /**
     * Creates a function query
     *
     * @param functions to be computed
     * @return function query
     */    
    public FunctionQuery<T> select(CriteriaFunction<T, ?, ?>... functions);

    /**
     * Creates a select query
     *
     * @param expressions to retrieve
     * @return select query
     */    
    public SelectQuery<T> select(Expression<T, ?>... expressions);
    
}
