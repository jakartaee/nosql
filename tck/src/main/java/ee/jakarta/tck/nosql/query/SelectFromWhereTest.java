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

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

@DisplayName("The Jakarta Query integration test using select where clause")
public class SelectFromWhereTest extends AbstractTemplateTest {

    @Nested
    @DisplayName("When there is param binder")
    class WhenThereIsParamBinder {
        //should query with only where
        //should return error when where there is no projection
        //should eq
        //should net
        //should gt
        //should gte
        //should lt
        //should lte
        //should like
        //should in
    }

    @Nested
    @DisplayName("When there is no param binder")
    class WhenThereIsNoParamBinder{
    }

    //should query with only where
    //should return error when where there is no projection
    //should eq
    //should net
    //should gt
    //should gte
    //should lt
    //should lte
    //should like
    //should in

}
