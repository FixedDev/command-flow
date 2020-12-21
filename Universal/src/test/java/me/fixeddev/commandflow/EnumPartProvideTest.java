package me.fixeddev.commandflow;

import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnumPartProvideTest {

    @Test
    public void testProvider() {
        AnnotatedCommandTreeBuilder builder = AnnotatedCommandTreeBuilder.create(PartInjector.create());
        CommandManager commandManager = new SimpleCommandManager();

        commandManager.registerCommands(builder.fromClass(new TestCommandClass()));
        commandManager.execute(Namespace.create(), "test foo bar baz");
    }

    public static class TestCommandClass implements CommandClass {

        @Command(names = "test")
        public void execute(FooEnum val1, FooEnum val2, FooEnum val3) {
            Assertions.assertEquals(FooEnum.FOO, val1);
            Assertions.assertEquals(FooEnum.BAR, val2);
            Assertions.assertEquals(FooEnum.BAZ, val3);
        }

    }

    public enum FooEnum {
        FOO,
        BAR,
        BAZ
    }

}
