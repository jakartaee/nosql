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
package jakarta.nosql.tck.factories;

import jakarta.nosql.tck.entities.Beer;
import jakarta.nosql.tck.entities.Coffee;
import jakarta.nosql.tck.entities.Drink;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class DrinkSupplier extends AbstractSupplier<Drink> {

    private static final Logger LOGGER = Logger.getLogger(DrinkSupplier.class.getName());

    private static final AtomicLong COUNTER = new AtomicLong(0L);

    @Override
    public Drink get() {
        var counter = COUNTER.incrementAndGet();
        LOGGER.info("Creating a new Drink instance with counter: " + counter);
        if(counter % 2 == 0) {
            return Beer.of(faker());
        } else {
            return Coffee.of(faker());
        }
    }

}
