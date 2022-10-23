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
import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DocumentTest {

    private static final Value DEFAULT_VALUE = Value.of(12);

    @Test
    public void shouldReturnNameWhenNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Document document = Document.of(null, DEFAULT_VALUE);
        });
    }

    @Test
    public void shouldReturnNameWhenValueIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Document document = Document.of("Name", null);
        });
    }

    @Test
    public void shouldCreateAnDocumentInstance() {
        String name = "name";
        Document document = Document.of(name, DEFAULT_VALUE);
        assertNotNull(document);
        assertEquals(name, document.getName());
        assertEquals(DEFAULT_VALUE, document.getValue());
    }

    @Test
    public void shouldBeEquals() {
        assertEquals(Document.of("name", DEFAULT_VALUE), Document.of("name", DEFAULT_VALUE));
    }

    @Test
    public void shouldReturnGetObject() {
        Value value = Value.of("text");
        Document document = Document.of("name", value);
        assertEquals(value.get(), document.get());
    }

    @Test
    public void shouldReturnGetClass() {
        Value value = Value.of("text");
        Document document = Document.of("name", value);
        assertEquals(value.get(String.class), document.get(String.class));
    }


    @Test
    public void shouldReturnGetType() {
        Value value = Value.of("text");
        Document document = Document.of("name", value);
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {
        };
        assertEquals(value.get(typeReference), document.get(typeReference));
    }
}