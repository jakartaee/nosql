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

@DisplayName("ABS function for numeric fields")
public class FunctionAbsTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using ABS function on positive numbers")
    void shouldSelectUsingAbsFunctionPositive(List<Word> entities) {
        entities.forEach(template::insert);

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.abs("score"))
                    .gt(50)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word -> Math.abs(word.getScore()) > 50);

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using ABS function with negative numbers")
    void shouldSelectUsingAbsFunctionNegative(List<Word> entities) {
        Word w1 = new Word("test1", "meaning1", "en");
        w1.setScore(-100);
        Word w2 = new Word("test2", "meaning2", "en");
        w2.setScore(-50);

        template.insert(w1);
        template.insert(w2);

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.abs("score"))
                    .eq(100)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word -> Math.abs(word.getScore()) == 100);

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }
}