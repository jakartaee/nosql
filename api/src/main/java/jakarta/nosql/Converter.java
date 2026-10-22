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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares that the annotated class is a converter.
 *
 * <p>The {@code autoApply} element specifies whether the converter
 * is automatically applied to all mapped attributes of the target type. If {@code autoApply = true}, the persistence
 * provider must automatically apply the converter to every mapped attribute of the converted type, except where
 * conversion is explicitly overridden.</p>
 *
 * <p>Every converter class must implement
 * {@link AttributeConverter}.</p>
 *
 * <p>Example:</p>
 *
 * <pre>{@code
 * @Converter(autoApply = true)
 * public class EmailConverter
 *         implements AttributeConverter<Email, String> {
 *
 *     @Override
 *     public String toDatabase(Email attribute) {
 *         return attribute == null ? null : attribute.value();
 *     }
 *
 *     @Override
 *     public Email toEntityAttribute(String value) {
 *         return value == null ? null : new Email(value);
 *     }
 * }
 *
 * @Entity
 * public class Customer {
 *
 *     @Id
 *     private Long id;
 *
 *     // EmailConverter is applied automatically
 *     private Email email;
 * }
 * }</pre>
 * Note: Converter implementations should be stateless.
 * @since 1.1
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Converter {

    /**
     * Specifies whether the annotated converter should be automatically applied to attributes of the target type.
     *
     * <p>If {@code true}, the converter is automatically applied
     * to all mapped attributes of the converted type unless explicitly overridden.</p>
     *
     * <pre>{@code
     * @Converter(autoApply = true)
     * public class UUIDConverter
     *         implements AttributeConverter<UUID, String> {
     *     ...
     * }
     *
     * @Entity
     * public class Person {
     *
     *     @Id
     *     private Long id;
     *
     *     // UUIDConverter is applied automatically
     *     private UUID identifier;
     * }
     * }</pre>
     *
     * <p>If {@code false}, the converter is applied only when
     * explicitly selected using {@link Convert}.</p>
     *
     * <pre>{@code
     * @Converter(autoApply = false)
     * public class UUIDConverter
     *         implements AttributeConverter<UUID, String> {
     *     ...
     * }
     *
     * @Entity
     * public class Person {
     *
     *     @Id
     *     private Long id;
     *
     *     @Convert(UUIDConverter.class)
     *     private UUID identifier;
     * }
     * }</pre>
     *
     * @return whether the converter should be automatically applied
     */
    boolean autoApply() default false;
}
