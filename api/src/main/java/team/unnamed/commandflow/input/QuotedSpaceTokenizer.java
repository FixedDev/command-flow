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
package team.unnamed.commandflow.input;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yushu@unnamed.team (Yushu)
 */
public class QuotedSpaceTokenizer implements InputTokenizer {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        tokenizeIn(line, token, tokens, false);
        return tokens;
    }

    private void tokenizeIn(String text, StringBuilder tokenBuilder, List<String> tokens, boolean isRecursiveCall) {
        char[] characters = text.toCharArray();

        for (int i = 0; i < characters.length; i++) {

            char charAt = characters[i];

            if (charAt == ' ') {
                appendTokenIfValid(tokenBuilder, tokens);
                continue;
            }

            if (charAt != '"' && charAt != '\'') {
                tokenBuilder.append(charAt);
                continue;
            }

            String previous = tokenBuilder.toString();
            tokenBuilder.setLength(0);
            boolean closed = false;

            while (++i < characters.length) {

                char current = characters[i];

                if (current == charAt) {
                    closed = true;
                    break;
                }

                tokenBuilder.append(current);
            }

            String tokenString = tokenBuilder.toString();
            tokenBuilder.setLength(0);

            if (closed) {
                if (!previous.isEmpty()) {
                    tokens.add(previous);
                }
                tokens.add(tokenString);
            } else {
                tokenBuilder.append(previous);
                tokenBuilder.append(charAt);
                tokenizeIn(tokenString, tokenBuilder, tokens, true);
                if (!isRecursiveCall) {
                    appendTokenIfValid(tokenBuilder, tokens);
                }
            }
        }

        // append the reminder
        appendTokenIfValid(tokenBuilder, tokens);
    }

    private void appendTokenIfValid(StringBuilder tokenBuilder, List<String> tokenList) {
        String tokenString = tokenBuilder.toString();
        if (!tokenString.isEmpty()) {
            tokenList.add(tokenString);
            tokenBuilder.setLength(0);
        }
    }

}
