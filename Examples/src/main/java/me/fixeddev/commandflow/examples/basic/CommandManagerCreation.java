package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;

public class CommandManagerCreation {

    public static void main(String[] args) {
        // This is how we create a command manager, simple isn't it?
        // We can set other properties of the command manager, like the authorizer,
        // the tokenizer, the executor, etc.
        CommandManager commandManager = new SimpleCommandManager();

        // We change the tokenizer to take as 1 argument the quoted strings
        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());
    }

}
