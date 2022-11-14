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

package jakarta.nosql;

import java.util.Arrays;
import java.util.Objects;

/**
 * Conditions type to run a query
 */
public enum Condition {
    EQUALS, GREATER_THAN, GREATER_EQUALS_THAN, LESSER_THAN, LESSER_EQUALS_THAN, IN, LIKE, AND, OR, NOT, BETWEEN;

    /**
     * Return tne field as name to both document and column.
     * The goal is to be a reserved word.
     * The formula is: underscore plus the {@link Enum#name()}
     * So, call this method on {@link Condition#EQUALS}  will return "_EQUALS"
     *
     * @return the keyword to condition
     */
    public String getNameField() {
        return '_' + this.name();
    }

    /**
     * Retrieve the condition from {@link Condition#getNameField()} on case-sensitive
     *
     * @param condition the condition converted to field
     * @return the condition instance
     * @throws NullPointerException     when condition is null
     * @throws IllegalArgumentException when the condition is not found
     */
    public static Condition parse(String condition) {
        Objects.requireNonNull(condition, "condition is required");
        return Arrays.stream(Condition.values())
                .filter(c -> c.getNameField()
                        .equals(condition)).findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("The condition %s is not found", condition)));
    }
}
