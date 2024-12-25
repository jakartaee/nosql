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

import jakarta.nosql.tck.entities.Book;
import jakarta.nosql.tck.factories.BookListSupplier;
import jakarta.nosql.tck.factories.BookSupplier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

public class QueryMapperDeleteRecordTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(BookListSupplier.class)
    @DisplayName("Should insert and delete the book")
    void shouldInsertAndDeleteBook(List<Book> books) {
        books.forEach(book -> template.insert(book));

        try {
            template.delete(Book.class)
                    .where("title")
                    .eq(books.get(0).title())
                    .execute();

            var deletedBook = template.select(Book.class)
                    .where("title")
                    .eq(books.get(0).title())
                    .result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(deletedBook).isEmpty();
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
            });
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BookListSupplier.class)
    @DisplayName("Should delete book with complex condition")
    void shouldDeleteBookWithComplexCondition(List<Book> books) {
        books.forEach(book -> template.insert(book));

        try {
            template.delete(Book.class)
                    .where("genre")
                    .eq(books.get(0).genre())
                    .and("author")
                    .eq(books.get(0).author())
                    .execute();

            var deletedBooks = template.select(Book.class)
                    .where("genre")
                    .eq(books.get(0).genre())
                    .and("author")
                    .eq(books.get(0).author())
                    .result();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(deletedBooks).isEmpty();
            });
        } catch (UnsupportedOperationException exp) {
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
            });
        }
    }

}
