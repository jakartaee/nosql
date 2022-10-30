/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentConditionTest {

    @Test
    public void shouldReturnNPEInEqWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.eq(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.eq("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.eq(null, "value"));
    }

    @Test
    public void shouldCreateEqFromDocument() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.eq(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.EQUALS, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldCreateNegationCondition() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DocumentCondition.gt(age);
        DocumentCondition negate = condition.negate();
        Document negateDocument = negate.getDocument();
        assertEquals(Condition.NOT, negate.getCondition());
        assertEquals(Condition.NOT.getNameField(), negateDocument.getName());
        assertEquals(DocumentCondition.gt(age), negateDocument.getValue().get());
    }

    @Test
    public void shouldReturnValidDoubleNegation() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DocumentCondition.gt(age);
        DocumentCondition affirmative = condition.negate().negate();
        Assertions.assertEquals(condition, affirmative);
    }


    @Test
    public void shouldCreateNotCondition() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DocumentCondition.gt(age);
        DocumentCondition negate = DocumentCondition.not(condition);
        Document negateDocument = negate.getDocument();
        assertEquals(Condition.NOT, negate.getCondition());
        assertEquals(Condition.NOT.getNameField(), negateDocument.getName());
        assertEquals(DocumentCondition.gt(age), negateDocument.getValue().get());
    }

    @Test
    public void shouldReturnValidDoubleNot() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DocumentCondition.gt(age);
        DocumentCondition affirmative = DocumentCondition.not(DocumentCondition.not(condition));
        Assertions.assertEquals(condition, affirmative);
    }

    @Test
    public void shouldShouldReturnErrorOnNot() {
        Assertions.assertThrows(NullPointerException.class, ()-> DocumentCondition.not(null));
    }

    @Test
    public void shouldCreateEqFromNameValue() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.eq("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.EQUALS, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldReturnNPEInGtWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.gt(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.gt("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.gt(null, "value"));
    }

    @Test
    public void shouldCreateGtFromDocument() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.gt(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.GREATER_THAN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldCreateGtFromNameValue() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.gt("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.GREATER_THAN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldReturnNPEInGetWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.gte(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.gte("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.gte(null, "value"));
    }

    @Test
    public void shouldCreateGteFromDocument() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.gte(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldCreateGteFromNameValue() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.gte("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldReturnNPEInLtWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.lt(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.lt("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.lt(null, "value"));
    }

    @Test
    public void shouldCreateLtFromDocument() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.lt(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LESSER_THAN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldCreateLtFromNameValue() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.lt("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LESSER_THAN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldReturnNPEInLteWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.lte(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.lte("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.eq(null, "value"));
    }

    @Test
    public void shouldCreateLteFromDocument() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.lte(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldCreateLteFromNameValue() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.lte("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldReturnNPEInInWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.in(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.in("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.in(null, "value"));
    }

    @Test
    public void shouldCreateInFromDocument() {
        Document document = Document.of("name", Collections.singleton("Ada Lovelace"));
        DocumentCondition condition = DocumentCondition.in(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.IN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldCreateInFromNameValue() {
        Document document = Document.of("name", Collections.singleton("Ada Lovelace"));
        DocumentCondition condition = DocumentCondition.in("name", Collections.singleton("Ada Lovelace"));
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.IN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldReturnNPELikeInWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.like(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.like("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.like(null, "value"));
    }

    @Test
    public void shouldCreateLikeFromDocument() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.like(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LIKE, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldCreateLikeFromNameValue() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.like("name", "Ada Lovelace");
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.LIKE, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldReturnNPEBetweenInWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.between(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.between("name", null));

        Assertions.assertThrows(NullPointerException.class,
                () -> DocumentCondition.between(null, "value"));
    }

    @Test
    public void shouldCreateBetweenFromDocument() {
        Document document = Document.of("age", Arrays.asList(10, 20));
        DocumentCondition condition = DocumentCondition.between(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.BETWEEN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldCreateBetweenFromNameValue() {
        Document document = Document.of("age", Arrays.asList(10, 20));
        DocumentCondition condition = DocumentCondition.between(document);
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.BETWEEN, condition.getCondition());
        Assertions.assertEquals(document, condition.getDocument());
    }

    @Test
    public void shouldNegate() {
        Document document = Document.of("name", "Ada Lovelace");
        DocumentCondition condition = DocumentCondition.eq(document).negate();
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(Condition.NOT, condition.getCondition());
    }

}
