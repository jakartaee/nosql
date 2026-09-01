/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
package ee.jakarta.tck.nosql.update;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.PersonListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("The template update operations")
public class UpdateTemplateTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When updating all entities without a condition")
    class WhenTheUpdateHasNoCondition {

        @ParameterizedTest
        @ArgumentsSource(PersonListSupplier.class)
        @DisplayName("Should update the selected attribute on every entity")
        void shouldUpdateTheAttributeOnEveryEntity(List<Person> entities) {

            // Given
            entities.forEach(template::insert);

            try {
                // When
                template.update(Person.class)
                        .set("name").to("Updated name")
                        .execute();
                List<Person> result = template.select(Person.class).result();

                // Then
                assertThat(result)
                        .as("entities after the unconditional update")
                        .isNotEmpty()
                        .allMatch(person -> "Updated name".equals(person.getName()));
            } catch (UnsupportedOperationException exception) {
                assertThat(exception)
                        .as("unsupported unconditional update")
                        .isInstanceOf(UnsupportedOperationException.class);
            }
        }
    }
}
