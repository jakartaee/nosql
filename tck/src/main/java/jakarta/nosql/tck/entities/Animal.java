package jakarta.nosql.tck.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;

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


}
