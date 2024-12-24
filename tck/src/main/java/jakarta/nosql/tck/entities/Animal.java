package jakarta.nosql.tck.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import net.datafaker.Faker;

import java.util.UUID;

@Entity
public class Animal extends AbstractAnimal {

    @Column
    private String name;

    @Column
    private String scientificName;

    @Column
    private String genus;

    @Column
    private String species;

    public String getName() {
        return name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getGenus() {
        return genus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    Animal() {
    }

    public Animal(String id, String name, String scientificName, String genus, String species) {
        this.name = name;
        this.scientificName = scientificName;
        this.genus = genus;
        this.species = species;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", genus='" + genus + '\'' +
                ", species='" + species + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public static Animal of(Faker faker) {
        var animal = faker.animal();
        return new Animal(UUID.randomUUID().toString(), animal.name(), animal.scientificName(), animal.genus(), animal.species());
    }


}
