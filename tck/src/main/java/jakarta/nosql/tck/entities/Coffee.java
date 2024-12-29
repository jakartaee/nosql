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
import jakarta.nosql.DiscriminatorValue;
import jakarta.nosql.Entity;
import net.datafaker.Faker;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@DiscriminatorValue("COFFEE")
@Entity
public class Coffee extends Drink {

    @Column
    private String country;

    @Column
    private String region;

    @Column
    private String blendName;

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getBlendName() {
        return blendName;
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", blendName='" + blendName + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public static Coffee of(Faker faker) {
        var fakeCoffee = faker.coffee();
        Coffee coffee = new Coffee();
        coffee.country = fakeCoffee.country();
        coffee.region = fakeCoffee.region();
        coffee.blendName = fakeCoffee.blendName();
        coffee.name = fakeCoffee.name1();
        coffee.id = UUID.randomUUID().toString();
        coffee.alcoholPercentage = ThreadLocalRandom.current().nextDouble(0, 10);
        return coffee;
    }
}
