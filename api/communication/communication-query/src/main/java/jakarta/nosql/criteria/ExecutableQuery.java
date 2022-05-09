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
 * The <code>ExecutableQuery</code> interface defines functionality that is
 * specific to executable queries.
 *
 * @param <T> the type of the root entity
 * @param <R> the type of the query result
 */
public interface ExecutableQuery<T, R extends CriteriaQueryResult<T>> {
    
    /**
     * Retrieves the root type. 
     *
     * @return root type
     */
    Class<T> getType();
    
    /**
     * Retrieves the result. 
     *
     * @return result
     */
    R getResult();
    
}
