/*
 * Copyright (c) 2020 Otavio Santana and others
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.test;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import java.util.function.Supplier;
import java.util.stream.Stream;

class ContainerSupplier implements Supplier<SeContainer> {

    private final CDIExtension config;

    ContainerSupplier(CDIExtension config) {
        this.config = config;
    }

    @Override
    public SeContainer get() {
        final SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        if (config.disableDiscovery()) {
            initializer.disableDiscovery();
        }
        initializer.setClassLoader(Thread.currentThread().getContextClassLoader());
        initializer.addBeanClasses(config.classes());
        initializer.enableDecorators(config.decorators());
        initializer.enableInterceptors(config.interceptors());
        initializer.selectAlternatives(config.alternatives());
        initializer.selectAlternativeStereotypes(config.alternativeStereotypes());
        initializer.addPackages(getPackages(config.packages()));
        initializer.addPackages(true, getPackages(config.recursivePackages()));
        return initializer.initialize();
    }

    private Package[] getPackages(Class<?>[] packages) {
        return Stream.of(packages).map(Class::getPackage).toArray(Package[]::new);
    }


}
