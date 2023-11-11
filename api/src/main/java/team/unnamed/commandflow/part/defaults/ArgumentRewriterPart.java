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
import team.unnamed.commandflow.part.visitor.CommandPartVisitor;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentRewriterPart implements CommandPart {

    private final CommandPart delegatePart;
    private final Map<String, String> rewrites;

    public ArgumentRewriterPart( CommandPart delegatePart) {
        this.delegatePart = delegatePart;

        rewrites = new HashMap<>();
    }

    @Override
    public String getName() {
        return delegatePart.getName();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException {
        String next = stack.next();
        int position = stack.getPosition();

        String to = rewrites.get(next);

        if (to != null) {
            stack.getBacking().set(position, to);
        }

        delegatePart.parse(context, stack, caller);
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        return delegatePart.getLineRepresentation();
    }

    @Override
    public @Nullable List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return delegatePart.getSuggestions(commandContext, stack);
    }

    @Override
    public boolean isAsync() {
        return delegatePart.isAsync();
    }

    @Override
    public <T> T acceptVisitor(CommandPartVisitor<T> visitor) {
        return delegatePart.acceptVisitor(visitor);
    }

    public void addRewrite(String to, String... from) {
        for (String fromStr : from) {
            rewrites.put(fromStr, to);
        }
    }

    public void removeRewrite(String to, String... from) {
        for (String fromStr : from) {
            rewrites.remove(fromStr, to);
        }
    }

}
