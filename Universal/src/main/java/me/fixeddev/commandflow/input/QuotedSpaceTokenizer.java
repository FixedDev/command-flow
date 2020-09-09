package me.fixeddev.commandflow.input;

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
                    tokens.add(tokenBuilder.toString());
                }
            }
        }
    }

    private void appendTokenIfValid(StringBuilder tokenBuilder, List<String> tokenList) {
        String tokenString = tokenBuilder.toString();
        if (!tokenString.isEmpty()) {
            tokenList.add(tokenString);
            tokenBuilder.setLength(0);
        }
    }

}
