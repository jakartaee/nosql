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

import jakarta.nosql.metamodel.ComparableAttribute;
import jakarta.nosql.metamodel.EntityAttribute;
import jakarta.nosql.metamodel.StringAttribute;
import jakarta.nosql.metamodel.ValueAttribute;

/**
 * The Entity type representing a path from the root type in a {@link CriteriaQuery}
 *
 * @param <X> the entity type referenced by the root
 * @param <Y> the destination type
 */
public interface Path<X extends Object, Y extends Object> {

    /**
     * Create a path corresponding to the referenced entity attribute
     *
     * @param <Z> the type of the entity attribute
     * @param attribute entity attribute
     * @return path corresponding to the entity attribute
     */
    public <Z extends Object> Path<X, Z> get(EntityAttribute<? super Y, Z> attribute);
    
    /**
     * Create an expression corresponding to the referenced single-valued
     * attribute
     *
     * @param <Z> the type of the attribute
     * @param attribute single-valued attribute
     * @return expression corresponding to the referenced attribute
     */
    public <Z extends Object> Expression<X, Z> get(ValueAttribute<? super Y, Z> attribute);

    /**
     * Create an expression corresponding to the referenced single-valued string
     * attribute
     *
     * @param attribute single-valued string attribute
     * @return string expression corresponding to the referenced string
     * attribute
     */
    public StringExpression<X> get(StringAttribute<? extends Y> attribute);

    /**
     * Create an expression corresponding to the referenced single-valued
     * comparable attribute
     *
     * @param <Z> the type of the comparable attribute
     * @param attribute single-valued comparable attribute
     * @return comparable expression corresponding to the referenced comparable
     * attribute
     */
    public <Z extends Comparable> ComparableExpression<X, Z> get(ComparableAttribute<? super Y, Z> attribute);

}
