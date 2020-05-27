/*
 *  Copyright (c) 2020 Otavio Santana and others
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v. 2.0 which is available at
 *  http://www.eclipse.org/legal/epl-2.0.
 *
 *  This Source Code may also be made available under the following Secondary
 *  Licenses when the conditions for such availability set forth in the Eclipse
 *  Public License v. 2.0 are satisfied: GNU General Public License v2.0
 *  w/Classpath exception which is available at
 *  https://www.gnu.org/software/classpath/license.html.
 *
 *  SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.communication.driver.column;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ColumnArgumentProvider implements ArgumentsProvider, AnnotationConsumer<ColumnSource> {

    private static final Logger LOGGER = Logger.getLogger(ColumnArgumentProvider.class.getName());

    private String value;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        final Map<String, String> map = get();
        if (map.isEmpty()) {
            return Stream.of(Arguments.of(ColumnArgument.EMPTY));
        }
        final String idName = map.get("id.name");
        final List<String> query = map.entrySet().stream()
                .filter(s -> s.getKey().startsWith("query"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return Stream.of(Arguments.of(new ColumnArgument(query, idName)));
    }


    public Map<String, String> get() {
        try {
            Properties properties = new Properties();
            InputStream stream = ColumnArgumentProvider.class.getClassLoader()
                    .getResourceAsStream(value);
            if (Objects.nonNull(stream)) {
                properties.load(stream);
                return properties.keySet().stream().collect(Collectors
                        .toMap(Object::toString, s -> properties.get(s).toString()));
            } else {
                LOGGER.info("Does not find " + value + " as resource, returning an empty configuration");
                return Collections.emptyMap();
            }

        } catch (IOException e) {
            LOGGER.fine("There is an issue when read the file: " + value);
            return Collections.emptyMap();
        }
    }

    @Override
    public void accept(ColumnSource columnSource) {
        this.value = columnSource.value();
    }
}
