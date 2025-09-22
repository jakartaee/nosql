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
package jakarta.nosql;

/**
 * A class that implements this interface can be used to convert entity attribute state into a database column
 * representation and vice versa. Note that the types X and Y may be the same Java type.
 *
 * <p>This interface is implemented by custom attribute converters. A converter is a class whose methods convert
 * between the target type of the converter (an arbitrary Java type which may be used as the type of a persistent
 * field or property) and a basic type or a type that can be persisted by a persistence provider, used as an
 * intermediate step in mapping to the database representation.
 *
 * <p>A converted field or property is considered basic, since, with the aid of the converter, its values can be
 * represented as instances of a basic type.
 *
 * <p>Note that the target type {@code X} and the converted basic type {@code Y} may be the same Java type.
 *
 * <p>Example: Using a record {@code Money} value object with a custom converter</p>
 * <pre>{@code
 * public record Money(Currency currency, BigDecimal amount) {
 *
 *     @Override
 *     public String toString() {
 *         return currency.getCurrencyCode() + " " + amount;
 *     }
 *
 *     public static Money fromString(String value) {
 *         String[] parts = value.split(" ");
 *         return new Money(Currency.getInstance(parts[0]), new BigDecimal(parts[1]));
 *     }
 * }
 * }</pre>
 *
 * <p>Converter implementation:</p>
 *
 * <pre>{@code
 * public class MoneyConverter implements AttributeConverter<Money, String> {
 *
 *     @Override
 *     public String convertToDatabaseColumn(Money attribute) {
 *         return attribute == null ? null : attribute.toString();
 *     }
 *
 *     @Override
 *     public Money convertToEntityAttribute(String dbData) {
 *         return dbData == null ? null : Money.fromString(dbData);
 *     }
 * }
 * }</pre>
 *
 * <p>Example usage in an entity:</p>
 *
 * <pre>{@code
 * @Entity
 * public class Product {
 *
 *     @Id
 *     private String id;
 *
 *     @Column
 *     private String name;
 *
 *     @Column
 *     @Convert(MoneyConverter.class)
 *     private Money price;
 * }
 * }</pre>
 *
 * <p>The representation stored in the database (depending on the NoSQL backend) may resemble:</p>
 *
 * <pre>{@code
 * {
 *   "id": "P-001",
 *   "name": "Laptop",
 *   "price": "USD 1499.00"
 * }
 * }</pre>
 *
 * @param <X> the target type, that is, the type of the entity attribute
 * @param <Y> a basic type or a supported type in the NoSQL database representing the type of the database column
 * @see Convert
 * @since 1.0.0
 */
public interface AttributeConverter<X, Y> {

    /**
     * Converts the value stored in the entity attribute into the data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database column
     */
    Y convertToDatabaseColumn(X attribute);

    /**
     * Converts the data stored in the database column into the value to be stored in the entity attribute.
     * Note that it is the responsibility of the converter writer to specify the correct database data type for the
     * corresponding column for use by the NoSQL database: i.e., persistence providers are not expected to do
     * such type conversion.
     *
     * @param dbData the data from the database column to be converted
     * @return the converted value to be stored in the entity attribute
     */
    X convertToEntityAttribute(Y dbData);
}
