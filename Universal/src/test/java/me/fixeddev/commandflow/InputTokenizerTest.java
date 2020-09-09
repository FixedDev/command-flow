package me.fixeddev.commandflow;

import me.fixeddev.commandflow.input.InputTokenizer;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InputTokenizerTest {

    @Test
    public void testQuotedSpaceTokenizer() {

        InputTokenizer tokenizer = new QuotedSpaceTokenizer();
        List<String> tokens = tokenizer.tokenize(
                "command subcommand \"single ' quote\" 'double \" quote' other 'nonclosed non\"closed"
        );

        for (String token : tokens) {
            System.out.println(token);
        }

    }

}
