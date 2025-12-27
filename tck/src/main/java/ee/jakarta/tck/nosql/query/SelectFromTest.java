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
package ee.jakarta.tck.nosql.query;

import org.junit.jupiter.api.DisplayName;

@DisplayName("The Jakarta Query integration test using select without where clause")
public class SelectFromTest {

    //FROM
    //empty with class
    //empty with Projection [class]
    //with entity class
    //should return error, when empty and there is no projection
    //should order by asc
    //should order by desc
    //should return error when select has update
    //should return error when select has delete
}
