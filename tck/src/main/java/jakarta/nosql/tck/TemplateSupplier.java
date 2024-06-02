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
package jakarta.nosql.tck;

import jakarta.nosql.Template;

import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * The {@code TemplateSupplier} interface provides a mechanism for obtaining instances of {@link Template}.
 * {@link Template} is a utility class designed to enhance productivity by simplifying common NoSQL operations.
 *
 * <p>The {@code TemplateSupplier} abstracts the complexity of database configuration and dependency injection,
 * facilitating seamless interaction with NoSQL databases, particularly schema-based databases.</p>
 *
 * <p>Upon creation of a {@code TemplateSupplier} instance, the Jakarta NoSQL provider is responsible for:
 * <ul>
 *   <li>Setting up the database credentials</li>
 *   <li>Initializing the database</li>
 *   <li>Configuring the dependency injection mechanism</li>
 * </ul>
 * Although Jakarta CDI (Contexts and Dependency Injection) is recommended as the dependency injection engine,
 * its specific implementation is beyond the scope of the Jakarta NoSQL specification. The specification primarily
 * focuses on configuration and management of database resources.</p>
 *
 * <p>Once the {@code TemplateSupplier} is instantiated and the {@link #get()} method is invoked, it is expected that the
 * returned {@link Template} instance is fully configured and ready for interaction with the database.</p>
 *
 * <p>The Jakarta NoSQL provider must implement this interface as part of the Service Provider Interface (SPI)
 * to define the creation and configuration of the {@link Template}.</p>
 *
 * <pre>
 * {@code
 * public interface TemplateSupplier extends Supplier<Template> {
 *
 *     static TemplateSupplier template() {
 *         ServiceLoader<TemplateSupplier> loader = ServiceLoader.load(TemplateSupplier.class);
 *         return loader.findFirst()
 *                       .orElseThrow(() -> new IllegalStateException("The Template instance was not found"));
 *     }
 * }
 * }
 * </pre>
 *
 * @see Template
 * @see jakarta.nosql
 * @since 1.0
 */
public interface TemplateSupplier extends Supplier<Template> {

    /**
     * Retrieves an instance of {@code TemplateSupplier} using the {@link ServiceLoader} mechanism.
     *
     * @return an instance of {@code TemplateSupplier}
     * @throws IllegalStateException if no {@code TemplateSupplier} implementation is found
     */
    static TemplateSupplier template() {
        ServiceLoader<TemplateSupplier> loader = ServiceLoader.load(TemplateSupplier.class);
        return loader.findFirst().orElseThrow(() -> new IllegalStateException("The Template instance was not found"));
    }
}
