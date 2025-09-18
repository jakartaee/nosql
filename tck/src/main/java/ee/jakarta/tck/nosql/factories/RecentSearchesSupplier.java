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

import ee.jakarta.tck.nosql.entities.RecentSearches;

import java.util.UUID;

public class RecentSearchesSupplier extends AbstractSupplier<RecentSearches> {

    @Override
    public RecentSearches get() {
        var userId = UUID.randomUUID().toString();
        var keywords = faker().lorem().words(faker().number().numberBetween(1, 10));
        return RecentSearches.of(userId, keywords);
    }
}
