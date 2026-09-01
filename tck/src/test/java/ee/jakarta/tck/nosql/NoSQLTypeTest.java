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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class NoSQLTypeTest {

    @AfterEach
    void clearDatabaseTypeProperty() {
        System.clearProperty(NoSQLType.DATABASE_TYPE_PROPERTY);
    }

    @Nested
    @DisplayName("When reading database type flexibility")
    class WhenTheFlexibilityIsRead {

        @ParameterizedTest(name = "{0} should have flexibility level {1}")
        @CsvSource({
                "KEY_VALUE, 1",
                "COLUMN, 2",
                "DOCUMENT, 3",
                "GRAPH, 4",
                "OTHER, 0"
        })
        @DisplayName("Should return the configured level")
        void shouldReturnTheConfiguredLevel(String type, int expectedFlexibility) {

            // Given
            var noSQLType = NoSQLType.valueOf(type);

            // When
            var flexibility = noSQLType.getFlexibility();

            // Then
            assertThat(flexibility)
                    .as("flexibility level for %s", type)
                    .isEqualTo(expectedFlexibility);
        }
    }

    @Nested
    @DisplayName("When resolving a database type by name")
    class WhenTheTypeIsResolvedByName {

        @ParameterizedTest(name = "{0} should resolve to {1}")
        @CsvSource({
                "COLUMN, COLUMN",
                "document, DOCUMENT",
                "Graph, GRAPH",
                "KEY_VALUE, KEY_VALUE",
                "OTHER, OTHER"
        })
        @DisplayName("Should return the matching type")
        void shouldReturnTheMatchingType(String input, NoSQLType expectedType) {

            // When
            var type = NoSQLType.get(input);

            // Then
            assertThat(type)
                    .as("database type resolved from %s", input)
                    .isEqualTo(expectedType);
        }

        @ParameterizedTest(name = "Invalid value \"{0}\" should resolve to KEY_VALUE")
        @ValueSource(strings = {"INVALID_TYPE", "UNKNOWN", ""})
        @DisplayName("Should return the default type when the name is invalid")
        void shouldReturnTheDefaultTypeWhenTheNameIsInvalid(String input) {

            // When
            var type = NoSQLType.get(input);

            // Then
            assertThat(type)
                    .as("default database type for invalid name")
                    .isEqualTo(NoSQLType.KEY_VALUE);
        }

        @Test
        @DisplayName("Should reject a null name")
        void shouldRejectANullName() {

            // When / Then
            assertThatNullPointerException()
                    .as("null database type name")
                    .isThrownBy(() -> NoSQLType.get(null));
        }
    }

    @Nested
    @DisplayName("When resolving a database type from the system property")
    class WhenTheTypeIsResolvedFromTheSystemProperty {

        @Test
        @DisplayName("Should return the configured type")
        void shouldReturnTheConfiguredType() {

            // Given
            System.setProperty(NoSQLType.DATABASE_TYPE_PROPERTY, "COLUMN");

            // When
            var type = NoSQLType.get();

            // Then
            assertThat(type)
                    .as("database type from the system property")
                    .isEqualTo(NoSQLType.COLUMN);
        }

        @Test
        @DisplayName("Should return the default type when the configured value is invalid")
        void shouldReturnTheDefaultTypeWhenTheConfiguredValueIsInvalid() {

            // Given
            System.setProperty(NoSQLType.DATABASE_TYPE_PROPERTY, "INVALID_TYPE");

            // When
            var type = NoSQLType.get();

            // Then
            assertThat(type)
                    .as("default database type for an invalid system property")
                    .isEqualTo(NoSQLType.KEY_VALUE);
        }

        @Test
        @DisplayName("Should return the default type when the property is absent")
        void shouldReturnTheDefaultTypeWhenThePropertyIsAbsent() {

            // When
            var type = NoSQLType.get();

            // Then
            assertThat(type)
                    .as("default database type without a system property")
                    .isEqualTo(NoSQLType.KEY_VALUE);
        }
    }
}