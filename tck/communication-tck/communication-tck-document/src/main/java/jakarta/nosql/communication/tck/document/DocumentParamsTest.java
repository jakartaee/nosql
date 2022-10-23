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

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocumentParamsTest {

    @Test
    public void shouldAddParamter() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertNotNull(name);
        assertThat(params.getParametersNames()).contains("name");
    }

    @Test
    public void shouldNotUseValueWhenIsInvalid() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertThrows(QueryException.class, name::get);

        assertThrows(QueryException.class, () -> name.get(String.class));
    }

    @Test
    public void shouldSetParameter() {
        Params params = Params.newParams();
        Value name = params.add("name");
        Document document = Document.of("name", name);
        params.bind("name", "Ada Lovelace");

        assertEquals("Ada Lovelace", document.get());

        params.bind("name", "Diana");
        assertEquals("Diana", document.get());
    }

}