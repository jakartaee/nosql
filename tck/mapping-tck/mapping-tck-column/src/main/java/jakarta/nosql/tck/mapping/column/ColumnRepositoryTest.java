/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.column.ColumnRepositoryProducer;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Vendor;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jakarta.nosql.Condition.AND;
import static jakarta.nosql.Condition.BETWEEN;
import static jakarta.nosql.Condition.EQUALS;
import static jakarta.nosql.Condition.GREATER_THAN;
import static jakarta.nosql.Condition.IN;
import static jakarta.nosql.Condition.LESSER_EQUALS_THAN;
import static jakarta.nosql.Condition.LESSER_THAN;
import static jakarta.nosql.Condition.LIKE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CDIExtension
public class ColumnRepositoryTest {

    private ColumnTemplate template;

    @Inject
    private Converters converters;

    @Inject
    private ColumnRepositoryProducer producer;

    private PersonRepository personRepository;

    private VendorRepository vendorRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(ColumnTemplate.class);


        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.insert(any(Person.class), any(Duration.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());

        this.personRepository = producer.get(PersonRepository.class, template);
        this.vendorRepository = producer.get(VendorRepository.class, template);
    }


    @Test
    public void shouldSaveUsingInsertWhenDataDoesNotExist() {
        when(template.find(Person.class, 10L)).thenReturn(Optional.empty());

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        assertNotNull(personRepository.save(person));
        verify(template).insert(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
    }


    @Test
    public void shouldSaveUsingUpdateWhenDataExists() {

        when(template.find(Person.class, 10L)).thenReturn(Optional.of(Person.builder().build()));

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        assertNotNull(personRepository.save(person));
        verify(template).update(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
    }


    @Test
    public void shouldSaveIterable() {
        when(personRepository.findById(10L)).thenReturn(Optional.empty());

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();

        personRepository.save(singletonList(person));
        verify(template).insert(captor.capture());
        Person personCapture = captor.getValue();
        assertEquals(person, personCapture);
    }


    @Test
    public void shouldFindByNameInstance() {

        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        personRepository.findByName("name");

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "name"), condition.getColumn());

        assertNotNull(personRepository.findByName("name"));
        when(template.singleResult(any(ColumnQuery.class))).thenReturn(Optional
                .empty());

        assertNull(personRepository.findByName("name"));


    }

    @Test
    public void shouldFindByNameANDAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        List<Person> persons = personRepository.findByNameAndAge("name", 20);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);

    }

    @Test
    public void shouldFindByAgeANDName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Set<Person> persons = personRepository.findByAgeAndName(20, "name");
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);

    }

    @Test
    public void shouldFindByNameANDAgeOrderByName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Stream<Person> persons = personRepository.findByNameAndAgeOrderByName("name", 20);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons.collect(Collectors.toList())).contains(ada);

    }

    @Test
    public void shouldFindByNameANDAgeOrderByAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        Queue<Person> persons = personRepository.findByNameAndAgeOrderByAge("name", 20);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);

    }

    @Test
    public void shouldDeleteByName() {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        personRepository.deleteByName("Ada");
        verify(template).delete(captor.capture());
        ColumnDeleteQuery deleteQuery = captor.getValue();
        ColumnCondition condition = deleteQuery.getCondition().get();
        assertEquals("Person", deleteQuery.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Column.of("name", "Ada"), condition.getColumn());

    }

    @Test
    public void shouldFindById() {
        personRepository.findById(10L);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L));
    }

    @Test
    public void shouldFindByIds() {
        when(template.find(Mockito.eq(Person.class), Mockito.any(Long.class)))
                .thenReturn(Optional.of(Person.builder().build()));

        personRepository.findById(singletonList(10L));
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L));

        personRepository.findById(asList(1L, 2L, 3L));
        verify(template, times(4)).find(Mockito.eq(Person.class), Mockito.any(Long.class));
    }

    @Test
    public void shouldDeleteById() {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        personRepository.deleteById(10L);
        verify(template).delete(Person.class, 10L);
    }

    @Test
    public void shouldDeleteByIds() {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        personRepository.deleteById(singletonList(10L));
        verify(template).delete(Person.class, 10L);
    }


    @Test
    public void shouldContainsById() {
        when(template.find(Person.class, 10L)).thenReturn(Optional.of(Person.builder().build()));

        assertTrue(personRepository.existsById(10L));
        Mockito.verify(template).find(Person.class, 10L);

        when(template.find(Person.class, 10L)).thenReturn(Optional.empty());
        assertFalse(personRepository.existsById(10L));

    }

    @Test
    public void shouldFindAll() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        List<Person> persons = personRepository.findAll();
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertFalse(query.getCondition().isPresent());
        assertEquals("Person", query.getColumnFamily());

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
    public void shouldFindByNameAndAgeGreaterEqualThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        personRepository.findByNameAndAgeGreaterThanEqual("Ada", 33);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(AND, condition.getCondition());
        List<ColumnCondition> conditions = condition.getColumn().get(new TypeReference<>() {
        });
        ColumnCondition columnCondition = conditions.get(0);
        ColumnCondition columnCondition2 = conditions.get(1);

        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("Ada", columnCondition.getColumn().get());
        assertEquals("name", columnCondition.getColumn().getName());

        assertEquals(Condition.GREATER_EQUALS_THAN, columnCondition2.getCondition());
        assertEquals(33, columnCondition2.getColumn().get());
        assertEquals("age", columnCondition2.getColumn().getName());
    }

    @Test
    public void shouldFindByGreaterThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        personRepository.findByAgeGreaterThan(33);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(GREATER_THAN, condition.getCondition());
        assertEquals(Column.of("age", 33), condition.getColumn());

    }

    @Test
    public void shouldFindByAgeLessThanEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        personRepository.findByAgeLessThanEqual(33);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals(Column.of("age", 33), condition.getColumn());

    }

    @Test
    public void shouldFindByAgeLessEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        personRepository.findByAgeLessThan(33);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(LESSER_THAN, condition.getCondition());
        assertEquals(Column.of("age", 33), condition.getColumn());

    }

    @Test
    public void shouldFindByAgeBetween() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        personRepository.findByAgeBetween(10, 15);
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(BETWEEN, condition.getCondition());
        List<Value> values = condition.getColumn().get(new TypeReference<>() {
        });
        assertEquals(Arrays.asList(10, 15), values.stream().map(Value::get).collect(Collectors.toList()));
        assertTrue(condition.getColumn().getName().contains("age"));
    }


    @Test
    public void shouldFindByNameLike() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        personRepository.findByNameLike("Ada");
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(LIKE, condition.getCondition());
        assertEquals(Column.of("name", "Ada"), condition.getColumn());

    }


    @Test
    public void shouldFindByStringWhenFieldIsSet() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(vendor));

        vendorRepository.findByPrefixes("prefix");

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("vendors", query.getColumnFamily());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Column.of("prefixes", "prefix"), condition.getColumn());

    }

    @Test
    public void shouldFindByIn() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(vendor));

        vendorRepository.findByPrefixesIn(singletonList("prefix"));

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).singleResult(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("vendors", query.getColumnFamily());
        assertEquals(IN, condition.getCondition());

    }


    @Test
    public void shouldConvertFieldToTheType() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(ada));

        personRepository.findByAge("120");
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(template).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnCondition condition = query.getCondition().get();
        assertEquals("Person", query.getColumnFamily());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Column.of("age", 120), condition.getColumn());
    }


    @Test
    public void shouldExecuteJNoSQLQuery() {
        personRepository.findByQuery();
        verify(template).query("select * from Person");
    }

    @Test
    public void shouldExecuteJNoSQLPrepare() {
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        when(template.prepare(Mockito.anyString())).thenReturn(statement);
        personRepository.findByQuery("Ada");
        verify(statement).bind("id", "Ada");
    }

    interface PersonRepository extends Repository<Person, Long> {

        List<Person> findAll();

        Person findByName(String name);


        void deleteByName(String name);

        List<Person> findByAge(String age);

        List<Person> findByNameAndAge(String name, Integer age);

        Set<Person> findByAgeAndName(Integer age, String name);

        Stream<Person> findByNameAndAgeOrderByName(String name, Integer age);

        Queue<Person> findByNameAndAgeOrderByAge(String name, Integer age);

        Set<Person> findByNameAndAgeGreaterThanEqual(String name, Integer age);

        Set<Person> findByAgeGreaterThan(Integer age);

        Set<Person> findByAgeLessThanEqual(Integer age);

        Set<Person> findByAgeLessThan(Integer age);

        Set<Person> findByAgeBetween(Integer ageA, Integer ageB);

        Set<Person> findByNameLike(String name);

        @Query("select * from Person")
        Optional<Person> findByQuery();

        @Query("select * from Person where id = @id")
        Optional<Person> findByQuery(@Param("id") String id);
    }

    public interface VendorRepository extends Repository<Vendor, String> {

        Vendor findByPrefixes(String prefix);

        Vendor findByPrefixesIn(List<String> prefix);

    }
}