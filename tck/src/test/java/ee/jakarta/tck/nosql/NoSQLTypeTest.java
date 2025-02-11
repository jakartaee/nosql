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
package ee.jakarta.tck.nosql;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NoSQLTypeTest {

    @ParameterizedTest(name = "{0} should have a flexibility level of {1}")
    @CsvSource({
            "KEY_VALUE, 1",
            "COLUMN, 2",
            "DOCUMENT, 3",
            "GRAPH, 4",
            "OTHER, 0"
    })
    public void shouldReturnCorrectFlexibility(String type, int expectedFlexibility) {
        assertThat(NoSQLType.valueOf(type).getFlexibility()).isEqualTo(expectedFlexibility);
    }

    @ParameterizedTest(name = "get({0}) should return {1}")
    @CsvSource({
            "COLUMN, COLUMN",
            "GRAPH, GRAPH",
            "KEY_VALUE, KEY_VALUE"
    })
    public void shouldReturnTypeFromValidString(String input, String expectedType) {
        assertThat(NoSQLType.get(input)).isEqualTo(NoSQLType.valueOf(expectedType));
    }

    @ParameterizedTest(name = "get({0}) should return the default type KEY_VALUE for invalid input")
    @ValueSource(strings = {"INVALID_TYPE", "UNKNOWN", ""})
    public void shouldReturnDefaultTypeForInvalidString(String input) {
        assertThat(NoSQLType.get(input)).isEqualTo(NoSQLType.KEY_VALUE);
    }

    @Test
    public void shouldReturnTypeFromValidSystemProperty() {
        System.setProperty(NoSQLType.DATABASE_TYPE_PROPERTY, "COLUMN");
        assertThat(NoSQLType.get()).isEqualTo(NoSQLType.COLUMN);
        System.clearProperty(NoSQLType.DATABASE_TYPE_PROPERTY);
    }

    @Test
    public void shouldReturnDefaultTypeForInvalidSystemProperty() {
        System.setProperty(NoSQLType.DATABASE_TYPE_PROPERTY, "INVALID_TYPE");
        assertThat(NoSQLType.get()).isEqualTo(NoSQLType.KEY_VALUE);
        System.clearProperty(NoSQLType.DATABASE_TYPE_PROPERTY);
    }

    @Test
    public void shouldReturnDefaultTypeWhenNoSystemPropertyIsSet() {
        System.clearProperty(NoSQLType.DATABASE_TYPE_PROPERTY);
        assertThat(NoSQLType.get()).isEqualTo(NoSQLType.KEY_VALUE);
    }
}