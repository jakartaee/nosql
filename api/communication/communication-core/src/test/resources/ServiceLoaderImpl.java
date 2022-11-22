/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.glassfish.hk2.osgiresourcelocator;

/**
 * Stub implementation of the HK2 ServiceLoader that uses {@link java.util.ServiceLoader},
 * simulating the state of the class being present and initialized by OSGi.
 */
public class ServiceLoader {
	public static <T> Iterable<? extends T> lookupProviderInstances(Class<T> serviceClass) {
		return java.util.ServiceLoader.load(serviceClass);
	}
}