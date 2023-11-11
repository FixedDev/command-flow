/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.commandflow;

import team.unnamed.commandflow.input.InputTokenizer;
import team.unnamed.commandflow.input.QuotedSpaceTokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InputTokenizerTest {

    @Test
    public void testQuotedSpaceTokenizer() {

        InputTokenizer tokenizer = new QuotedSpaceTokenizer();
        List<String> tokens = tokenizer.tokenize(
                "command subcommand \"single ' quote\" 'double \" quote' other 'nonclosed non\"closed"
        );

        Assertions.assertEquals(7, tokens.size());
        Assertions.assertEquals("command", tokens.get(0));
        Assertions.assertEquals("subcommand", tokens.get(1));
        Assertions.assertEquals("single ' quote", tokens.get(2));
        Assertions.assertEquals("double \" quote", tokens.get(3));
        Assertions.assertEquals("other", tokens.get(4));
        Assertions.assertEquals("'nonclosed", tokens.get(5));
        Assertions.assertEquals("non\"closed", tokens.get(6));

    }

}
