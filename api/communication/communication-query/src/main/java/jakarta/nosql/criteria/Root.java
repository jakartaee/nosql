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

import jakarta.nosql.metamodel.ComparableAttribute;
import jakarta.nosql.metamodel.SingularAttribute;
import jakarta.nosql.metamodel.StringAttribute;

/**
 * The Entity type representing the from clause of a {@link CriteriaQuery}
 *
 * @param <X> the entity type referenced by the root
 */
public interface Root<X extends Object> {

    /**
     * Create an expression corresponding to the referenced single-valued
     * attribute
     *
     * @param <Y> the type of the attribute
     * @param attribute single-valued attribute
     * @return expression corresponding to the referenced attribute
     */
    public <Y extends Object> Expression<X, Y> get(SingularAttribute<? super X, Y> attribute);

    /**
     * Create an expression corresponding to the referenced single-valued string
     * attribute
     *
     * @param attribute single-valued string attribute
     * @return string expression corresponding to the referenced string
     * attribute
     */
    public StringExpression<X> get(StringAttribute<? extends X> attribute);

    /**
     * Create an expression corresponding to the referenced single-valued
     * comparable attribute
     *
     * @param <Y> the type of the comparable attribute
     * @param attribute single-valued comparable attribute
     * @return comparable expression corresponding to the referenced comparable
     * attribute
     */
    public <Y extends Comparable> ComparableExpression<X, Y> get(ComparableAttribute<? super X, Y> attribute);

}
