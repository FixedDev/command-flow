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
package team.unnamed.commandflow;

import team.unnamed.commandflow.annotated.AnnotatedCommandTreeBuilder;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.part.PartInjector;
import team.unnamed.commandflow.annotated.part.defaults.DefaultsModule;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParameterizedPartTest {

    @Test
    public void testParameterizedPart() {
        CommandManager commandManager = new SimpleCommandManager();

        PartInjector partInjector = PartInjector.create();
        partInjector.install(new DefaultsModule());
        partInjector.install(binder -> binder.bindFactory(StringSet.class.getGenericInterfaces()[0], (name, modifiers) -> new ArgumentPart() {

            @Override
            public List<?> parseValue(CommandContext context,
                                      ArgumentStack stack,
                                      CommandPart caller) throws ArgumentParseException {
                Set<String> arguments = new StringSet();

                while (stack.hasNext()) {
                    arguments.add(stack.next());
                }

                return Collections.singletonList(arguments);
            }

            @Override
            public String getName() {
                return name;
            }
        }));

        AnnotatedCommandTreeBuilder treeBuilder = AnnotatedCommandTreeBuilder.create(partInjector);

        commandManager.registerCommands(treeBuilder.fromClass(new TestCommand()));

        commandManager.execute(Namespace.create(), "test 1 Sex sex sex");
        commandManager.execute(Namespace.create(), "test 2 Foo bar baz");
    }

    @Command(
            names = "test"
    )
    public static class TestCommand implements CommandClass {

        @Command(
                names = "1"
        )
        public void runTestCommand(List<String> arguments) {
            System.out.printf(
                    "Collection type: %s. Provided arguments: %s\n",
                    arguments.getClass(),
                    arguments
            );
        }

        @Command(
                names = "2"
        )
        public void runTestCommand2(Set<String> set) {
            System.out.printf(
                    "Type: %s. Provided arguments: %s\n",
                    set.getClass(),
                    set
            );
        }
    }

    public static class StringSet extends HashSet<String> implements Set<String> {}
}
