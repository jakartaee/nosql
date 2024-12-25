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

    @Column
    private Integer age;

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

    public Integer getAge() {
        return age;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    Animal() {
    }

    public Animal(String id, String name, String scientificName, String genus, String species, Integer age) {
        this.name = name;
        this.scientificName = scientificName;
        this.genus = genus;
        this.species = species;
        this.id = id;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", genus='" + genus + '\'' +
                ", species='" + species + '\'' +
                ", age=" + age +
                ", id='" + id + '\'' +
                '}';
    }

    public static Animal of(Faker faker) {
        var animal = faker.animal();
        var age = faker.number().numberBetween(1, 1000);
        return new Animal(UUID.randomUUID().toString(), animal.name(), animal.scientificName(), animal.genus(), animal.species(), age);
    }


}
