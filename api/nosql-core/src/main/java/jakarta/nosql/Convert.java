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


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies how the values of a field or property are converted to a basic type or a type that can be persisted by a persistence provider.
 *
 * <p>The {@code Convert} annotation may be applied to any field or property and must be used in conjunction with the
 * {@link Id} or {@link Column} annotation (if not, a default value will be used).
 *
 * <p>The dot notation may also be used with map entries:
 * <p>Convert a basic attribute
 * {@snippet :
 * @Converter
 * public class BooleanToIntegerConverter
 *         implements AttributeConverter<Boolean, Integer> {  ... }
 *
 * @Entity
 * public class Employee {
 *     @Id
 *     long id;
 *
 *     @Convert(converter = BooleanToIntegerConverter.class)
 *     boolean fullTime;
 * }
 * }
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Convert {
    /**
     * Specifies the converter to be applied.
     */
    Class<? extends AttributeConverter<?, ?>> value();
}
