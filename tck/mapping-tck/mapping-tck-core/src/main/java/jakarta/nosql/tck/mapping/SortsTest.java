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
package jakarta.nosql.tck.mapping;

import jakarta.nosql.Sort;
import jakarta.nosql.mapping.Sorts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortsTest {

    @Test
    public void shouldCreateNewSortInstance() {
        Sorts sorts = Sorts.sorts();
        assertTrue(sorts.getSorts().isEmpty());
    }

    @Test
    public void shouldAsc() {
        Sorts sorts = Sorts.sorts().asc("name");
        assertThat(sorts.getSorts()).contains(Sort.asc("name"));
    }

    @Test
    public void shouldDesc() {
        Sorts sorts = Sorts.sorts().desc("name");
        assertThat(sorts.getSorts()).contains(Sort.desc("name"));
    }

    @Test
    public void shouldAdd() {
        Sort sort = Sort.desc("name");
        Sorts sorts = Sorts.sorts().add(sort);
        assertThat(sorts.getSorts()).contains(sort);
    }

    @Test
    public void shouldRemove() {
        Sort sort = Sort.desc("name");
        Sorts sorts = Sorts.sorts().add(sort).desc("name");
        sorts.remove(sort);
        assertTrue(sorts.getSorts().isEmpty());
    }

    @Test
    public void shouldReturnErrorWhenUsesNullParameters(){
        assertThrows(NullPointerException.class, () -> Sorts.sorts().asc(null));
        assertThrows(NullPointerException.class, () -> Sorts.sorts().desc(null));
        assertThrows(NullPointerException.class, () -> Sorts.sorts().add(null));
        assertThrows(NullPointerException.class, () -> Sorts.sorts().remove(null));
    }
}