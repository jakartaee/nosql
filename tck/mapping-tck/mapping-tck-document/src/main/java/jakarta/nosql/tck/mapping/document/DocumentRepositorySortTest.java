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
package jakarta.nosql.tck.mapping.document;

import jakarta.nosql.Condition;
import jakarta.nosql.Sort;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.Sorts;
import jakarta.nosql.mapping.document.DocumentRepositoryProducer;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static jakarta.nosql.Condition.AND;
import static jakarta.nosql.Condition.EQUALS;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CDIExtension
class DocumentRepositorySortTest {

    private DocumentTemplate template;

    @Inject
    private Converters converters;

    @Inject
    private DocumentRepositoryProducer producer;

    private PersonRepository personRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(DocumentTemplate.class);

        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.insert(any(Person.class), any(Duration.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());

        this.personRepository = producer.get(PersonRepository.class, template);
    }

    @Test
    public void shouldFindAll() {

        when(template.select(any(DocumentQuery.class))).thenReturn(Stream.of(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findAll(pagination, Sorts.sorts().asc("name"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.asc("name")));

    }

    @Test
    public void shouldFindByName() {

        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .of(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findByName("name", pagination, Sort.desc("name"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).singleResult(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.desc("name")));
        assertEquals(Document.of("name", "name"), condition.getDocument());

        assertNotNull(personRepository.findByName("name", pagination, Sort.asc("name")));
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .empty());

        assertNull(personRepository.findByName("name", pagination, Sort.asc("name")));

    }

    @Test
    public void shouldFindByAge() {

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(Person.builder().build()));

        personRepository.findByAge(10, Sort.desc("name"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(0, query.getSkip());
        assertEquals(0, query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.desc("name")));
        assertEquals(Document.of("age", 10), condition.getDocument());

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(Person.builder().build()));
        assertNotNull(personRepository.findByAge(10, Sort.asc("name")));
        when(template.singleResult(any(DocumentQuery.class))).thenReturn(Optional
                .empty());

    }

    @Test
    public void shouldFindByNameAndAge() {

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(Person.builder().build()));

        personRepository.findByNameAndAge("name", 10, Sorts.sorts().desc("name"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(AND, condition.getCondition());
        assertEquals(0, query.getSkip());
        assertEquals(0, query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.desc("name")));

    }


    @Test
    public void shouldFindByNameOrderByName() {

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findByNameOrderByName("name", pagination, Sort.desc("age"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "name"), condition.getDocument());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.asc("name"), Sort.desc("age")));

    }

    @Test
    public void shouldFindByNameOrderByName2() {

        when(template.select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findByNameOrderByName("name", pagination, Sorts.sorts().desc("age").asc("phone"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "name"), condition.getDocument());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        assertThat(query.getSorts(), Matchers.contains(Sort.asc("name"), Sort.desc("age"), Sort.asc("phone")));

    }

    private Pagination getPagination() {
        return Pagination.page(current().nextLong(1, 10)).size(current().nextLong(1, 10));
    }

    interface PersonRepository extends Repository<Person, Long> {

        List<Person> findAll(Pagination pagination, Sorts sorts);

        Person findByName(String name, Pagination pagination, Sort sort);

        List<Person> findByAge(Integer age, Sort sort);

        List<Person> findByNameAndAge(String name, Integer age, Sorts sorts);

        List<Person> findByNameOrderByName(String name, Pagination pagination, Sort sort);

        List<Person> findByNameOrderByName(String name, Pagination pagination, Sorts sorts);


    }

}
