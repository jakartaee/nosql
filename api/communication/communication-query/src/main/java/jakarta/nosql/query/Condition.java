/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
 */

package jakarta.nosql.query;

/**
 * Condition performs different computations or actions depending on whether a boolean query
 * condition evaluates to true or false.
 * The conditions are composed of three elements.
 * The condition's name
 * The Operator
 * The Value
 *
 * @see Condition#getName()
 * @see Condition#getOperator()
 * @see Condition#getValue()
 */
public interface Condition {

    /**
     * the data source or target, to apply the operator
     *
     * @return the name
     */
    String getName();

    /**
     * that defines comparing process between the name and the value.
     *
     * @return the operator
     */
    Operator getOperator();

    /**
     * that data that receives the operation.
     *
     * @return the value
     */
    QueryValue<?> getValue();
}
