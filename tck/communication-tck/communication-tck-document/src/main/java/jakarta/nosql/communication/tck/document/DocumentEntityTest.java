/*
 *  Copyright (c) 2020 Otavio Santana and others
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

package jakarta.nosql.communication.tck.document;

import jakarta.nosql.TypeReference;
import jakarta.nosql.TypeSupplier;
import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DocumentEntityTest {

    @Test
    public void shouldReturnErrorWhenNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DocumentEntity.of(null));
    }

    @Test
    public void shouldReturnErrorWhenDocumentsIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DocumentEntity.of("entity", null));
    }

    @Test
    public void shouldReturnOneDocument() {
        DocumentEntity entity = DocumentEntity.of("entity");
        assertEquals(Integer.valueOf(0), Integer.valueOf(entity.size()));
        assertTrue(entity.isEmpty());

        entity.add(Document.of("name", "name"));
        entity.add(Document.of("name2", Value.of("name2")));
        assertFalse(entity.isEmpty());
        assertEquals(Integer.valueOf(2), Integer.valueOf(entity.size()));
        assertFalse(DocumentEntity.of("entity", singletonList(Document.of("name", "name"))).isEmpty());
    }

    @Test
    public void shouldDoCopy() {
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(Document.of("name", "name")));
        DocumentEntity copy = entity.copy();
        assertFalse(entity == copy);
        assertEquals(entity, copy);

    }

    @Test
    public void shouldReturnErrorWhenFindHasNullParameter() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Assertions.assertThrows(NullPointerException.class, () -> entity.find(null));
        Assertions.assertThrows(NullPointerException.class, () -> entity.find("name", (Class<Object>) null));
        Assertions.assertThrows(NullPointerException.class, () -> entity.find("name", (TypeSupplier<Object>) null));
    }

    @Test
    public void shouldFindDocument() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Optional<Document> name = entity.find("name");
        Optional<Document> notfound = entity.find("not_found");
        assertTrue(name.isPresent());
        assertFalse(notfound.isPresent());
        assertEquals(document, name.get());
    }


    @Test
    public void shouldReturnErrorWhenFindDocumentIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Document document = Document.of("name", "name");
            DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
            entity.find(null);
        });
    }

    @Test
    public void shouldFindValue() {
        Document column = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(column));
        Optional<String> name = entity.find("name", String.class);
        Assertions.assertNotNull(name);
        Assertions.assertTrue(name.isPresent());
        Assertions.assertEquals("name", name.orElse(""));
    }

    @Test
    public void shouldNotFindValue() {
        Document column = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(column));
        Optional<String> notFound = entity.find("not_found", String.class);
        Assertions.assertNotNull(notFound);
        Assertions.assertFalse(notFound.isPresent());
    }

    @Test
    public void shouldFindTypeSupplier() {
        Document column = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(column));
        List<String> names = entity.find("name", new TypeReference<List<String>>() {})
                .orElse(Collections.emptyList());
        Assertions.assertNotNull(names);
        Assertions.assertFalse(names.isEmpty());
    }

    @Test
    public void shouldNotFindTypeSupplier() {
        Document column = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(column));
        List<String> names = entity.find("not_found", new TypeReference<List<String>>() {})
                .orElse(Collections.emptyList());
        Assertions.assertNotNull(names);
        Assertions.assertTrue(names.isEmpty());
    }

    @Test
    public void shouldRemoveDocumentByName() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        assertTrue(entity.remove("name"));
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldConvertToMap() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        assertEquals(document.getName(), result.keySet().stream().findAny().get());

    }

    @Test
    public void shouldConvertSubColumnToMap() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(Document.of("sub", document)));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        Map<String, Object> map = (Map<String, Object>) result.get("sub");
        assertEquals("name", map.get("name"));
    }

    @Test
    public void shouldConvertSubDocumentListToMap() {
        DocumentEntity entity = DocumentEntity.of("entity");
        entity.add(Document.of("_id", "id"));
        List<Document> documents = asList(Document.of("name", "Ada"), Document.of("type", "type"),
                Document.of("information", "ada@lovelace.com"));

        entity.add(Document.of("contacts", documents));
        Map<String, Object> result = entity.toMap();
        assertEquals("id", result.get("_id"));
        List<Map<String, Object>> contacts = (List<Map<String, Object>>) result.get("contacts");
        assertEquals(3, contacts.size());
        assertThat(contacts, containsInAnyOrder(singletonMap("name", "Ada"), singletonMap("type", "type"),
                singletonMap("information", "ada@lovelace.com")));

    }

    @Test
    public void shouldConvertSubDocumentListToMap2() {
        DocumentEntity entity = DocumentEntity.of("entity");
        entity.add(Document.of("_id", "id"));
        List<List<Document>> documents = new ArrayList<>();
        documents.add(asList(Document.of("name", "Ada"), Document.of("type", "type"),
                Document.of("information", "ada@lovelace.com")));

        entity.add(Document.of("contacts", documents));
        Map<String, Object> result = entity.toMap();
        assertEquals("id", result.get("_id"));
        List<List<Map<String, Object>>> contacts = (List<List<Map<String, Object>>>) result.get("contacts");
        assertEquals(1, contacts.size());
        List<Map<String, Object>> maps = contacts.get(0);
        assertEquals(3, maps.size());
        assertThat(maps, containsInAnyOrder(singletonMap("name", "Ada"), singletonMap("type", "type"),
                singletonMap("information", "ada@lovelace.com")));

    }

    @Test
    public void shouldShouldCreateANewInstance() {
        String name = "name";
        DocumentEntity entity = DocumentEntity.of(name);
        assertEquals(name, entity.getName());
    }

    @Test
    public void shouldCreateAnEmptyEntity() {
        DocumentEntity entity = DocumentEntity.of("name");
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldReturnAnErrorWhenAddANullDocument() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = DocumentEntity.of("name");
            entity.add(null);
        });
    }

    @Test
    public void shouldAddANewDocument() {
        DocumentEntity entity = DocumentEntity.of("name");
        entity.add(Document.of("document", 12));
        assertFalse(entity.isEmpty());
        assertEquals(1, entity.size());
    }

    @Test
    public void shouldReturnErrorWhenAddAnNullIterable() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = DocumentEntity.of("name");
            entity.addAll(null);
        });
    }

    @Test
    public void shouldAddAllDocuments() {
        DocumentEntity entity = DocumentEntity.of("name");
        entity.addAll(asList(Document.of("name", 12), Document.of("value", "value")));
        assertFalse(entity.isEmpty());
        assertEquals(2, entity.size());
    }


    @Test
    public void shouldNotFindDocument() {
        DocumentEntity entity = DocumentEntity.of("name");
        Optional<Document> document = entity.find("name");
        assertFalse(document.isPresent());
    }

    @Test
    public void shouldRemoveByName() {
        DocumentEntity entity = DocumentEntity.of("name");
        entity.add(Document.of("value", 32D));
        assertTrue(entity.remove("value"));
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldNotRemoveByName() {
        DocumentEntity entity = DocumentEntity.of("name");
        entity.add(Document.of("value", 32D));

        assertFalse(entity.remove("value1"));
        assertFalse(entity.isEmpty());
    }

    @Test
    public void shouldReturnErrorWhenRemoveByNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = DocumentEntity.of("name");
            entity.remove(null);
        });
    }


    @Test
    public void shouldAddDocumentAsNameAndObject() {
        DocumentEntity entity = DocumentEntity.of("documentCollection");
        entity.add("name", 10);
        assertEquals(1, entity.size());
        Optional<Document> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    public void shouldAddDocumentAsNameAndValue() {
        DocumentEntity entity = DocumentEntity.of("documentCollection");
        entity.add("name", Value.of(10));
        assertEquals(1, entity.size());
        Optional<Document> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    public void shouldReturnErrorWhenAddDocumentasObjectWhenHasNullObject() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = DocumentEntity.of("documentCollection");
            entity.add("name", null);
        });
    }

    @Test
    public void shouldReturnErrorWhenAddDocumentasObjectWhenHasNullDocumentName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = DocumentEntity.of("documentCollection");
            entity.add(null, 10);
        });
    }

    @Test
    public void shouldReturnErrorWhenAddDocumentasValueWhenHasNullDocumentName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = DocumentEntity.of("documentCollection");
            entity.add(null, Value.of(12));
        });
    }


    @Test
    public void shouldAvoidDuplicatedDocument() {
        DocumentEntity entity = DocumentEntity.of("documentCollection");
        entity.add("name", 10);
        entity.add("name", 13);
        assertEquals(1, entity.size());
        Optional<Document> document = entity.find("name");
        assertEquals(Document.of("name", 13), document.get());
    }

    @Test
    public void shouldAvoidDuplicatedDocumentWhenAddList() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name", 13));
        DocumentEntity entity = DocumentEntity.of("documentCollection");
        entity.addAll(documents);
        assertEquals(1, entity.size());
        assertEquals(1, DocumentEntity.of("documentCollection", documents).size());
    }

    @Test
    public void shouldReturnsTheDocumentNames() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);
        assertThat(collection.getDocumentNames(), containsInAnyOrder("name", "name2", "name3", "name4", "name5"));

    }

    @Test
    public void shouldReturnsTheDocumentValues() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);
        assertThat(collection.getValues(), containsInAnyOrder(Value.of(10), Value.of(11), Value.of(12),
                Value.of(13), Value.of(16)));
    }

    @Test
    public void shouldReturnTrueWhenContainsElement() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);

        assertTrue(collection.contains("name"));
        assertTrue(collection.contains("name2"));
        assertTrue(collection.contains("name3"));
        assertTrue(collection.contains("name4"));
        assertTrue(collection.contains("name5"));
    }

    @Test
    public void shouldReturnFalseWhenDoesNotContainElement() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);

        assertFalse(collection.contains("name6"));
        assertFalse(collection.contains("name7"));
        assertFalse(collection.contains("name8"));
        assertFalse(collection.contains("name9"));
        assertFalse(collection.contains("name10"));
    }

    @Test
    public void shouldRemoveAllElementsWhenUseClearMethod() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);

        assertFalse(collection.isEmpty());
        collection.clear();
        assertTrue(collection.isEmpty());
    }

}