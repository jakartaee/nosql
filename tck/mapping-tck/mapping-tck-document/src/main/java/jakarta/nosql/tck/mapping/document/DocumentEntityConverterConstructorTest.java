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

import jakarta.nosql.TypeReference;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.tck.entities.Animal;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.constructor.BookUser;
import jakarta.nosql.tck.entities.constructor.Computer;
import jakarta.nosql.tck.entities.constructor.PetOwner;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
public class DocumentEntityConverterConstructorTest {

    @Inject
    private DocumentEntityConverter converter;

    @Test
    public void shouldConverterEntityComputer() {
        DocumentEntity communication = DocumentEntity.of("Computer");
        communication.add("_id", 10L);
        communication.add("name", "Dell");
        communication.add("age", 2020);
        communication.add("model", "Dell 2020");
        communication.add("price", "USD 20");
        Computer computer = this.converter.toEntity(communication);
        assertNotNull(computer);
        assertEquals(10L, computer.getId());
        assertEquals("Dell", computer.getName());
        assertEquals(2020, computer.getAge());
        assertEquals("Dell 2020", computer.getModel());
        assertEquals(Money.parse("USD 20"), computer.getPrice());
    }

    @Test
    public void shouldConvertComputerToCommunication() {
        Computer computer = new Computer(10L, "Dell", 2020, "Dell 2020",
                Money.parse("USD 20"));
        DocumentEntity communication = this.converter.toDocument(computer);
        assertNotNull(communication);

        assertEquals(computer.getId(), communication.find("_id", Long.class).get());
        assertEquals(computer.getName(), communication.find("name", String.class).get());
        assertEquals(computer.getAge(), communication.find("age", int.class).get());
        assertEquals(computer.getModel(), communication.find("model", String.class).get());
        assertEquals(computer.getPrice().toString(), communication.find("price", String.class).get());
    }

    @Test
    public void shouldConvertPetOwner() {
        DocumentEntity communication = DocumentEntity.of("PetOwner");
        communication.add("_id", 10L);
        communication.add("name", "Otavio");
        communication.add("animal", Arrays.asList(Document.of("_id", 23)
        , Document.of("name", "Ada")));

        PetOwner petOwner = this.converter.toEntity(communication);
        assertNotNull(petOwner);
        assertEquals(10L, petOwner.getId());
        assertEquals("Otavio", petOwner.getName());
        Animal animal = petOwner.getAnimal();
        assertEquals(23L, animal.getId());
        assertEquals("Ada", animal.getName());
    }

    @Test
    public void shouldConvertPetOwnerCommunication() {
        Animal ada = new Animal("Ada");
        PetOwner petOwner = new PetOwner(10L, "Poliana", ada);
        DocumentEntity communication = this.converter.toDocument(petOwner);
        assertNotNull(communication);
        assertEquals(10L, communication.find("_id", Long.class).get());
        assertEquals("Poliana", communication.find("name", String.class).get());
        List<Document> documents = communication.find("animal", new TypeReference<List<Document>>() {})
                .get();
        assertThat(documents).contains(Document.of("name", "Ada"));
    }

    @Test
    public void shouldConvertBookUser() {
        DocumentEntity communication = DocumentEntity.of("BookUser");
        communication.add("_id", "otaviojava");
        communication.add("native_name", "Otavio Santana");
        List<List<Document>> documents = new ArrayList<>();
        documents.add(Arrays.asList(Document.of("_id", 10), Document.of("name", "Effective Java")));
        documents.add(Arrays.asList(Document.of("_id", 12), Document.of("name", "Clean Code")));
        communication.add("books", documents);

        BookUser bookUser = this.converter.toEntity(communication);
        assertNotNull(bookUser);
        assertEquals("Otavio Santana", bookUser.getName());
        assertEquals("otaviojava", bookUser.getNickname());
        assertEquals(2, bookUser.getBooks().size());

    }
}
