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
package team.unnamed.commandflow.annotated;

import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.Usage;
import team.unnamed.commandflow.annotated.part.PartInjector;
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

        List<team.unnamed.commandflow.command.Command> commands = builder.fromClass(new TestCommand());
        assertEquals(1, commands.size(), "Expected one command only");
        team.unnamed.commandflow.command.Command command = commands.get(0);
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
