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

@DiscriminatorValue("BEER")
@Entity
public class Beer extends Drink {

    @Column
    private String brand;

    @Column
    private String style;

    public String getBrand() {
        return brand;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return "Beer{" +
                "brand='" + brand + '\'' +
                ", style='" + style + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", alcoholPercentage=" + alcoholPercentage +
                '}';
    }

    public static Beer of(Faker faker) {
        var fakeBeer = faker.beer();
        Beer beer = new Beer();
        beer.id = UUID.randomUUID().toString();
        beer.brand = fakeBeer.brand();
        beer.name = fakeBeer.name();
        beer.style = fakeBeer.style();
        beer.alcoholPercentage = ThreadLocalRandom.current().nextDouble(10, 100);
        return beer;
    }
}
