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
package jakarta.nosql.tck;

import java.util.logging.Logger;

public class NoSQLTypeCondition {

    public static final String DISABLE_IF_KEY_VALUE = NoSQLTypeCondition.class.getName() + "#isKeyValueType";
    private static final Logger LOGGER = Logger.getLogger(NoSQLTypeCondition.class.getName());

    public static boolean isKeyValueType() {
        var type = NoSQLType.get();
        LOGGER.info("Checking if NoSQLType is KEY_VALUE, the nosql type: " + type);
        return NoSQLType.KEY_VALUE.equals(type);
    }
}
