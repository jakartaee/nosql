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
package ee.jakarta.tck.nosql;

import jakarta.nosql.Template;
import ee.jakarta.tck.nosql.entities.Animal;
import ee.jakarta.tck.nosql.entities.Book;
import ee.jakarta.tck.nosql.entities.Drink;
import ee.jakarta.tck.nosql.entities.Person;
import ee.jakarta.tck.nosql.entities.Vehicle;
import org.junit.jupiter.api.BeforeEach;

import java.util.logging.Logger;

public abstract class AbstractTemplateTest {

    private static final Logger LOGGER = Logger.getLogger(AbstractTemplateTest.class.getName());

    protected Template template;

    @BeforeEach
    void setUp() {
        LOGGER.info("Getting the template");
        TemplateSupplier supplier = TemplateSupplier.template();
        this.template = supplier.get();
        LOGGER.info("Cleaning up the database");
        try {
            template.delete(Person.class).execute();
            template.delete(Animal.class).execute();
            template.delete(Vehicle.class).execute();
            template.delete(Book.class).execute();
            template.delete(Drink.class).execute();
        } catch (UnsupportedOperationException e) {
            LOGGER.warning("The delete operation is not supported");
        }
    }


}
