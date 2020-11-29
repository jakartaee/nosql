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

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.AnnotationUtils;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.InjectionTarget;
import java.util.function.Consumer;
import java.util.function.Supplier;

class CDIJUnitExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private SeContainer container;
    private CreationalContext<Object> context;

    @Override
    public void beforeAll(final ExtensionContext extensionContext) {
        final CDIExtension config = AnnotationUtils.findAnnotation(extensionContext.getElement(), CDIExtension.class)
                .orElse(null);
        if (config == null) {
            return;
        }
        Supplier<SeContainer> supplier = new ContainerSupplier(config);
        container = supplier.get();
    }

    @Override
    public void afterAll(final ExtensionContext extensionContext) {
        if (container != null) {
            doClose(container);
            container = null;
        }
    }

    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        if (container == null) {
            return;
        }
        extensionContext.getTestInstance().ifPresent(inject());
    }

    private Consumer<Object> inject() {
        return instance ->  {
            final BeanManager manager = container.getBeanManager();
            final AnnotatedType<?> annotatedType = manager.createAnnotatedType(instance.getClass());
            final InjectionTarget injectionTarget = manager.createInjectionTarget(annotatedType);
            context = manager.createCreationalContext(null);
            injectionTarget.inject(instance, context);
        };
    }

    @Override
    public void afterEach(final ExtensionContext extensionContext) {
        if (context != null) {
            context.release();
            context = null;
        }
    }

    private void doClose(final SeContainer container) {
        container.close();
    }
}