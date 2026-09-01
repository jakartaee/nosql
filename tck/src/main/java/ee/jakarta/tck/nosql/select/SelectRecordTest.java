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
import ee.jakarta.tck.nosql.entities.Book;
import ee.jakarta.tck.nosql.factories.BookListSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("The query execution exploring record entities")
public class SelectRecordTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When selecting record entities without filters")
    class WhenTheRecordEntitySelection {

        @ParameterizedTest
        @ArgumentsSource(BookListSupplier.class)
        @DisplayName("Should return every inserted record entity")
        void shouldReturnAllRecordEntities(List<Book> books) {

            // Given
            insertBooks(books);

            try {
                // When
                List<Book> result = template.select(Book.class).result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("books returned without filters")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("number of books returned without filters")
                            .hasSize(books.size());
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting record entities by title equality")
    class WhenTheTitleEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(BookListSupplier.class)
        @DisplayName("Should return only record entities with the requested title")
        void shouldReturnOnlyMatchingRecordEntities(List<Book> books) {

            // Given
            insertBooks(books);
            String title = books.getFirst().title();

            try {
                // When
                List<Book> result = template.select(Book.class)
                        .where("title")
                        .eq(title)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("books returned for the title equality filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("books matching the requested title")
                            .allMatch(book -> book.title().equals(title));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting record entities with a title LIKE pattern")
    class WhenTheTitleLikeSelection {

        @ParameterizedTest
        @ArgumentsSource(BookListSupplier.class)
        @DisplayName("Should return only record entities whose titles match the requested pattern")
        void shouldReturnOnlyMatchingRecordEntities(List<Book> books) {

            // Given
            insertBooks(books);
            String title = books.getFirst().title();

            try {
                // When
                List<Book> result = template.select(Book.class)
                        .where("title")
                        .like(title)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("books returned for the title LIKE filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("books whose titles match the requested pattern")
                            .allMatch(book -> book.title().contains(title));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    @Nested
    @DisplayName("When selecting record entities by genre equality")
    class WhenTheGenreEqualitySelection {

        @ParameterizedTest
        @ArgumentsSource(BookListSupplier.class)
        @DisplayName("Should return only record entities with the requested genre")
        void shouldReturnOnlyMatchingRecordEntities(List<Book> books) {

            // Given
            insertBooks(books);
            String genre = books.getFirst().genre();

            try {
                // When
                List<Book> result = template.select(Book.class)
                        .where("genre")
                        .eq(genre)
                        .result();

                // Then
                assertSoftly(softly -> {
                    softly.assertThat(result)
                            .as("books returned for the genre equality filter")
                            .isNotEmpty();
                    softly.assertThat(result)
                            .as("books matching the requested genre")
                            .allMatch(book -> book.genre().equals(genre));
                });
            } catch (UnsupportedOperationException exception) {
                assertOperationIsUnsupported(exception);
            }
        }
    }

    private void insertBooks(List<Book> books) {
        books.forEach(template::insert);
    }

    private void assertOperationIsUnsupported(UnsupportedOperationException exception) {
        assertThat(exception)
                .as("providers may report unsupported record-based select operations")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
