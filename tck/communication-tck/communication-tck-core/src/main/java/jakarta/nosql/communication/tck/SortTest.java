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
package jakarta.nosql.communication.tck;

import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SortTest {

    @Test
    public void shouldReturnErrorWhenParameterIsRequired() {
        Assertions.assertThrows(NullPointerException.class, ()-> Sort.of(null, SortType.ASC));
        Assertions.assertThrows(NullPointerException.class, ()-> Sort.of("name", null));
        Assertions.assertThrows(NullPointerException.class, ()-> Sort.asc(null));
        Assertions.assertThrows(NullPointerException.class, ()-> Sort.desc(null));
    }

    @Test
    public void shouldCreateInstance() {
        Sort asc = Sort.of("name", SortType.ASC);
        Sort desc = Sort.of("name", SortType.DESC);

        Assertions.assertEquals("name", asc.getName());
        Assertions.assertEquals("name", desc.getName());

        Assertions.assertEquals(SortType.ASC, asc.getType());
        Assertions.assertEquals(SortType.DESC, desc.getType());
    }

    @Test
    public void shouldCreateInstanceFromAsc() {
        Sort sort = Sort.asc("name");
        Assertions.assertEquals(Sort.of("name", SortType.ASC), sort);
    }

    @Test
    public void shouldCreateInstanceFromDesc() {
        Sort sort = Sort.desc("name");
        Assertions.assertEquals(Sort.of("name", SortType.DESC), sort);
    }

}