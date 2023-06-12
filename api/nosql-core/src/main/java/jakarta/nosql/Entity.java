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
package jakarta.nosql;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is an entity.
 * <p>
 * You can include one or multiple entities if the Database supports it.
 * </p>
 * <p>
 * The sample below shows two entities, Person and Address, where a person has an address:
 * </p>
 * <pre>{@code
 * @Entity
 * public class Person {
 *
 *     @Id
 *     private Long id;
 *
 *     @Column
 *     private String name;
 *
 *     @Column
 *     private Address address;
 * }
 *
 * @Entity
 * public class Address {
 *     @Column
 *     private String street;
 *
 *     @Column
 *     private String city;
 * }
 * }</pre>
 * <p>
 * However, it’s essential to remember that NoSQL databases have varying behaviors then the serialization method may differ depending on the NoSQL vendor.
 * For instance, in a Document database, these entities may be converted into a sub-document, while on a Key-value, it will be the value:
 * </p>
 * <pre>{@code
 * {
 *     "_id":10,
 *     "name":"Ada Lovelave",
 *     "address":{
 *          "city":"São Paulo",
 *          "street":"Av Nove de Julho"
 *     }
 * }
 * }</pre>
 *
 * @see Id
 * @see Column
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    /**
     * The name of an entity. The default value is the unqualified simple name of the class.
     * <p>
     * For example, given the {@code org.jakarta.nosql.demo.Person} class, the default name will be {@code Person}:
     *
     * <pre>{@code
     *     @Entity
     *     public class Person {
     *     }
     * }</pre>
     * <p>
     * <p>
     * In the case of name customization, it just needs to set the value of the @Entity annotation with the desired name as like below:
     *
     * <pre>{@code
     *     @Entity("ThePerson")
     *     public class Person {
     *     }
     * }</pre>
     *
     * @return the entity name (Optional)
     */
    String value() default "";
}
