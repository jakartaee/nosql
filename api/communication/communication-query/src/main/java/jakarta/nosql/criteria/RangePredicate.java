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
 * An {@link Predicate} to apply a range operator
 *
 * @param <X> The root type
 * @param <L> The left hand side type
 * @param <R> The right hand side type
 */
public interface RangePredicate<X, L, R> extends Predicate<X> {
    
    enum Operator {
        INCLUSIVE_BETWEEN,
        EXCLUSIVE_BETWEEN
    }
    
    /**
     * Return the operator for this {@link Predicate}.
     *
     * @return negated predicate
     */
    Operator getOperator();

    /**
     * Return the left hand side for this {@link Predicate}.
     *
     * @return negated predicate
     */
    Expression<X, ?, L> getLeft();

    /**
     * Return the from value for this {@link Predicate}.
     *
     * @return negated predicate
     */
    R getFrom();
    
    /**
     * Return the to value for this {@link Predicate}.
     *
     * @return negated predicate
     */
    R getTo();

}
