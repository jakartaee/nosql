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
package jakarta.nosql.tck.delete;

import jakarta.nosql.tck.AbstractTemplateTest;
import jakarta.nosql.tck.NoSQLTypeCondition;
import jakarta.nosql.tck.entities.Animal;
import jakarta.nosql.tck.factories.AnimalListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

public class DeleteTemplateMappedSuperclassTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should insert Iterable and delete with no conditions")
    @EnabledIf(NoSQLTypeCondition.DISABLE_IF_KEY_VALUE)
    void shouldInsertIterableAndDeleteNoCondition(List<Animal> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Animal.class).execute();

            List<Animal> result = template.select(Animal.class).result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should insert Iterable and delete with simple condition")
    @EnabledIf(NoSQLTypeCondition.DISABLE_IF_KEY_VALUE)
    void shouldInsertIterableAndDeleteWithSimpleCondition(List<Animal> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Animal.class)
                    .where("name")
                    .eq(entities.get(0).getName())
                    .execute();

            List<Animal> result = template.select(Animal.class)
                    .where("name")
                    .eq(entities.get(0).getName())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'in' condition")
    @EnabledIf(NoSQLTypeCondition.DISABLE_IF_KEY_VALUE)
    void shouldInsertIterableAndDeleteWithInCondition(List<Animal> entities) {
        // Insert the entities
        entities.forEach(entity -> template.insert(entity));

        try {
            // Delete based on the 'name' field (in a list of values)
            template.delete(Animal.class)
                    .where("name")
                    .in(List.of(entities.get(0).getName()))
                    .execute();

            // Verify that no animals with the given names exist
            List<Animal> result = template.select(Animal.class)
                    .where("name")
                    .in(List.of(entities.get(0).getName()))
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'between' condition")
    @EnabledIf(NoSQLTypeCondition.DISABLE_IF_KEY_VALUE)
    void shouldInsertIterableAndDeleteWithBetweenCondition(List<Animal> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Animal.class)
                    .where("species")
                    .between(entities.get(0).getSpecies(), "Zebra") // Example condition
                    .execute();

            List<Animal> result = template.select(Animal.class)
                    .where("species")
                    .between(entities.get(0).getSpecies(), "Zebra") // Example condition
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'complex' query")
    @EnabledIf(NoSQLTypeCondition.DISABLE_IF_KEY_VALUE)
    void shouldInsertIterableAndDeleteWithComplexQuery(List<Animal> entities) {
        // Insert the entities
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Animal.class)
                    .where("genus")
                    .eq(entities.get(0).getGenus())
                    .and("species")
                    .eq(entities.get(0).getSpecies())
                    .execute();

            List<Animal> result = template.select(Animal.class)
                    .where("genus")
                    .eq(entities.get(0).getGenus())
                    .and("species")
                    .eq(entities.get(0).getSpecies())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
