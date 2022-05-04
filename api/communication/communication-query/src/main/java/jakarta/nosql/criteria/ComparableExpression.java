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
 * Type for query expressions representing a comparable Entity attribute
 *
 * @param <X> the root type
 * @param <Y> the entity type
 * @param <T> the comparable type of the expression
 */
public interface ComparableExpression<X, Y, T extends Comparable> extends Expression<X, Y, T> {

    /**
     * Create a predicate for testing whether this expression is greater than
     * the argument expression
     *
     * @param expression expression
     * @return greater-than predicate
     */
    BinaryPredicate<X, T, Expression<X, ? ,T>> greaterThan(Expression<X, ?, T> expression);

    /**
     * Create a predicate for testing whether this expression is greater than
     * the argument value
     *
     * @param y value
     * @return greater-than predicate
     */
    BinaryPredicate<X, T, T> greaterThan(T y);

    /**
     * Create a predicate for testing whether this expression is greater than or
     * equal to the argument expression
     *
     * @param expression expression
     * @return greater-than-or-equal predicate
     */
    BinaryPredicate<X, T, Expression<X, ?, T>> greaterThanOrEqualTo(Expression<X, ?, T> expression);

    /**
     * Create a predicate for testing whether this expression is greater than or
     * equal to the argument value
     *
     * @param y value
     * @return greater-than-or-equal predicate
     */
    BinaryPredicate<X, T, T> greaterThanOrEqualTo(T y);

    /**
     * Create a predicate for testing whether this expression is less than the
     * argument expression
     *
     * @param expression expression
     * @return greater-than-or-equal predicate
     */
    BinaryPredicate<X, T, Expression<X, ?, T>> lessThan(Expression<X, ?, T> expression);

    /**
     * Create a predicate for testing whether this expression is less than the
     * argument value
     *
     * @param y value
     * @return greater-than-or-equal predicate
     */
    BinaryPredicate<X, T, T> lessThan(T y);

    /**
     * Create a predicate for testing whether this expression is less than or
     * equal to the argument expression
     *
     * @param expression expression
     * @return greater-than-or-equal predicate
     */
    BinaryPredicate<X, T, Expression<X, ?, T>> lessThanOrEqualTo(Expression<X, ?, T> expression);

    /**
     * Create a predicate for testing whether this expression is less than or
     * equal to the argument value
     *
     * @param y value
     * @return greater-than-or-equal predicate
     */
    BinaryPredicate<X, T, T> lessThanOrEqualTo(T y);

    /**
     * Create a predicate for testing whether the this expression is between the
     * first and second argument expressions in value
     *
     * @param exprsn1 expression
     * @param exprsn2 expression
     * @return between predicate
     */
    RangePredicate<X, T, Expression<X, ?, T>> exclusiveBetween(Expression<X, ?, T> exprsn1, Expression<X, ?, T> exprsn2);

    /**
     * Create a predicate for testing whether the this expression value is
     * between the first and second argument values
     *
     * @param x value
     * @param y value
     * @return between predicate
     */
    RangePredicate<X, T, T> exclusiveBetween(T x, T y);

    /**
     * Create a predicate for testing whether the this expression is between the
     * first and second argument expressions in value
     *
     * @param exprsn1 expression
     * @param exprsn2 expression
     * @return between predicate
     */
    RangePredicate<X, T, Expression<X, ?, T>> inclusiveBetween(Expression<X, ?, T> exprsn1, Expression<X, ?, T> exprsn2);

    /**
     * Create a predicate for testing whether the this expression value is
     * between the first and second argument values
     *
     * @param x value
     * @param y value
     * @return between predicate
     */
    RangePredicate<X, T, T> inclusiveBetween(T x, T y);

    /**
     * Create an ordering by the ascending value of this expression
     *
     * @return ascending ordering corresponding to the expression
     */
    Order<X, T> asc();

    /**
     * Create an ordering by the descending value of the expression
     *
     * @return descending ordering corresponding to the expression
     */
    Order<X, T> desc();

}
