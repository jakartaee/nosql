/*
 * Copyright (c) 2019 Otavio Santana and others
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
package jakarta.nosql;

import java.util.function.Supplier;

/**
 * This enum contains all the commons configurations that might be used to the NoSQL databases.
 * It implements {@link Supplier} which returns the property value on the arrangement.
 */
public enum Configurations implements Supplier<String> {
    /**
     * to set a user in a NoSQL database
     */
    USER("jakarta.nosql.user"),
    /**
     * to set a password in a database
     */
    PASSWORD("jakarta.nosql.password"),
    /**
     * the host configuration that might have more than one with a number as a suffix,
     * such as jakarta.nosql.host-1=localhost, jakarta.nosql.host-2=host2
     */
    HOST("jakarta.nosql.host"),
    /**
     * A configuration to set the encryption to settings property.
     */
    ENCRYPTION("jakarta.nosql.settings.encryption");

    private final String configuration;

    Configurations(String configuration) {
        this.configuration = configuration;
    }


    @Override
    public String get() {
        return configuration;
    }
}
