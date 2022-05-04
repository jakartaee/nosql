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
package jakarta.nosql.communication.tck.document;


import jakarta.nosql.Condition;
import jakarta.nosql.TypeReference;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentDeleteQuery;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static jakarta.nosql.document.DocumentCondition.eq;
import static jakarta.nosql.document.DocumentDeleteQuery.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DeleteQueryBuilderTest {

    @Test
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        Assertions.assertThrows(NullPointerException.class,() -> builder("document", "document", null));
    }

    @Test
    public void shouldDelete() {
        String documentCollection = "documentCollection";
        DocumentDeleteQuery query = builder().from(documentCollection).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
    }

    @Test
    public void shouldDeleteDocuments() {
        String documentCollection = "documentCollection";
        DocumentDeleteQuery query = builder("document", "document2").from(documentCollection).build();
        assertThat(query.getDocuments(), containsInAnyOrder("document", "document2"));
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
    }


    @Test
    public void shouldReturnErrorWhenFromIsNull() {
        Assertions.assertThrows(NullPointerException.class,() -> builder().from(null));
    }

    @Test
    public void shouldSelectWhereNameEq() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.eq("name", name))
                .build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(name, document.get());

    }

    @Test
    public void shouldSelectWhereNameLike() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.like("name", name))
                .build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.LIKE, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(name, document.get());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.gt("name", value))
                .build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.GREATER_THAN, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = builder()
                .from(documentCollection)
                .where(DocumentCondition.gte("name", value)).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.lt("name", value)).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.LESSER_THAN, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.lte("name", value)).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        String documentCollection = "documentCollection";
        Number valueA = 10;
        Number valueB = 20;

        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.between("name", Arrays.asList(valueA, valueB)))
                .build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.BETWEEN, condition.getCondition());
        assertEquals("name", document.getName());
        assertThat(document.get(new TypeReference<List<Number>>() {}), Matchers.contains(10, 20));
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.eq("name", name).negate()).build();
        DocumentCondition condition = query.getCondition().get();

        Document column = condition.getDocument();
        DocumentCondition negate = column.get(DocumentCondition.class);
        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.NOT, condition.getCondition());
        assertEquals(Condition.EQUALS, negate.getCondition());
        assertEquals("name", negate.getDocument().getName());
        assertEquals(name, negate.getDocument().get());
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentCondition nameEqualsAda = DocumentCondition.eq("name", name);
        DocumentCondition olderThenTen = DocumentCondition.gt("age", 10);
        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.and(nameEqualsAda, olderThenTen)).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10))));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentCondition nameEqualsAda = DocumentCondition.eq("name", name);
        DocumentCondition olderThenTen = DocumentCondition.gt("age", 10);
        DocumentDeleteQuery query = builder().from(documentCollection)
                .where(DocumentCondition.or(nameEqualsAda, olderThenTen)).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.OR, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10))));
    }



    @Test
    public void shouldDeleteNegate() {
        String columnFamily = "columnFamily";
        DocumentCondition cityNotEqualsAssis = DocumentCondition.eq("city", "Assis").negate();
        DocumentCondition nameNotEqualsLucas = DocumentCondition.eq("name", "Lucas").negate();

        DocumentDeleteQuery query = builder().from(columnFamily)
                .where(DocumentCondition.and(cityNotEqualsAssis, nameNotEqualsLucas)).build();

        DocumentCondition condition = query.getCondition().orElseThrow(RuntimeException::new);
        assertEquals(columnFamily, query.getDocumentCollection());
        Document column = condition.getDocument();
        List<DocumentCondition> conditions = column.get(new TypeReference<List<DocumentCondition>>() {
        });

        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, containsInAnyOrder(eq(Document.of("city", "Assis")).negate(),
                eq(Document.of("name", "Lucas")).negate()));


    }

    @Test
    public void shouldExecuteDelete() {
        String collection = "collection";
        DocumentCollectionManager manager = mock(DocumentCollectionManager.class);
        ArgumentCaptor<DocumentDeleteQuery> queryCaptor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        builder().from(collection).delete(manager);
        verify(manager).delete(queryCaptor.capture());

        DocumentDeleteQuery query = queryCaptor.getValue();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(collection, query.getDocumentCollection());
    }
}
