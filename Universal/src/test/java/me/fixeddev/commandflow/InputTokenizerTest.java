package me.fixeddev.commandflow;

import me.fixeddev.commandflow.input.InputTokenizer;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;
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
