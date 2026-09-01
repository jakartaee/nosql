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
package ee.jakarta.tck.nosql.select;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Beer;
import ee.jakarta.tck.nosql.entities.Coffee;
import ee.jakarta.tck.nosql.entities.Drink;
import ee.jakarta.tck.nosql.factories.DrinkListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The query execution exploring inherited entities and subtypes")
public class SelectTemplateInheritanceTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When selecting inherited entities by alcohol percentage less than a threshold")
    class WhenTheAlcoholPercentageLessThanSelection {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only inherited entities below the requested alcohol percentage")
        void shouldReturnOnlyMatchingInheritedEntities(List<Drink> entities) {

            // Given
            insertDrinks(entities);
            double upperBound = entities.getFirst().getAlcoholPercentage() + 1;

            try {
                // When
                List<Drink> result = template.select(Drink.class)
                        .where("alcoholPercentage")
                        .lt(upperBound)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("drinks returned for the less-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("drinks below the requested alcohol percentage")
                            .allMatch(drink -> drink.getAlcoholPercentage() < upperBound);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting inherited entities by alcohol percentage less than or equal to a threshold")
    class WhenTheAlcoholPercentageLessThanOrEqualToSelection {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only inherited entities at or below the requested alcohol percentage")
        void shouldReturnOnlyMatchingInheritedEntities(List<Drink> entities) {

            // Given
            insertDrinks(entities);
            double alcoholPercentage = entities.getFirst().getAlcoholPercentage();

            try {
                // When
                List<Drink> result = template.select(Drink.class)
                        .where("alcoholPercentage")
                        .lte(alcoholPercentage)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("drinks returned for the less-than-or-equal filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("drinks at or below the requested alcohol percentage")
                            .allMatch(drink -> drink.getAlcoholPercentage() <= alcoholPercentage);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting inherited entities by alcohol percentage greater than a threshold")
    class WhenTheAlcoholPercentageGreaterThanSelection {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only inherited entities above the requested alcohol percentage")
        void shouldReturnOnlyMatchingInheritedEntities(List<Drink> entities) {

            // Given
            insertDrinks(entities);
            Drink secondDrink = entities.stream()
                    .sorted(Comparator.comparingDouble(Drink::getAlcoholPercentage))
                    .skip(1)
                    .findFirst()
                    .orElseThrow();

            try {
                // When
                List<Drink> result = template.select(Drink.class)
                        .where("alcoholPercentage")
                        .gt(secondDrink.getAlcoholPercentage())
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("drinks returned for the greater-than filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("drinks above the requested alcohol percentage")
                            .allMatch(drink -> drink.getAlcoholPercentage() > secondDrink.getAlcoholPercentage());
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting inherited entities by alcohol percentage greater than or equal to a threshold")
    class WhenTheAlcoholPercentageGreaterThanOrEqualToSelection {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only inherited entities at or above the requested alcohol percentage")
        void shouldReturnOnlyMatchingInheritedEntities(List<Drink> entities) {

            // Given
            insertDrinks(entities);
            double alcoholPercentage = entities.getFirst().getAlcoholPercentage();

            try {
                // When
                List<Drink> result = template.select(Drink.class)
                        .where("alcoholPercentage")
                        .gte(alcoholPercentage)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("drinks returned for the greater-than-or-equal filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("drinks at or above the requested alcohol percentage")
                            .allMatch(drink -> drink.getAlcoholPercentage() >= alcoholPercentage);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting inherited entities by an alcohol percentage range")
    class WhenTheAlcoholPercentageRangeSelection {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only inherited entities whose alcohol percentage falls within the requested range")
        void shouldReturnOnlyMatchingInheritedEntities(List<Drink> entities) {

            // Given
            insertDrinks(entities);
            Drink secondDrink = entities.stream()
                    .sorted(Comparator.comparingDouble(Drink::getAlcoholPercentage))
                    .skip(1)
                    .findFirst()
                    .orElseThrow();
            double lowerBound = secondDrink.getAlcoholPercentage();
            double upperBound = lowerBound + 5;

            try {
                // When
                List<Drink> result = template.select(Drink.class)
                        .where("alcoholPercentage")
                        .between(lowerBound, upperBound)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("drinks returned for the alcohol percentage range filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("drinks whose alcohol percentage falls within the requested range")
                            .allMatch(drink -> drink.getAlcoholPercentage() >= lowerBound
                                    && drink.getAlcoholPercentage() <= upperBound);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting inherited subtypes")
    class WhenTheSubtypeSelection {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only instances of each requested inherited subtype")
        void shouldReturnOnlyTheRequestedSubtypes(List<Drink> entities) {

            // Given
            insertDrinks(entities);

            try {
                // When
                List<Drink> coffees = template.select(Coffee.class).result();
                List<Drink> beers = template.select(Beer.class).result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(coffees)
                            .as("coffee subtype results")
                            .isNotEmpty();
                    softly.assertThat(coffees)
                            .as("coffee subtype instances")
                            .allMatch(Coffee.class::isInstance);
                    softly.assertThat(beers)
                            .as("beer subtype results")
                            .isNotEmpty();
                    softly.assertThat(beers)
                            .as("beer subtype instances")
                            .allMatch(Beer.class::isInstance);
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting subtype entities by country equality")
    class WhenTheSubtypeCountryEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only subtype entities with the requested country")
        void shouldReturnOnlyMatchingSubtypeEntities(List<Drink> entities) {

            // Given
            insertDrinks(entities);
            Coffee coffee = entities.stream()
                    .filter(Coffee.class::isInstance)
                    .map(Coffee.class::cast)
                    .findFirst()
                    .orElseThrow();

            try {
                // When
                List<Coffee> coffees = template.select(Coffee.class)
                        .where("country")
                        .eq(coffee.getCountry())
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(coffees)
                            .as("coffees returned for the country filter")
                            .isNotEmpty();
                    softly.assertThat(coffees)
                            .as("coffees matching the requested country")
                            .allMatch(result -> result.getCountry().equals(coffee.getCountry()));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting subtype entities by style equality")
    class WhenTheSubtypeStyleEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(DrinkListSupplier.class)
        @DisplayName("Should return only subtype entities with the requested style")
        void shouldReturnOnlyMatchingSubtypeEntities(List<Drink> entities) {

            // Given
            insertDrinks(entities);
            Beer beer = entities.stream()
                    .filter(Beer.class::isInstance)
                    .map(Beer.class::cast)
                    .findFirst()
                    .orElseThrow();

            try {
                // When
                List<Beer> beers = template.select(Beer.class)
                        .where("style")
                        .eq(beer.getStyle())
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(beers)
                            .as("beers returned for the style filter")
                            .isNotEmpty();
                    softly.assertThat(beers)
                            .as("beers matching the requested style")
                            .allMatch(result -> result.getStyle().equals(beer.getStyle()));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    private void insertDrinks(List<Drink> entities) {
        entities.forEach(template::insert);
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported inheritance-based select operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
