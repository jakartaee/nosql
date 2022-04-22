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
 * @param <X> the entity type
 * @param <T> the comparable type of the expression
 */
public interface ComparableExpression<X extends Object, T extends Comparable> extends Expression<X, T> {

    /**
     * Create a predicate for testing whether this expression is greater than
     * the argument expression
     *
     * @param <Y> the comparable type or subtype
     * @param expression expression
     * @return greater-than predicate
     */
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Expression<X, Y>> greaterThan(Expression<X, ? extends Y> expression);

    /**
     * Create a predicate for testing whether this expression is greater than
     * the argument value
     *
     * @param <Y> the comparable type or subtype
     * @param y value
     * @return greater-than predicate
     */
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Y> greaterThan(Y y);

    /**
     * Create a predicate for testing whether this expression is greater than or
     * equal to the argument expression
     *
     * @param <Y> the comparable type or subtype
     * @param expression expression
     * @return greater-than-or-equal predicate
     */
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Expression<X, Y>> greaterThanOrEqualTo(Expression<X, ? extends Y> expression);

    /**
     * Create a predicate for testing whether this expression is greater than or
     * equal to the argument value
     *
     * @param <Y> the comparable type or subtype
     * @param y value
     * @return greater-than-or-equal predicate
     */
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Y> greaterThanOrEqualTo(Y y);

    /**
     * Create a predicate for testing whether this expression is less than the
     * argument expression
     *
     * @param <Y> the comparable type or subtype
     * @param expression expression
     * @return greater-than-or-equal predicate
     */
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Expression<X, Y>> lessThan(Expression<X, ? extends Y> expression);

    /**
     * Create a predicate for testing whether this expression is less than the
     * argument value
     *
     * @param <Y> the comparable type or subtype
     * @param y value
     * @return greater-than-or-equal predicate
     */
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Y> lessThan(Y y);

    /**
     * Create a predicate for testing whether this expression is less than or
     * equal to the argument expression
     *
     * @param <Y> the comparable type or subtype
     * @param expression expression
     * @return greater-than-or-equal predicate
     */
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Expression<X, Y>> lessThanOrEqualTo(Expression<X, ? extends Y> expression);

    /**
     * Create a predicate for testing whether this expression is less than or
     * equal to the argument value
     *
     * @param <Y> the comparable type or subtype
     * @param y value
     * @return greater-than-or-equal predicate
     */
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Y> lessThanOrEqualTo(Y y);

    /**
     * Create a predicate for testing whether the this expression is between the
     * first and second argument expressions in value
     *
     * @param <Y> the comparable type or subtype
     * @param exprsn1 expression
     * @param exprsn2 expression
     * @return between predicate
     */
    public <Y extends Comparable<? super Y>> RangePredicate<X, Y, Expression<X, Y>> exclusiveBetween(Expression<X, ? extends Y> exprsn1, Expression<X, ? extends Y> exprsn2);

    /**
     * Create a predicate for testing whether the this expression value is
     * between the first and second argument values
     *
     * @param <Y> the comparable type or subtype
     * @param x value
     * @param y value
     * @return between predicate
     */
    public <Y extends Comparable<? super Y>> RangePredicate<X, Y, Y> exclusiveBetween(Y x, Y y);

    /**
     * Create a predicate for testing whether the this expression is between the
     * first and second argument expressions in value
     *
     * @param <Y> the comparable type or subtype
     * @param exprsn1 expression
     * @param exprsn2 expression
     * @return between predicate
     */
    public <Y extends Comparable<? super Y>> RangePredicate<X, Y, Expression<X, Y>> inclusiveBetween(Expression<X, ? extends Y> exprsn1, Expression<X, ? extends Y> exprsn2);

    /**
     * Create a predicate for testing whether the this expression value is
     * between the first and second argument values
     *
     * @param <Y> the comparable type or subtype
     * @param x value
     * @param y value
     * @return between predicate
     */
    public <Y extends Comparable<? super Y>> RangePredicate<X, Y, Y> inclusiveBetween(Y x, Y y);

    /**
     * Create an ordering by the ascending value of this expression
     *
     * @param <T> the comparable type
     * @return ascending ordering corresponding to the expression
     */
    public <T extends Object> Order<T> asc();

    /**
     * Create an ordering by the descending value of the expression
     *
     * @param <T> the comparable type
     * @return descending ordering corresponding to the expression
     */
    public <T extends Object> Order<T> desc();

}
