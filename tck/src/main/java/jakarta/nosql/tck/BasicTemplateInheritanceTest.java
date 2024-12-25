/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck;

import jakarta.nosql.tck.entities.Animal;
import jakarta.nosql.tck.factories.AnimalSupplier;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;

public class BasicTemplateInheritanceTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicTemplateInheritanceTest.class.getName());

    @ParameterizedTest
    @ArgumentsSource(AnimalSupplier.class)
    @DisplayName("Should insert the animal: {0}")
    void shouldInsert(Animal entity) {
        var animal = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(animal).isNotNull();
            soft.assertThat(animal.getId()).isNotNull();
            soft.assertThat(animal.getName()).isEqualTo(entity.getName());
            soft.assertThat(animal.getScientificName()).isEqualTo(entity.getScientificName());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalSupplier.class)
    @DisplayName("Should update the animal: {0}")
    void shouldUpdate(Animal entity) {
        var insertedAnimal = template.insert(entity);

        insertedAnimal.setSpecies("Updated Species");
        var updatedAnimal = template.update(insertedAnimal);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updatedAnimal).isNotNull();
            soft.assertThat(updatedAnimal.getId()).isEqualTo(insertedAnimal.getId());
            soft.assertThat(updatedAnimal.getSpecies()).isEqualTo("Updated Species");
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalSupplier.class)
    @DisplayName("Should delete the animal: {0}")
    void shouldDelete(Animal entity) {
        var insertedAnimal = template.insert(entity);

        template.delete(Animal.class, insertedAnimal.getId());

        var deletedAnimal = template.find(Animal.class, insertedAnimal.getId());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(deletedAnimal).isEmpty();
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalSupplier.class)
    @DisplayName("Should find the animal: {0}")
    void shouldFind(Animal entity) {
        var insertedAnimal = template.insert(entity);
        var foundAnimal = template.find(Animal.class, insertedAnimal.getId());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(foundAnimal).isPresent();
            soft.assertThat(foundAnimal.orElseThrow().getId()).isEqualTo(insertedAnimal.getId());
            soft.assertThat(foundAnimal.orElseThrow().getName()).isEqualTo(insertedAnimal.getName());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(AnimalSupplier.class)
    @DisplayName("Should insert animal with TTL")
    void shouldInsertWithTTL(Animal animal) {
        try {
            var insertedAnimal = template.insert(animal, Duration.ofMinutes(10));
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(insertedAnimal).isNotNull();
                soft.assertThat(insertedAnimal.getId()).isNotNull();
                soft.assertThat(insertedAnimal.getName()).isEqualTo(animal.getName());
            });
        } catch (UnsupportedOperationException e) {
            LOGGER.info("TTL operation not supported by this database: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should throw exception when null entity is inserted")
    void shouldThrowExceptionWhenNullEntityInserted() {
        Assertions.assertThatThrownBy(() -> template.insert(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw exception when null entity is updated")
    void shouldThrowExceptionWhenNullEntityUpdated() {
        Assertions.assertThatThrownBy(() -> template.update(null))
                .isInstanceOf(NullPointerException.class);
    }
}
