/*
 * Copyright (c) 2020 Otavio Santana and others
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.mapping.column;

import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.mapping.column.ColumnQueryMapper;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;

import static jakarta.nosql.column.ColumnDeleteQuery.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CDIExtension
public class ColumnMapperDeleteBuilderTest {

    @Inject
    private ColumnQueryMapper mapperBuilder;


    @Test
    public void shouldReturnDeleteFrom() {
        ColumnQueryMapper.ColumnMapperDeleteFrom columnFrom = mapperBuilder.deleteFrom(Person.class);
        ColumnDeleteQuery query = columnFrom.build();
        ColumnDeleteQuery queryExpected = delete().from("Person").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldDeleteWhereNameEq() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").eq("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("name").eq("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameLike() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").like("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("name").like("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameGt() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").gt(10).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").gt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameGte() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").gte(10).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").gte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameLt() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").lt(10).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").lt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameLte() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").lte(10).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").lte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameBetween() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id")
                .between(10, 20).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id")
                .between(10L, 20L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameIn() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name")
                .in(Arrays.asList("Ada", "Poliana")).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("name")
                .in(Arrays.asList("Ada", "Poliana")).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameNot() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").not().like("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("name").not().like("Ada").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldDeleteWhereNameAnd() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("age").between(10, 20)
                .and("name").eq("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("age")
                .between(10, 20)
                .and("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNameOr() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").between(10, 20)
                .or("name").eq("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id")
                .between(10L, 20L)
                .or("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldConvertField() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").eq("20")
                .build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").eq(20L)
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldUseAttributeConverter() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Worker.class).where("salary")
                .eq(new Money("USD", BigDecimal.TEN)).build();
        ColumnDeleteQuery queryExpected = delete().from("Worker").where("money")
                .eq("USD 10").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryByEmbeddable() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Worker.class).where("job.city").eq("Salvador")
                .build();
        ColumnDeleteQuery queryExpected = delete().from("Worker").where("city").eq("Salvador")
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryBySubEntity() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Address.class).where("zipCode.zip").eq("01312321")
                .build();
        ColumnDeleteQuery queryExpected = delete().from("Address").where("zipCode.zip").eq("01312321")
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldExecuteDeleteFrom() {
        ColumnTemplate template = Mockito.mock(ColumnTemplate.class);
        ArgumentCaptor<ColumnDeleteQuery> queryCaptor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        mapperBuilder.deleteFrom(Person.class).delete(template);
        Mockito.verify(template).delete(queryCaptor.capture());
        ColumnDeleteQuery query = queryCaptor.getValue();
        ColumnDeleteQuery queryExpected = delete().from("Person").build();
        assertEquals(queryExpected, query);
    }
}