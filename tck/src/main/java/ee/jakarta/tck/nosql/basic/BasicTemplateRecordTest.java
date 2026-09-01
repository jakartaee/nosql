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
package ee.jakarta.tck.nosql.basic;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Book;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.factories.BookSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The basic template operations using a record entity")
public class BasicTemplateRecordTest extends AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(BasicTemplateRecordTest.class.getName());

    @Nested
    @DisplayName("When inserting a record entity")
    class WhenTheInsertion {

        @ParameterizedTest
        @ArgumentsSource(BookSupplier.class)
        @DisplayName("Should persist the record entity: {0}")
        void shouldInsert(Book entity) {

            // Given

            // When
            var book = template.insert(entity);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(book)
                        .as("inserted book")
                        .isNotNull();
                softly.assertThat(book.id())
                        .as("inserted book id")
                        .isNotNull();
                softly.assertThat(book.title())
                        .as("inserted book title")
                        .isEqualTo(entity.title());
                softly.assertThat(book.author())
                        .as("inserted book author")
                        .isEqualTo(entity.author());
                softly.assertThat(book.publisher())
                        .as("inserted book publisher")
                        .isEqualTo(entity.publisher());
                softly.assertThat(book.genre())
                        .as("inserted book genre")
                        .isEqualTo(entity.genre());
            });
        }

        @ParameterizedTest
        @ArgumentsSource(BookSupplier.class)
        @DisplayName("Should persist the record entity with TTL when supported: {0}")
        void shouldInsertWithTtl(Book book) {
            try {
                // Given

                // When
                var insertedBook = template.insert(book, Duration.ofMinutes(10));

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(insertedBook)
                            .as("inserted book with ttl")
                            .isNotNull();
                    softly.assertThat(insertedBook.id())
                            .as("inserted book with ttl id")
                            .isNotNull();
                    softly.assertThat(insertedBook.title())
                            .as("inserted book with ttl title")
                            .isEqualTo(book.title());
                });
            } catch (UnsupportedOperationException exception) {
                LOGGER.info("TTL operation not supported by this database: " + exception.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("When updating a record entity")
    class WhenTheUpdate {

        @ParameterizedTest
        @ArgumentsSource(BookSupplier.class)
        @DisplayName("Should update the record entity: {0}")
        void shouldUpdate(Book entity) {

            // Given
            var insertedBook = template.insert(entity);

            // When
            var updatedBook = template.update(insertedBook);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(updatedBook)
                        .as("updated book")
                        .isNotNull();
                softly.assertThat(updatedBook.id())
                        .as("updated book id")
                        .isEqualTo(insertedBook.id());
                softly.assertThat(updatedBook.title())
                        .as("updated book title")
                        .isEqualTo(insertedBook.title());
            });
        }
    }

    @Nested
    @DisplayName("When removing a record entity")
    class WhenTheRemoval {

        @ParameterizedTest
        @ArgumentsSource(BookSupplier.class)
        @DisplayName("Should remove the record entity: {0}")
        void shouldDelete(Book entity) {

            // Given
            var insertedBook = template.insert(entity);

            // When
            template.delete(Book.class, insertedBook.id());
            var deletedBook = template.find(Book.class, insertedBook.id());

            // Then
            assertThat(deletedBook)
                    .as("book after deletion")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("When searching for a record entity")
    class WhenTheSearch {

        @ParameterizedTest
        @ArgumentsSource(BookSupplier.class)
        @DisplayName("Should return the record entity: {0}")
        void shouldFind(Book entity) {

            // Given
            var insertedBook = template.insert(entity);

            // When
            var foundBook = template.find(Book.class, insertedBook.id());

            // Then
            assertSoftly(softly -> {
                softly.assertThat(foundBook)
                        .as("found book optional")
                        .isPresent();
                foundBook.ifPresent(book -> {
                    softly.assertThat(book.id())
                            .as("found book id")
                            .isEqualTo(insertedBook.id());
                    softly.assertThat(book.title())
                            .as("found book title")
                            .isEqualTo(insertedBook.title());
                });
            });
        }
    }

    @Nested
    @DisplayName("When validating record entities")
    class WhenTheValidation {

        @Test
        @DisplayName("Should reject a null entity during insertion")
        void shouldRejectNullEntityOnInsert() {

            // Given

            // When / Then
            assertThatThrownBy(() -> template.insert(null))
                    .as("null entity insertion")
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should reject a null entity during update")
        void shouldRejectNullEntityOnUpdate() {

            // Given

            // When / Then
            assertThatThrownBy(() -> template.update((Person) null))
                    .as("null entity update")
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
