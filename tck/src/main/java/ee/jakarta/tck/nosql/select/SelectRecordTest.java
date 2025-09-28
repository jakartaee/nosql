/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.nosql.select;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Book;
import ee.jakarta.tck.nosql.factories.BookListSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

@DisplayName("The query execution exploring records")
public class SelectRecordTest extends AbstractTemplateTest {


    @ParameterizedTest
    @ArgumentsSource(BookListSupplier.class)
    @DisplayName("Should insert book and select with no conditions")
    void shouldInsertBookAndSelectWithNoConditions(List<Book> books) {
        books.forEach(book -> template.insert(book));

        try {
            List<Book> result = template.select(Book.class)
                    .result();

            SoftAssertions.assertSoftly(soft -> soft.assertThat(result)
                    .isNotEmpty()
                    .hasSize(books.size()));
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BookListSupplier.class)
    @DisplayName("Should select book by title")
    void shouldSelectBookByTitle(List<Book> books) {
        books.forEach(book -> template.insert(book));

        try {
            var result = template.select(Book.class)
                    .where("title")
                    .eq(books.getFirst().title())
                    .<Book>result();

            SoftAssertions.assertSoftly(soft -> soft.assertThat(result)
                    .isNotEmpty()
                    .allMatch(book -> book.title().equals(books.getFirst().title())));
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BookListSupplier.class)
    @DisplayName("Should select book with 'like' condition")
    void shouldSelectBookWithLikeCondition(List<Book> books) {
        books.forEach(book -> template.insert(book));

        try {
            var result = template.select(Book.class)
                    .where("title")
                    .like(books.getFirst().title())
                    .<Book>result();

            SoftAssertions.assertSoftly(soft -> soft.assertThat(result)
                    .isNotEmpty()
                    .allMatch(book -> book.title().contains(books.getFirst().title())));
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BookListSupplier.class)
    @DisplayName("Should select book by genre")
    void shouldSelectBookByGenre(List<Book> books) {
        books.forEach(book -> template.insert(book));

        try {
            var result = template.select(Book.class)
                    .where("genre")
                    .eq(books.getFirst().genre())
                    .<Book>result();

            SoftAssertions.assertSoftly(soft -> soft.assertThat(result)
                    .isNotEmpty()
                    .allMatch(book -> book.genre().equals(books.getFirst().genre())));
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class));
        }
    }
}
