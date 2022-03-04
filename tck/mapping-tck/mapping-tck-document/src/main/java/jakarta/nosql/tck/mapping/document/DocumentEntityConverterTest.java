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

import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.tck.entities.Actor;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.AppointmentBook;
import jakarta.nosql.tck.entities.Contact;
import jakarta.nosql.tck.entities.ContactType;
import jakarta.nosql.tck.entities.Director;
import jakarta.nosql.tck.entities.Download;
import jakarta.nosql.tck.entities.Job;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.Movie;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Vendor;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.entities.ZipCode;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CDIExtension
public class DocumentEntityConverterTest {

    @Inject
    private DocumentEntityConverter converter;

    private Document[] documents;

    private Actor actor = Actor.actorBuilder().withAge()
            .withId()
            .withName()
            .withPhones(Arrays.asList("234", "2342"))
            .withMovieCharacter(Collections.singletonMap("JavaZone", "Jedi"))
            .withMovieRating(Collections.singletonMap("JavaZone", 10))
            .build();

    @BeforeEach
    public void init() {

        documents = new Document[]{Document.of("_id", 12L),
                Document.of("age", 10), Document.of("name", "Otavio"), Document.of("phones", Arrays.asList("234", "2342"))
                , Document.of("movieCharacter", Collections.singletonMap("JavaZone", "Jedi"))
                , Document.of("movieRating", Collections.singletonMap("JavaZone", 10))};
    }

    @Test
    public void shouldConvertDocumentEntityFromEntity() {

        Person person = Person.builder().withAge()
                .withId(12)
                .withName("Otavio")
                .withPhones(Arrays.asList("234", "2342")).build();

        DocumentEntity entity = converter.toDocument(person);
        assertEquals("Person", entity.getName());
        assertEquals(4, entity.size());
        assertThat(entity.getDocuments(), containsInAnyOrder(Document.of("_id", 12L),
                Document.of("age", 10), Document.of("name", "Otavio"), Document.of("phones", Arrays.asList("234", "2342"))));

    }

    @Test
    public void shouldConvertEntityFromDocumentEntity() {


        DocumentEntity entity = converter.toDocument(actor);
        assertEquals("Actor", entity.getName());
        assertEquals(6, entity.size());


        assertThat(entity.getDocuments(), containsInAnyOrder(documents));
    }

    @Test
    public void shouldConvertDocumentEntityFromEntity2() {
        DocumentEntity entity = DocumentEntity.of("Actor");
        Stream.of(documents).forEach(entity::add);

        Actor actor = converter.toEntity(Actor.class, entity);
        assertNotNull(actor);
        assertEquals(10, actor.getAge());
        assertEquals(12L, actor.getId());
        assertEquals(Arrays.asList("234", "2342"), actor.getPhones());
        assertEquals(Collections.singletonMap("JavaZone", "Jedi"), actor.getMovieCharacter());
        assertEquals(Collections.singletonMap("JavaZone", 10), actor.getMovieRating());
    }


    @Test
    public void shouldConvertColumnEntityToExistEntity() {
        DocumentEntity entity = DocumentEntity.of("Actor");
        Stream.of(documents).forEach(entity::add);
        Actor actor = Actor.actorBuilder().build();
        Actor result = converter.toEntity(actor, entity);

        assertSame(actor, result);
        assertEquals(10, actor.getAge());
        assertEquals(12L, actor.getId());
        assertEquals(asList("234", "2342"), actor.getPhones());
        assertEquals(Collections.singletonMap("JavaZone", "Jedi"), actor.getMovieCharacter());
        assertEquals(Collections.singletonMap("JavaZone", 10), actor.getMovieRating());
    }

    @Test
    public void shouldReturnErrorWhenToEntityIsNull() {
        DocumentEntity entity = DocumentEntity.of("Actor");
        Stream.of(documents).forEach(entity::add);
        Actor actor = Actor.actorBuilder().build();

        assertThrows(NullPointerException.class, () -> converter.toEntity(null, entity));

        assertThrows(NullPointerException.class, () -> converter.toEntity(actor, null));
    }

    @Test
    public void shouldConvertDocumentEntityFromEntity3() {
        DocumentEntity entity = DocumentEntity.of("Actor");
        Stream.of(documents).forEach(entity::add);

        Actor actor = converter.toEntity(entity);
        assertNotNull(actor);
        assertEquals(10, actor.getAge());
        assertEquals(12L, actor.getId());
        assertEquals(Arrays.asList("234", "2342"), actor.getPhones());
        assertEquals(Collections.singletonMap("JavaZone", "Jedi"), actor.getMovieCharacter());
        assertEquals(Collections.singletonMap("JavaZone", 10), actor.getMovieRating());
    }

    @Test
    public void shouldConvertEntityFromDocumentEntity2() {

        Movie movie = new Movie("Matrix", 2012, singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(Arrays.asList("234", "2342")).withMovie(movie).build();

        DocumentEntity entity = converter.toDocument(director);
        assertEquals(5, entity.size());

        assertEquals(getValue(entity.find("name")), director.getName());
        assertEquals(getValue(entity.find("age")), director.getAge());
        assertEquals(getValue(entity.find("_id")), director.getId());
        assertEquals(getValue(entity.find("phones")), director.getPhones());

        Document subdocument = entity.find("movie").get();
        List<Document> documents = subdocument.get(new TypeReference<List<Document>>() {
        });
        assertEquals(3, documents.size());
        assertEquals("movie", subdocument.getName());

        assertEquals(movie.getTitle(), getValue(documents.stream().filter(d -> "title".equals(d.getName())).findFirst()));
        assertEquals(movie.getYear(), getValue(documents.stream().filter(d -> "year".equals(d.getName())).findFirst()));
        assertEquals(movie.getActors(), getValue(documents.stream().filter(d -> "actors".equals(d.getName())).findFirst()));


    }


    @Test
    public void shouldConvertToEmbeddedClassWhenHasSubDocument() {
        Movie movie = new Movie("Matrix", 2012, singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(Arrays.asList("234", "2342")).withMovie(movie).build();

        DocumentEntity entity = converter.toDocument(director);
        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }


    @Test
    public void shouldConvertToEmbeddedClassWhenHasSubDocument2() {
        Movie movie = new Movie("Matrix", 2012, singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(Arrays.asList("234", "2342")).withMovie(movie).build();

        DocumentEntity entity = converter.toDocument(director);
        entity.remove("movie");
        entity.add(Document.of("movie", Arrays.asList(Document.of("title", "Matrix"),
                Document.of("year", 2012), Document.of("actors", singleton("Actor")))));

        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }


    @Test
    public void shouldConvertToEmbeddedClassWhenHasSubDocument3() {
        Movie movie = new Movie("Matrix", 2012, singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(Arrays.asList("234", "2342")).withMovie(movie).build();

        DocumentEntity entity = converter.toDocument(director);
        entity.remove("movie");

        Map<String, Object> map = new HashMap<>();
        map.put("title", "Matrix");
        map.put("year", 2012);
        map.put("actors", singleton("Actor"));

        entity.add(Document.of("movie", map));
        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }

    @Test
    public void shouldConvertToDocumentWhenHaConverter() {
        Worker worker = new Worker();
        Job job = new Job();
        job.setCity("Sao Paulo");
        job.setDescription("Java Developer");
        worker.setName("Bob");
        worker.setSalary(new Money("BRL", BigDecimal.TEN));
        worker.setJob(job);
        DocumentEntity entity = converter.toDocument(worker);
        assertEquals("Worker", entity.getName());
        assertEquals("Bob", entity.find("name").get().get());
        assertEquals("Sao Paulo", entity.find("city").get().get());
        assertEquals("Java Developer", entity.find("description").get().get());
        assertEquals("BRL 10", entity.find("money").get().get());
    }

    @Test
    public void shouldConvertToEntityWhenHasConverter() {
        Worker worker = new Worker();
        Job job = new Job();
        job.setCity("Sao Paulo");
        job.setDescription("Java Developer");
        worker.setName("Bob");
        worker.setSalary(new Money("BRL", BigDecimal.TEN));
        worker.setJob(job);
        DocumentEntity entity = converter.toDocument(worker);
        Worker worker1 = converter.toEntity(entity);
        assertEquals(worker.getSalary(), worker1.getSalary());
        assertEquals(job.getCity(), worker1.getJob().getCity());
        assertEquals(job.getDescription(), worker1.getJob().getDescription());
    }

    @Test
    public void shouldConvertToListEmbeddable() {
        AppointmentBook appointmentBook = new AppointmentBook("ids");
        appointmentBook.add(Contact.builder().withType(ContactType.EMAIL).withName("Ada").withInformation("ada@lovelace.com").build());
        appointmentBook.add(Contact.builder().withType(ContactType.MOBILE).withName("Ada").withInformation("11 1231231 123").build());
        appointmentBook.add(Contact.builder().withType(ContactType.PHONE).withName("Ada").withInformation("12 123 1231 123123").build());

        DocumentEntity entity = converter.toDocument(appointmentBook);
        Document contacts = entity.find("contacts").get();
        assertEquals("ids", appointmentBook.getId());
        List<List<Document>> documents = (List<List<Document>>) contacts.get();

        assertEquals(3L, documents.stream().flatMap(Collection::stream)
                .filter(c -> c.getName().equals("contact_name"))
                .count());
    }

    @Test
    public void shouldConvertFromListEmbeddable() {
        DocumentEntity entity = DocumentEntity.of("AppointmentBook");
        entity.add(Document.of("_id", "ids"));
        List<List<Document>> documents = new ArrayList<>();

        documents.add(asList(Document.of("contact_name", "Ada"), Document.of("type", ContactType.EMAIL),
                Document.of("information", "ada@lovelace.com")));

        documents.add(asList(Document.of("contact_name", "Ada"), Document.of("type", ContactType.MOBILE),
                Document.of("information", "11 1231231 123")));

        documents.add(asList(Document.of("contact_name", "Ada"), Document.of("type", ContactType.PHONE),
                Document.of("information", "phone")));

        entity.add(Document.of("contacts", documents));

        AppointmentBook appointmentBook = converter.toEntity(entity);

        List<Contact> contacts = appointmentBook.getContacts();
        assertEquals("ids", appointmentBook.getId());
        assertEquals("Ada", contacts.stream().map(Contact::getName).distinct().findFirst().get());

    }


    @Test
    public void shouldConvertSubEntity() {
        ZipCode zipcode = new ZipCode();
        zipcode.setZip("12321");
        zipcode.setPlusFour("1234");

        Address address = new Address();
        address.setCity("Salvador");
        address.setState("Bahia");
        address.setStreet("Rua Engenheiro Jose Anasoh");
        address.setZipCode(zipcode);

        DocumentEntity documentEntity = converter.toDocument(address);
        List<Document> documents = documentEntity.getDocuments();
        assertEquals("Address", documentEntity.getName());
        assertEquals(4, documents.size());
        List<Document> zip = documentEntity.find("zipCode").map(d -> d.get(new TypeReference<List<Document>>() {
        })).orElse(Collections.emptyList());
        assertEquals("Rua Engenheiro Jose Anasoh", getValue(documentEntity.find("street")));
        assertEquals("Salvador", getValue(documentEntity.find("city")));
        assertEquals("Bahia", getValue(documentEntity.find("state")));
        assertEquals("12321", getValue(zip.stream().filter(d -> d.getName().equals("zip")).findFirst()));
        assertEquals("1234", getValue(zip.stream().filter(d -> d.getName().equals("plusFour")).findFirst()));
    }

    @Test
    public void shouldConvertDocumentInSubEntity() {

        DocumentEntity entity = DocumentEntity.of("Address");

        entity.add(Document.of("street", "Rua Engenheiro Jose Anasoh"));
        entity.add(Document.of("city", "Salvador"));
        entity.add(Document.of("state", "Bahia"));
        entity.add(Document.of("zipCode", Arrays.asList(
                Document.of("zip", "12321"),
                Document.of("plusFour", "1234"))));

        Address address = converter.toEntity(entity);

        assertEquals("Rua Engenheiro Jose Anasoh", address.getStreet());
        assertEquals("Salvador", address.getCity());
        assertEquals("Bahia", address.getState());
        assertEquals("12321", address.getZipCode().getZip());
        assertEquals("1234",  address.getZipCode().getPlusFour());

    }

    @Test
    public void shouldReturnNullWhenThereIsNotSubEntity() {

        DocumentEntity entity = DocumentEntity.of("Address");

        entity.add(Document.of("street", "Rua Engenheiro Jose Anasoh"));
        entity.add(Document.of("city", "Salvador"));
        entity.add(Document.of("state", "Bahia"));
        entity.add(Document.of("zip", "12321"));
        entity.add(Document.of("plusFour", "1234"));

        Address address = converter.toEntity(entity);

        assertEquals("Rua Engenheiro Jose Anasoh", address.getStreet());
        assertEquals("Salvador", address.getCity());
        assertEquals("Bahia", address.getState());
        assertNull(address.getZipCode());

    }


    @Test
    public void shouldConvertAndDoNotUseUnmodifiableCollection() {
        DocumentEntity entity = DocumentEntity.of("vendors");
        entity.add("name", "name");
        entity.add("prefixes", Arrays.asList("value", "value2"));

        Vendor vendor = converter.toEntity(entity);
        vendor.add("value3");

        Assertions.assertEquals(3, vendor.getPrefixes().size());

    }

    @Test
    public void shouldConvertEntityToDocumentWithArray() {
        byte[] contents = {1, 2, 3, 4, 5, 6};

        DocumentEntity entity = DocumentEntity.of("download");
        entity.add("_id", 1L);
        entity.add("contents", contents);

        Download download = converter.toEntity(entity);
        Assertions.assertEquals(1L, download.getId());
        Assertions.assertArrayEquals(contents, download.getContents());
    }

    @Test
    public void shouldConvertDocumentToEntityWithArray() {
        byte[] contents = {1, 2, 3, 4, 5, 6};

        Download download = new Download();
        download.setId(1L);
        download.setContents(contents);

        DocumentEntity entity = converter.toDocument(download);


        Assertions.assertEquals(1L, entity.find("_id").get().get());
        final byte[] bytes = entity.find("contents").map(v -> v.get(byte[].class)).orElse(new byte[0]);
        Assertions.assertArrayEquals(contents, bytes);
    }

    private Object getValue(Optional<Document> document) {
        return document.map(Document::getValue).map(Value::get).orElse(null);
    }

}