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
package jakarta.nosql.tck.mapping.document;

import jakarta.nosql.Condition;
import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.document.DocumentRepositoryProducer;
import jakarta.nosql.mapping.document.DocumentTemplate;
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
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CDIExtension
class DocumentRepositoryPaginationTest {

    private DocumentTemplate template;

    @Inject
    private Converters converters;

    @Inject
    private DocumentRepositoryProducer producer;

    private PersonRepository personRepository;

    private VendorRepository vendorRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(DocumentTemplate.class);

        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.insert(any(Person.class), any(Duration.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());

        personRepository = producer.get(PersonRepository.class, template);
        vendorRepository = producer.get(VendorRepository.class, template);
    }




    @Test
    public void shouldFindByNameInstance() {

        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findByName("name", pagination);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

        assertEquals(Document.of("name", "name"), condition.getDocument());

        assertNotNull(personRepository.findByName("name", pagination));
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .empty());

        assertNull(personRepository.findByName("name", pagination));


    }

    @Test
    public void shouldFindByNameANDAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        List<Person> persons = personRepository.findByNameAndAge("name", 20, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);

        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByAgeANDName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        Set<Person> persons = personRepository.findByAgeAndName(20, "name", pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);
        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByNameANDAgeOrderByName() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();

        Stream<Person> persons = personRepository.findByNameAndAgeOrderByName("name", 20, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons.collect(Collectors.toList())).contains(ada);
        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByNameANDAgeOrderByAge() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        Queue<Person> persons = personRepository.findByNameAndAgeOrderByAge("name", 20, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        assertThat(persons).contains(ada);
        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());


    }


    @Test
    public void shouldFindAll() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();

        List<Person> persons = personRepository.findAll(pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertFalse(query.getCondition().isPresent());
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
    }


    @Test
    public void shouldFindByNameAndAgeGreaterEqualThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByNameAndAgeGreaterThanEqual("Ada", 33, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(AND, condition.getCondition());
        List<DocumentCondition> conditions = condition.getDocument().get(new TypeReference<>() {
        });
        DocumentCondition columnCondition = conditions.get(0);
        DocumentCondition columnCondition2 = conditions.get(1);

        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("Ada", columnCondition.getDocument().get());
        assertEquals("name", columnCondition.getDocument().getName());

        assertEquals(Condition.GREATER_EQUALS_THAN, columnCondition2.getCondition());
        assertEquals(33, columnCondition2.getDocument().get());
        assertEquals("age", columnCondition2.getDocument().getName());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
    }

    @Test
    public void shouldFindByGreaterThan() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAgeGreaterThan(33, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(GREATER_THAN, condition.getCondition());
        assertEquals(Document.of("age", 33), condition.getDocument());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByAgeLessThanEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAgeLessThanEqual(33, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals(Document.of("age", 33), condition.getDocument());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByAgeLessEqual() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAgeLessThan(33, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(LESSER_THAN, condition.getCondition());
        assertEquals(Document.of("age", 33), condition.getDocument());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByAgeBetween() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAgeBetween(10,15, pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(BETWEEN, condition.getCondition());
        List<Value> values = condition.getDocument().get(new TypeReference<>() {
        });
        assertEquals(Arrays.asList(10, 15), values.stream().map(Value::get).collect(Collectors.toList()));
        assertTrue(condition.getDocument().getName().contains("age"));
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
    }


    @Test
    public void shouldFindByNameLike() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByNameLike("Ada", pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(LIKE, condition.getCondition());
        assertEquals(Document.of("name", "Ada"), condition.getDocument());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }


    @Test
    public void shouldFindByStringWhenFieldIsSet() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(vendor));

        Pagination pagination = getPagination();
        vendorRepository.findByPrefixes("prefix", pagination);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("vendors", query.getDocumentCollection());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("prefixes", "prefix"), condition.getDocument());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }

    @Test
    public void shouldFindByIn() {
        Vendor vendor = new Vendor("vendor");
        vendor.setPrefixes(Collections.singleton("prefix"));

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(vendor));

        Pagination pagination = getPagination();
        vendorRepository.findByPrefixesIn(Collections.singletonList("prefix"), pagination);

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("vendors", query.getDocumentCollection());
        assertEquals(IN, condition.getCondition());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());

    }


    @Test
    public void shouldConvertFieldToTheType() {
        Person ada = Person.builder()
                .withAge(20).withName("Ada").build();

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(ada));

        Pagination pagination = getPagination();
        personRepository.findByAge("120", pagination);
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("age", 120), condition.getDocument());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
    }

    private Pagination getPagination() {
        return Pagination.page(current().nextLong(1, 10)).size(current().nextLong(1, 10));
    }
    interface PersonRepository extends Repository<Person, Long> {

        List<Person> findAll(Pagination pagination);

        Person findByName(String name, Pagination pagination);

        List<Person> findByAge(String age, Pagination pagination);

        List<Person> findByNameAndAge(String name, Integer age, Pagination pagination);

        Set<Person> findByAgeAndName(Integer age, String name, Pagination pagination);

        Stream<Person> findByNameAndAgeOrderByName(String name, Integer age, Pagination pagination);

        Queue<Person> findByNameAndAgeOrderByAge(String name, Integer age, Pagination pagination);

        Set<Person> findByNameAndAgeGreaterThanEqual(String name, Integer age, Pagination pagination);

        Set<Person> findByAgeGreaterThan(Integer age, Pagination pagination);

        Set<Person> findByAgeLessThanEqual(Integer age, Pagination pagination);

        Set<Person> findByAgeLessThan(Integer age, Pagination pagination);

        Set<Person> findByAgeBetween(Integer ageA, Integer ageB, Pagination pagination);

        Set<Person> findByNameLike(String name, Pagination pagination);

    }

    public interface VendorRepository extends Repository<Vendor, String> {

        Vendor findByPrefixes(String prefix, Pagination pagination);

        Vendor findByPrefixesIn(List<String> prefix, Pagination pagination);

    }
}
