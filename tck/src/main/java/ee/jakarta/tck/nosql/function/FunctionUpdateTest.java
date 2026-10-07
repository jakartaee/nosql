/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
package ee.jakarta.tck.nosql.function;

import ee.jakarta.tck.nosql.AbstractTemplateTest;
import ee.jakarta.tck.nosql.entities.Word;
import ee.jakarta.tck.nosql.factories.WordListSupplier;
import jakarta.nosql.Function;
import jakarta.nosql.UnsupportedFunctionException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

@DisplayName("UPDATE queries using Function expressions")
public class FunctionUpdateTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should update using UPPER function in WHERE clause")
    void shouldUpdateUsingUpperFunction(List<Word> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            template.update(Word.class)
                    .set("language").to("english")
                    .where(Function.upper("language"))
                    .eq("EN")
                    .execute();

            List<Word> updated = template.select(Word.class)
                    .where("language")
                    .eq("english")
                    .result();

            Assertions.assertThat(updated)
                    .isNotEmpty()
                    .allMatch(word -> "english".equals(word.getLanguage()));

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }
}