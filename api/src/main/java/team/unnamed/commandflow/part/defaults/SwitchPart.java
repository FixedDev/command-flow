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
package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class SwitchPart implements CommandPart {

    private final String name;
    private final String shortName;
    private final boolean allowFullName;

    public SwitchPart(String name, String shortName, boolean allowFullName) {
        this.name = name;
        this.shortName = shortName;
        this.allowFullName = allowFullName;
    }

    public SwitchPart(String shortName) {
        this.name = shortName;
        this.shortName = shortName;
        this.allowFullName = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = Component.text();
        builder.append(Component.text("["));
        builder.append(Component.text("-" + shortName));
        builder.append(Component.text("]"));

        return builder.build();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();

        boolean found = false;

        while (stack.hasNext()) {
            String arg = stack.next();

            if (!arg.startsWith("-")) {
                continue;
            }

            if (arg.equals("--" + name) && allowFullName) {
                stack.remove();

                context.setValue(this, true);
                found = true;

                break;
            }

            if (arg.equals("-" + shortName)) {
                stack.remove();

                context.setValue(this, true);
                found = true;

                break;
            }
        }

        if (!found) {
            context.setValue(this, false);
        }

        stack.applySnapshot(snapshot, false);
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        StackSnapshot snapshot = stack.getSnapshot();

        while (stack.hasNext()) {
            String arg = stack.next();

            if (!arg.startsWith("-")) {
                continue;
            }

            if (arg.equals("--" + name) && allowFullName) {
                stack.remove();
                break;
            }

            if (arg.equals("-" + shortName)) {
                stack.remove();
                break;
            }
        }

        stack.applySnapshot(snapshot, false);

        return Collections.emptyList();
    }

    public String getShortName() {
        return shortName;
    }

    public boolean allowsFullName() {
        return allowFullName;
    }

}
