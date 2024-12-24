package jakarta.nosql.tck;

import jakarta.nosql.tck.entities.Book;
import jakarta.nosql.tck.factories.BookSupplier;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Duration;

public class BasicTemplateRecordTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(BookSupplier.class)
    @DisplayName("Should insert the book: {0}")
    void shouldInsert(Book entity) {
        var book = template.insert(entity);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(book).isNotNull();
            soft.assertThat(book.id()).isNotNull();
            soft.assertThat(book.title()).isEqualTo(entity.title());
            soft.assertThat(book.author()).isEqualTo(entity.author());
            soft.assertThat(book.publisher()).isEqualTo(entity.publisher());
            soft.assertThat(book.genre()).isEqualTo(entity.genre());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(BookSupplier.class)
    @DisplayName("Should update the book: {0}")
    void shouldUpdate(Book entity) {
        var insertedBook = template.insert(entity);

        var updatedBook = template.update(insertedBook);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updatedBook).isNotNull();
            soft.assertThat(updatedBook.id()).isEqualTo(insertedBook.id());
            soft.assertThat(updatedBook.title()).isEqualTo(insertedBook.title());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(BookSupplier.class)
    @DisplayName("Should delete the book: {0}")
    void shouldDelete(Book entity) {
        var insertedBook = template.insert(entity);

        template.delete(Book.class, insertedBook.id());

        var deletedBook = template.find(Book.class, insertedBook.id());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(deletedBook).isEmpty();
        });
    }

    @ParameterizedTest
    @ArgumentsSource(BookSupplier.class)
    @DisplayName("Should find the book: {0}")
    void shouldFind(Book entity) {
        var insertedBook = template.insert(entity);
        var foundBook = template.find(Book.class, insertedBook.id());
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(foundBook).isPresent();
            soft.assertThat(foundBook.orElseThrow().id()).isEqualTo(insertedBook.id());
            soft.assertThat(foundBook.orElseThrow().title()).isEqualTo(insertedBook.title());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(BookSupplier.class)
    @DisplayName("Should insert book with TTL")
    void shouldInsertWithTTL(Book book) {
        try {
            var insertedBook = template.insert(book, Duration.ofMinutes(10));
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(insertedBook).isNotNull();
                soft.assertThat(insertedBook.id()).isNotNull();
                soft.assertThat(insertedBook.title()).isEqualTo(book.title());
            });
        } catch (UnsupportedOperationException e) {
            System.out.println("TTL operation not supported by this database: " + e.getMessage());
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
