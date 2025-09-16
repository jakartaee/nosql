/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package ee.jakarta.tck.nosql.factories;

import ee.jakarta.tck.nosql.entities.Computer;
import ee.jakarta.tck.nosql.entities.MobileSystem;
import ee.jakarta.tck.nosql.entities.Program;
import net.datafaker.Faker;

import java.util.Map;
import java.util.UUID;

public class ComputerSupplier extends AbstractSupplier<Computer>  {

    @Override
    public Computer get() {
        Faker faker = faker();
        var id = UUID.randomUUID().toString();
        var program = Program.of(faker.app().name(), Map.of("version", faker.app().version(),
                "language", faker.programmingLanguage().name()));
        var program2 = Program.of(faker.app().name(), Map.of("version", faker.app().version(),
                "language", faker.programmingLanguage().name()));
        var programs = Map.of("programA", program, "programB", program2);

        return Computer.of(id, programs);
    }
}
