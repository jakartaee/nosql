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

@DisplayName("SELECT queries using Function expressions")
public class FunctionSelectTest extends AbstractTemplateTest {

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using LEFT function")
    void shouldSelectUsingLeftFunction(List<Word> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.left("term", 2))
                    .eq("Ja")
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word -> word.getTerm().startsWith("Ja"));

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using RIGHT function")
    void shouldSelectUsingRightFunction(List<Word> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.right("term", 2))
                    .eq("pt")
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word -> word.getTerm().endsWith("pt"));

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using UPPER function")
    void shouldSelectUsingUpperFunction(List<Word> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.upper("meaning"))
                    .eq("COFFEE")
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word -> "coffee".equalsIgnoreCase(word.getMeaning()));

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using LOWER function")
    void shouldSelectUsingLowerFunction(List<Word> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.lower("term"))
                    .eq("java")
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word -> "java".equalsIgnoreCase(word.getTerm()));

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using LENGTH function")
    void shouldSelectUsingLengthFunction(List<Word> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.length("term"))
                    .gt(5)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word -> word.getTerm().length() > 5);

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using complex query with functions and AND")
    void shouldSelectUsingFunctionWithAnd(List<Word> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.upper("language"))
                    .eq("EN")
                    .and(Function.length("term"))
                    .gt(4)
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word ->
                            "en".equalsIgnoreCase(word.getLanguage()) &&
                                    word.getTerm().length() > 4
                    );

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(WordListSupplier.class)
    @DisplayName("Should select using complex query with functions and OR")
    void shouldSelectUsingFunctionWithOr(List<Word> entities) {
        entities.forEach(entity -> template.insert(entity));

        try {
            List<Word> result = template.select(Word.class)
                    .where(Function.upper("language"))
                    .eq("EN")
                    .or(Function.upper("language"))
                    .eq("PT")
                    .result();

            Assertions.assertThat(result)
                    .isNotEmpty()
                    .allMatch(word ->
                            "en".equalsIgnoreCase(word.getLanguage()) ||
                                    "pt".equalsIgnoreCase(word.getLanguage())
                    );

        } catch (UnsupportedFunctionException exp) {
            Assertions.assertThat(exp).isInstanceOf(UnsupportedFunctionException.class);
        }
    }
}