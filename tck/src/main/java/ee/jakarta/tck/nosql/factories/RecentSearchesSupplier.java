package ee.jakarta.tck.nosql.factories;

import ee.jakarta.tck.nosql.entities.Person;

public class RecentSearchesSupplier extends AbstractSupplier<Person> {

    @Override
    public Person get() {
        return Person.of(faker());
    }
}
