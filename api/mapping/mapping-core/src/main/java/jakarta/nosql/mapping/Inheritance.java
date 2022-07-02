/*
 * Copyright (c) 2022 Otavio Santana and others
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
package jakarta.nosql.mapping;


import javax.enterprise.inject.Stereotype;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the inheritance strategy to be used for an entity class hierarchy.
 * It is specified on the entity class that is the root of the entity class hierarchy.
 *
 * This class can be either a regular or an abstract;
 * The table/column-family/document-collection will have a column for every attribute
 * of every class in the hierarchy.
 * The subclass will use the {@link Entity} name from that class with this annotation.
 *
 * <pre>
 *
 *   Example:
 *   &#064;Entity
 *   &#064;Inheritance
 *   public class Notification { ... }
 *
 *   &#064;Entity
 *   public class SMSNotification extends Notification { ... }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Stereotype
public @interface Inheritance {
}
