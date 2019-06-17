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
 */
package jakarta.nosql;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ConditionTest {

    @Test
    public void shouldReturnNameField() {
        Assertions.assertEquals("_AND", Condition.AND.getNameField());
        Assertions.assertEquals("_EQUALS", Condition.EQUALS.getNameField());
        Assertions.assertEquals("_GREATER_EQUALS_THAN", Condition.GREATER_EQUALS_THAN.getNameField());
        Assertions.assertEquals("_IN", Condition.IN.getNameField());
        Assertions.assertEquals("_NOT", Condition.NOT.getNameField());
        Assertions.assertEquals("_OR", Condition.OR.getNameField());
        Assertions.assertEquals("_LESSER_THAN", Condition.LESSER_THAN.getNameField());
    }

    @Test
    public void shouldParser() {
        Assertions.assertEquals(Condition.AND,Condition.parse("_AND"));
        Assertions.assertEquals(Condition.EQUALS,Condition.parse("_EQUALS"));
        Assertions.assertEquals(Condition.GREATER_EQUALS_THAN,Condition.parse("_GREATER_EQUALS_THAN"));
        Assertions.assertEquals(Condition.IN,Condition.parse("_IN"));
        Assertions.assertEquals(Condition.NOT,Condition.parse("_NOT"));
        Assertions.assertEquals(Condition.OR,Condition.parse("_OR"));
        Assertions.assertEquals(Condition.LESSER_THAN,Condition.parse("_LESSER_THAN"));

    }
}