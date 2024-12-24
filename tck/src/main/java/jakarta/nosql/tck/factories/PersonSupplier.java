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

import jakarta.nosql.tck.entities.Person;
import net.datafaker.providers.base.Number;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class PersonSupplier extends AbstractSupplier<Person> {

    @Override
    public Person get() {

        Number number = faker().number();
        Person person = new Person();
        person.setId(number.numberBetween(1L, 1000L));
        person.setName(faker().name().fullName());
        person.setAge(number.numberBetween(5, 80));
        return person;
    }
}
