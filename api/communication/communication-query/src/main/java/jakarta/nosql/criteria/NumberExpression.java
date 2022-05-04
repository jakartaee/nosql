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
 * Type for query expressions representing a number attribute
 *
 * @param <X> the root type
 * @param <Y> the entity type
 * @param <T> the number type of the expression
 */
public interface NumberExpression<X, Y, T extends Number & Comparable> extends ComparableExpression<X, Y, T> {

    /**
     * Return the {@link CriteriaFunction} to sum this {@link NumberExpression}.
     *
     * @return operator
     */
    ExpressionFunction<X, Y, T, T> sum();
    
}
