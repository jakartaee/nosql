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
import jakarta.nosql.DiscriminatorColumn;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import jakarta.nosql.Inheritance;

import java.util.Objects;

@Inheritance
@DiscriminatorColumn("type")
@Entity
public abstract class Drink {

    @Id
    protected String id;

    @Column
    protected String name;

    @Column
    protected double alcoholPercentage;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAlcoholPercentage(double alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Drink drink)) {
            return false;
        }
        return Objects.equals(id, drink.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
