/*
 *  Copyright (c) 2022 Otavio Santana and others
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v. 2.0 which is available at
 *  http://www.eclipse.org/legal/epl-2.0.
 *
 *  This Source Code may also be made available under the following Secondary
 *  Licenses when the conditions for such availability set forth in the Eclipse
 *  Public License v. 2.0 are satisfied: GNU General Public License v2.0
 *  w/Classpath exception which is available at
 *  https://www.gnu.org/software/classpath/license.html.
 *
 *  SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.communication.column;

import jakarta.nosql.Condition;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

public class ColumnConditionTest {

    @Test
    public void shouldReturnNPEInEqWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.eq(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.eq("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.eq(null, "value"));
    }

    @Test
    public void shouldCreateEqFromDocument() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.eq(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.EQUALS, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldCreateEqFromNameValue() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.eq("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.EQUALS, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldReturnNPEInGtWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.gt(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.gt("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.gt(null, "value"));
    }

    @Test
    public void shouldCreateGtFromDocument() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.gt(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.GREATER_THAN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldCreateGtFromNameValue() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.gt("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.GREATER_THAN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldReturnNPEInGetWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.gte(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.gte("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.gte(null, "value"));
    }

    @Test
    public void shouldCreateGteFromDocument() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.gte(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldCreateGetFromNameValue() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.gte("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldReturnNPEInLtWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.lt(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.lt("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.lt(null, "value"));
    }

    @Test
    public void shouldCreateLtFromDocument() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.lt(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LESSER_THAN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldCreateLtFromNameValue() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.lt("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LESSER_THAN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldReturnNPEInLteWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.lte(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.lte("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.eq(null, "value"));
    }

    @Test
    public void shouldCreateLteFromDocument() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.lte(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldCreateLteFromNameValue() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.lte("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldReturnNPEInInWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.in(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.in("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.in(null, "value"));
    }

    @Test
    public void shouldCreateInFromDocument() {
        Column column = Column.of("name", Collections.singleton("Ada Lovelace"));
        ColumnCondition condition = ColumnCondition.in(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.IN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldCreateInFromNameValue() {
        Column column = Column.of("name", Collections.singleton("Ada Lovelace"));
        ColumnCondition condition = ColumnCondition.in("name", Collections.singleton("Ada Lovelace"));
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.IN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldReturnNPELikeInWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.like(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.like("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.like(null, "value"));
    }

    @Test
    public void shouldCreateLikeFromDocument() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.like(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LIKE, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldCreateLikeFromNameValue() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.like("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LIKE, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldReturnNPEBetweenInWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.between(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.between("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ColumnCondition.between(null, "value"));
    }

    @Test
    public void shouldCreateBetweenFromDocument() {
        Column column = Column.of("age", Arrays.asList(10, 20));
        ColumnCondition condition = ColumnCondition.between(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.BETWEEN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldCreateBetweenFromNameValue() {
        Column column = Column.of("age", Arrays.asList(10, 20));
        ColumnCondition condition = ColumnCondition.between(column);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.BETWEEN, condition.getCondition());
        Assertions.assertEquals(column, condition.getColumn());
    }

    @Test
    public void shouldNegate() {
        Column column = Column.of("name", "Ada Lovelace");
        ColumnCondition condition = ColumnCondition.eq(column).negate();
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.NOT, condition.getCondition());
    }

}
