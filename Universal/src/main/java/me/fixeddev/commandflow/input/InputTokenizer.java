package me.fixeddev.commandflow.input;

import java.util.List;

public interface InputTokenizer {
    List<String> tokenize(String line);
}
