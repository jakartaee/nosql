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

import ee.jakarta.tck.nosql.entities.Profile;
import net.datafaker.Faker;

import java.util.Map;

public class ProfileSupplier extends AbstractSupplier<Profile>  {

    @Override
    public Profile get() {
        Faker faker = faker();
        var name = faker.name().fullName();
        Map<String, String> socialMedia = Map.of("socialMediaA", faker.internet().url(),
        "socialMediaB", faker.internet().url());

        return new Profile(name, socialMedia);
    }
}
