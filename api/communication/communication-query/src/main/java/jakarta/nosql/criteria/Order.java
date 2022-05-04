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

/**
 * An object that defines an ordering over the query results
 *
 * @param <X> the root type
 * @param <T> the type of the defined result
 */
public interface Order<X, T extends Comparable> {

    /**
     * Whether ascending ordering is in effect
     *
     * @return boolean indicating whether ordering is ascending
     */
    boolean isAscending();

    /**
     * Return the expression that is used for ordering
     *
     * @return expression used for ordering
     */
    ComparableExpression<X, ?, T> getExpression();

}
