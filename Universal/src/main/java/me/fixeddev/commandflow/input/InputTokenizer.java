package me.fixeddev.commandflow.input;

import java.util.List;

public interface InputTokenizer {
    /**
     * Converts the given input {@link String} into a list of tokens
     *
     * @param line The string to tokenize
     * @return A modifiable list of tokens for the given String
     */
    List<String> tokenize(String line);
}
