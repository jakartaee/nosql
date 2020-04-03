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

import jakarta.nosql.Condition;
import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.DynamicQueryException;
import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.PreparedStatementAsync;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.RepositoryAsync;
import jakarta.nosql.mapping.column.ColumnRepositoryAsyncProducer;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CDIExtension
public class ColumnRepositoryAsyncTest {

    private ColumnTemplateAsync template;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    @Inject
    private ColumnRepositoryAsyncProducer producer;

    private PersonAsyncRepository personRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(ColumnTemplateAsync.class);
        this.personRepository = producer.get(PersonAsyncRepository.class, template);
    }


    @Test
    public void shouldSaveUsingInsertWhenThereIsNotData() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        personRepository.save(person);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L), consumerCaptor.capture());
        Consumer consumer = consumerCaptor.getValue();
        consumer.accept(Optional.empty());
        verify(template).insert(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
    }

    @Test
    public void shouldSaveUsingUpdateWhenThereIsData() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        personRepository.save(person);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L), consumerCaptor.capture());
        Consumer consumer = consumerCaptor.getValue();
        consumer.accept(Optional.of(Person.builder().build()));
        verify(template).update(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
    }


    @Test
    public void shouldSaveWithTTl() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        template.insert(person, Duration.ofHours(2));
        verify(template).insert(captor.capture(), eq(Duration.ofHours(2)));
        Person value = captor.getValue();
        assertEquals(person, value);
    }


    @Test
    public void shouldDeleteByName() {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        personRepository.deleteByName("name");
        verify(template).delete(captor.capture());
        ColumnDeleteQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "name"), condition.getColumn());

    }

    @Test
    public void shouldDeleteByNameCallBack() {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        Consumer<Void> voidConsumer = v -> {
        };
        personRepository.deleteByName("name", voidConsumer);
        verify(template).delete(captor.capture(), consumerCaptor.capture());
        ColumnDeleteQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "name"), condition.getColumn());
        assertEquals(voidConsumer, consumerCaptor.getValue());
    }


    @Test
    public void shouldReturnErrorOnFindByName() {
        Assertions.assertThrows(DynamicQueryException.class, () -> personRepository.findByName("name"));
    }

    @Test
    public void shouldFindByName() {
        Consumer<List<Person>> callback = v -> {
        };

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        personRepository.findByName("name", callback);
        verify(template).select(captor.capture(), consumerCaptor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "name"), condition.getColumn());
        assertEquals(callback, consumerCaptor.getValue());
    }

    @Test
    public void shouldFindByNameOrderByAgeDesc() {
        Consumer<List<Person>> callback = v -> {
        };

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        personRepository.findByNameOrderByAgeDesc("name", callback);
        verify(template).select(captor.capture(), consumerCaptor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "name"), condition.getColumn());
        assertEquals(callback, consumerCaptor.getValue());
        assertEquals(Sort.of("age", SortType.DESC), query.getSorts().get(0));

    }


    @Test
    public void shouldFindById() {
        Consumer<Optional<Person>> callBack = p -> {
        };
        personRepository.findById(10L, callBack);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L), eq(callBack));

    }


    @Test
    public void shouldDeleteById() {
        personRepository.deleteById(10L);
        verify(template).delete(Person.class, 10L);
    }


    @Test
    public void shouldExistsById() {
        Consumer<Boolean> callback = v -> {
        };
        personRepository.existsById(10L, callback);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L), any(Consumer.class));
    }

    @Test
    public void shouldReturnToString() {
        assertNotNull(personRepository.toString());
    }

    @Test
    public void shouldReturnHasCode() {
        assertNotNull(personRepository.hashCode());
        assertEquals(personRepository.hashCode(), personRepository.hashCode());
    }

    @Test
    public void shouldReturnEquals() {
        assertNotNull(personRepository.equals(personRepository));
    }

    @Test
    public void shouldExecuteJNoSQLQuery() {
        personRepository.findByQuery();
        verify(template).query(Mockito.eq("select * from Person"), Mockito.any(Consumer.class));
    }

    @Test
    public void shouldExecuteJNoSQLPrepare() {
        PreparedStatementAsync statement = Mockito.mock(PreparedStatementAsync.class);
        when(template.prepare(Mockito.anyString())).thenReturn(statement);
        personRepository.findByQuery("Ada", l ->{});
        verify(statement).bind("id", "Ada");
    }

    interface PersonAsyncRepository extends RepositoryAsync<Person, Long> {

        void deleteByName(String name);

        void deleteByName(String name, Consumer<Void> callback);

        void findByName(String name);

        void findByName(String name, Consumer<List<Person>> callBack);

        void findByNameOrderByAgeDesc(String name, Consumer<List<Person>> callBack);

        @Query("select * from Person")
        void findByQuery();

        @Query("select * from Person where id = @id")
        void findByQuery(@Param("id") String id, Consumer<List<Person>> callback);
    }

}