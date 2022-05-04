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
 * A predicate representing a restriction in a {@link CriteriaQuery}
 *
 * @param <T> The Entity type whose fetching is to be be restricted
 */
public interface Predicate<T> {

    /**
     * Create a negation of this restriction.
     *
     * @return not predicate
     */
    NegationPredicate<T> not();

    /**
     * Create a conjunction of this with the argument restriction
     *
     * @param restriction restriction
     * @return and predicate
     */
    ConjunctionPredicate<T> and(Predicate<T> restriction);

    /**
     * Create a disjunction of this with the argument restriction
     *
     * @param restriction restriction
     * @return or predicate
     */
    DisjunctionPredicate<T> or(Predicate<T> restriction);

}
