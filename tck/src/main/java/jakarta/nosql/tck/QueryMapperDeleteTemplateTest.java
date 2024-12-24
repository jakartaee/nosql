package jakarta.nosql.tck;

import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.factories.PersonListSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

public class QueryMapperDeleteTemplateTest extends AbstractTemplateTest {


    @BeforeEach
    void cleanDatabase() {
        template.delete(Person.class).execute();
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and delete with no conditions")
    void shouldInsertIterableAndDeleteNoCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Person.class).execute();

            List<Person> result = template.select(Person.class).result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and delete with simple condition")
    void shouldInsertIterableAndDeleteWithSimpleCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Person.class)
                    .where("name")
                    .eq(entities.get(0).getName())
                    .execute();

            List<Person> result = template.select(Person.class)
                    .where("name")
                    .eq(entities.get(0).getName())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and delete with greater-than condition")
    void shouldInsertIterableAndDeleteWithGreaterThanCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Person.class)
                    .where("age")
                    .gt(entities.get(0).getAge())
                    .execute();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .gt(entities.get(0).getAge())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and delete with less-than condition")
    void shouldInsertIterableAndDeleteWithLessThanCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Person.class)
                    .where("age")
                    .lt(entities.get(0).getAge())
                    .execute();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .lt(entities.get(0).getAge())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'in' condition")
    void shouldInsertIterableAndDeleteWithInCondition(List<Person> entities) {
        // Insert the entities
        entities.forEach(entity -> template.insert(entity));

        try {
            // Delete based on the 'name' field (in a list of values)
            template.delete(Person.class)
                    .where("name")
                    .in(List.of(entities.get(0).getName()))
                    .execute();

            // Verify that no persons with the given names exist
            List<Person> result = template.select(Person.class)
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
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'between' condition")
    void shouldInsertIterableAndDeleteWithBetweenCondition(List<Person> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Person.class)
                    .where("age")
                    .between(entities.get(0).getAge(), entities.get(0).getAge() + 5)
                    .execute();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .between(entities.get(0).getAge(), entities.get(0).getAge() + 5)
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }


    @ParameterizedTest
    @ArgumentsSource(PersonListSupplier.class)
    @DisplayName("Should insert Iterable and delete with 'complex' query")
    void shouldInsertIterableAndDeleteWithComplexQuery(List<Person> entities) {
        // Insert the entities
        entities.forEach(entity -> template.insert(entity));

        try {
            template.delete(Person.class)
                    .where("age")
                    .gt(entities.get(0).getAge())
                    .and("name")
                    .eq(entities.get(0).getName())
                    .execute();

            List<Person> result = template.select(Person.class)
                    .where("age")
                    .gt(entities.get(0).getAge())
                    .and("name")
                    .eq(entities.get(0).getName())
                    .result();
            Assertions.assertThat(result).isEmpty();

        } catch (UnsupportedOperationException exp) {
            // Expected for key-value or unsupported NoSQL databases
            Assertions.assertThat(exp).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}