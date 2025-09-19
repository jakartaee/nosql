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
package jakarta.nosql;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated {@code record} class is a projection used to map query results.
 *
 * <p>This annotation allows mapping query results directly into record classes without requiring full entity loading.
 * You do not need to explicitly list the fields in the query if they match the record structure.</p>
 *
 * <p>You may omit the {@code FROM} clause entirely in the query string if the {@code from()} attribute is set.
 * In such cases, Jakarta NoSQL will automatically use the declared class as the source entity.</p>
 *
 * <p>If both the query string and the annotation specify a source entity, the entity name in the query string takes precedence.</p>
 *
 * <p>Example using implicit {@code from} via the annotation:</p>
 *
 * <pre>{@code
 * @Projection(from = Person.class)
 * public record PersonSummary(String name, int age) {}
 *
 * List<PersonSummary> results = template
 *     .typedQuery("SELECT name, age", PersonSummary.class)
 *     .result();
 * }</pre>
 *
 * <p>Example with an explicit {@code FROM} clause (annotation still helps with mapping):</p>
 *
 * <pre>{@code
 * @Projection
 * public record PersonSummary(String name, int age) {}
 *
 * List<PersonSummary> results = template
 *     .typedQuery("SELECT name, age FROM Person", PersonSummary.class)
 *     .result();
 * }</pre>
 *
 * @see jakarta.nosql.TypedQuery
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Projection {

    Class<?> from() default void.class;
}
