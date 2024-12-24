package jakarta.nosql.tck.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import net.datafaker.Faker;

import java.util.UUID;

@Entity
public record Book(@Id String id, @Column String title, @Column String author, @Column String publisher,
                   @Column String genre) {

    public static Book of(Faker faker){
        var book = faker.book();
        return new Book(UUID.randomUUID().toString(), book.title(), book.author(), book.publisher(), book.genre());
    }
}
