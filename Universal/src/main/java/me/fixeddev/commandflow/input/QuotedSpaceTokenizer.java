package me.fixeddev.commandflow.input;

import java.util.ArrayList;
import java.util.List;

public class QuotedSpaceTokenizer implements InputTokenizer {
    @Override
    public List<String> tokenize(String line) {
        List<String> inputTokens = new ArrayList<>();

        StringBuilder token = new StringBuilder();

        boolean quoted = false;
        char quoteType = ' ';
        boolean escaped = false;
        for (int i = 0; i < line.length(); i++) {
            char charAt = line.charAt(i);

            if (charAt == '\\') {
                escaped = true;
                continue;
            }

            if ((charAt == '"' || charAt == '\'') && !escaped) {
                if (quoted && quoteType != charAt) {
                    continue;
                }

                quoteType = charAt;
                quoted = !quoted;
                escaped = false;
                continue;
            }

            escaped = false;

            if (charAt == ' ' && !quoted) {
                String tokenStr = token.toString();

                if (!tokenStr.trim().isEmpty()) {
                    inputTokens.add(tokenStr);
                    token = new StringBuilder();
                }

                continue;
            }

            token.append(charAt);
        }

        return inputTokens;
    }
}
