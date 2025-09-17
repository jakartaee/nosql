/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package ee.jakarta.tck.nosql.select;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.PersonListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

@DisplayName("The query execution to the basic operations on the fluent API")
public class BasicOperationsTemplateTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and select with no conditions")
    void shouldExecuteEq(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            String id = entities.get(0).getId();
            List<Person> result = template.select(Person.class)
                    .where("id").eq(id)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(person -> person.getId().equals(id));
        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

}
