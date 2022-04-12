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

/**
 * Type for query expressions representing an Entity attribute
 *
 * @param <X> the entity type
 * @param <T> the type of the expression
 */
public interface Expression<X extends Object, T extends Object> {

    /**
     * Create a predicate for testing if the expression is equal to the argument
     * expression
     *
     * @param expression the expression to check the equality against
     * @return equality predicate
     */
    public Predicate<X> equal(Expression<X, T> expression);

    /**
     * Create a predicate for testing if the expression is equal to the argument
     * value
     *
     * @param value the value to check the equality against
     * @return equality predicate
     */
    public Predicate<X> equal(T value);

    /**
     * Create a predicate to test whether the expression is a member of the
     * argument list.
     *
     * @param values values to be tested against
     * @return predicate testing for membership
     */
    public Predicate<X> in(Collection<T> values);

}
