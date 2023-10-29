package me.fixeddev.commandflow.annotated;

import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Usage;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomComponentParserTest {
    @Test
    void test() {
        PartInjector partInjector = PartInjector.create();
        AnnotatedCommandTreeBuilder builder = AnnotatedCommandTreeBuilder.create(partInjector);

        // a component parser that always creates plain text components with "Hello " as the prefix
        builder.setComponentParser(string -> Component.text("Hello, " + string.toLowerCase(Locale.ROOT)));

        List<me.fixeddev.commandflow.command.Command> commands = builder.fromClass(new TestCommand());
        assertEquals(1, commands.size(), "Expected one command only");
        me.fixeddev.commandflow.command.Command command = commands.get(0);
        assertEquals("test", command.getName());
        assertEquals("admin", command.getPermission());
        assertEquals(Component.text("Hello, you don't have permission to do this!"), command.getPermissionMessage());
        assertEquals(Component.text("Hello, this is the usage"), command.getUsage());
    }

    @Command(names = "test", permissionMessage = "You don't have permission to do this!", permission = "admin")
    @Usage("This is the usage")
    public static class TestCommand implements CommandClass {
        @Command(names = "")
        void run() {
            System.out.println("Hello world!");
        }
    }
}
