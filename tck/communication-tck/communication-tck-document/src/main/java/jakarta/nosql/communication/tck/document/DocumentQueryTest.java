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

import jakarta.nosql.Sort;
import jakarta.nosql.document.DocumentQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static jakarta.nosql.document.DocumentQuery.select;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DocumentQueryTest {

    private DocumentQuery query;

    @BeforeEach
    public void setUp() {
        query = select().from("documentCollection").build();
    }


    @Test
    public void shouldNotRemoveDocuments() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            List<String> documents = query.getDocuments();
            assertTrue(documents.isEmpty());
            documents.clear();
        });
    }


    @Test
    public void shouldNotRemoveSort() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            List<Sort> sorts = query.getSorts();
            assertTrue(sorts.isEmpty());
            sorts.clear();
        });
    }
}