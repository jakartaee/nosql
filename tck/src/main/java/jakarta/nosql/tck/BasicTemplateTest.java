/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck;

import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.factories.PersonSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class BasicTemplateTest extends AbstractTemplateTest {


    @ParameterizedTest
    @ArgumentsSource(PersonSupplier.class)
    @DisplayName("Should insert the person: {0}")
    void shouldInsert(Person entity) {
        var person = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(person).isNotNull();
            soft.assertThat(person.getId()).isNotNull();
            soft.assertThat(person.getName()).isEqualTo(entity.getName());
            soft.assertThat(person.getAge()).isEqualTo(entity.getAge());
        });
    }
}
