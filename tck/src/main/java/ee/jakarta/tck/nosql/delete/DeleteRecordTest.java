/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.nosql.delete;

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

@DisplayName("Deleting record entities through the template")
public class DeleteRecordTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When deleting record entities through an equality condition")
    class WhenTheRecordDeletionUsesEqualityCondition {

        @ParameterizedTest
        @ArgumentsSource(BookListSupplier.class)
        @DisplayName("Should delete record entities matching the selected value")
        void shouldDeleteRecordEntitiesMatchingTheSelectedValue(List<Book> books) {
            // Given
            books.forEach(template::insert);
            var title = books.getFirst().title();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Book.class)
                        .where("title")
                        .eq(title)
                        .execute();

                // Then
                var deletedBooks = template.select(Book.class)
                        .where("title")
                        .eq(title)
                        .result();

                assertSoftly(softly -> softly.assertThat(deletedBooks)
                        .as("record entities matching the deleted equality value")
                        .isEmpty());
            });
        }
    }

    @Nested
    @DisplayName("When deleting record entities through combined conditions")
    class WhenTheRecordDeletionUsesCompositeCondition {

        @ParameterizedTest
        @ArgumentsSource(BookListSupplier.class)
        @DisplayName("Should delete record entities matching every selected condition")
        void shouldDeleteRecordEntitiesMatchingEverySelectedCondition(List<Book> books) {
            // Given
            books.forEach(template::insert);
            var genre = books.getFirst().genre();
            var author = books.getFirst().author();

            assertDeleteOrUnsupported(() -> {
                // When
                template.delete(Book.class)
                        .where("genre")
                        .eq(genre)
                        .and("author")
                        .eq(author)
                        .execute();

                // Then
                var deletedBooks = template.select(Book.class)
                        .where("genre")
                        .eq(genre)
                        .and("author")
                        .eq(author)
                        .result();

                assertSoftly(softly -> softly.assertThat(deletedBooks)
                        .as("record entities matching the deleted combined conditions")
                        .isEmpty());
            });
        }
    }

    private void assertDeleteOrUnsupported(Runnable scenario) {
        try {
            scenario.run();
        } catch (UnsupportedOperationException exception) {
            assertThat(exception)
                    .as("delete operations may be unsupported by the provider")
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
