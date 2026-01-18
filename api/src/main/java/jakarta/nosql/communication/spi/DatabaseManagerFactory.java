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
package jakarta.nosql.communication.spi;

/**
 * Factory responsible for creating {@link DatabaseManager} instances based on
 * a logical database identifier.
 *
 * <p>The interpretation of the {@code databaseName} parameter is entirely
 * provider-defined. It typically represents a logical name, alias, or configuration
 * key resolved by the underlying NoSQL provider. The specification intentionally
 * does not mandate how this value is mapped to physical databases, clusters,
 * or endpoints.</p>
 *
 * <p>The lifecycle semantics of this factory are also provider-specific. While
 * this type implements {@link AutoCloseable}, providers may associate different
 * behaviors with the {@link #close()} operation. Some providers may release
 * shared resources, invalidate previously created managers, or perform cleanup
 * operations, while others may treat this method as a no-op.</p>
 *
 * <p>Applications should not assume that invoking {@link #close()} has a direct
 * impact on existing {@link DatabaseManager} instances, nor should they rely on
 * any specific resource management strategy. Such concerns are considered an
 * implementation detail of the provider.</p>
 *
 * <p>This factory is intended to act as an infrastructure-level entry point and
 * does not define or expose any configuration model. All configuration aspects,
 * including connection handling, authentication, pooling, and resource management,
 * are delegated to the provider.</p>
 */
public interface DatabaseManagerFactory extends AutoCloseable {

    DatabaseManager create(String databaseName);

    @Override
    void close();
}
