/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * From Jupiter internal API.
 */
final class AnnotationUtils {

    private AnnotationUtils() {
    }

    static <A extends Annotation> Optional<A> findAnnotation(Optional<? extends AnnotatedElement> element,
                                                                    Class<A> annotationType) {

        if (element == null || !element.isPresent()) {
            return Optional.empty();
        }

        return findAnnotation(element.get(), annotationType);
    }

    static <A extends Annotation> Optional<A> findAnnotation(AnnotatedElement element, Class<A> annotationType) {
        Preconditions.notNull(annotationType, "annotationType must not be null");
        boolean inherited = annotationType.isAnnotationPresent(Inherited.class);
        return findAnnotation(element, annotationType, inherited, new HashSet<>());
    }

    private static <A extends Annotation> Optional<A> findAnnotation(AnnotatedElement element, Class<A> annotationType,
                                                                     boolean inherited, Set<Annotation> visited) {

        Preconditions.notNull(annotationType, "annotationType must not be null");

        if (element == null) {
            return Optional.empty();
        }

        // Directly present?
        A annotation = element.getDeclaredAnnotation(annotationType);
        if (annotation != null) {
            return Optional.of(annotation);
        }

        // Meta-present on directly present annotations?
        Optional<A> directMetaAnnotation = findMetaAnnotation(annotationType, element.getDeclaredAnnotations(),
                inherited, visited);
        if (directMetaAnnotation.isPresent()) {
            return directMetaAnnotation;
        }

        if (element instanceof Class) {
            Class<?> type = (Class<?>) element;

            // Search on interfaces
            for (Class<?> ifc : type.getInterfaces()) {
                if (ifc != Annotation.class) {
                    Optional<A> annotationOnInterface = findAnnotation(ifc, annotationType, inherited, visited);
                    if (annotationOnInterface.isPresent()) {
                        return annotationOnInterface;
                    }
                }
            }

            // Indirectly present?
            // Search in class hierarchy
            if (inherited) {
                Class<?> superclass = type.getSuperclass();
                if (superclass != null && superclass != Object.class) {
                    Optional<A> annotationOnSuperclass = findAnnotation(superclass, annotationType, inherited, visited);
                    if (annotationOnSuperclass.isPresent()) {
                        return annotationOnSuperclass;
                    }
                }
            }
        }

        // Meta-present on indirectly present annotations?
        return findMetaAnnotation(annotationType, element.getAnnotations(), inherited, visited);
    }

    private static <A extends Annotation> Optional<A> findMetaAnnotation(Class<A> annotationType,
                                                                         Annotation[] candidates, boolean inherited, Set<Annotation> visited) {

        for (Annotation candidateAnnotation : candidates) {
            Class<? extends Annotation> candidateAnnotationType = candidateAnnotation.annotationType();
            if (!isInJavaLangAnnotationPackage(candidateAnnotationType) && visited.add(candidateAnnotation)) {
                Optional<A> metaAnnotation = findAnnotation(candidateAnnotationType, annotationType, inherited,
                        visited);
                if (metaAnnotation.isPresent()) {
                    return metaAnnotation;
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isInJavaLangAnnotationPackage(Class<? extends Annotation> annotationType) {
        return (annotationType != null && annotationType.getName().startsWith("java.lang.annotation"));
    }


}
