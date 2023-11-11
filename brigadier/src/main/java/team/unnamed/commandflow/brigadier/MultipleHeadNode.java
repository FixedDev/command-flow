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
package team.unnamed.commandflow.brigadier;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class MultipleHeadNode<T> {

    private final List<CommandNode<T>> headPointers;
    private final List<CommandNode<T>> tailPointers;

    public MultipleHeadNode() {
        headPointers = new ArrayList<>();
        tailPointers = new ArrayList<>();
    }

    public MultipleHeadNodeBuilder<T> toBuilder() {
        MultipleHeadNodeBuilder<T> builder = new MultipleHeadNodeBuilder<>();

        for (CommandNode<T> headPointer : headPointers) {
            ArgumentBuilder<T, ?> headPointerBuilder = headPointer.createBuilder();

            builder = builder.addHead(headPointerBuilder);
        }

        return builder;
    }

    public MultipleHeadNode<T> addChild(CommandNode<T> node) {
        if (getHeadsSize() == 0) {
            headPointers.add(node);
            tailPointers.add(node);

            return this;
        }

        ListIterator<CommandNode<T>> iterator = headPointers.listIterator();

        while (iterator.hasNext()) {
            CommandNode<T> headPointer = iterator.next();

            if (headPointer == null) {
                continue;
            }

            if (headPointer.getRedirect() != null) {
                continue;
            }

            headPointer.addChild(node);
        }

        headPointers.clear();
        headPointers.add(node);

        return this;
    }

    public MultipleHeadNode<T> addChild(Collection<CommandNode<T>> nodes) {
        if (getHeadsSize() == 0) {
            headPointers.addAll(nodes);

            tailPointers.addAll(nodes);
            return this;
        }

        for (CommandNode<T> headPointer : headPointers) {
            if (headPointer.getRedirect() != null) {
                continue;
            }

            for (CommandNode<T> commandNode : nodes) {
                headPointer.addChild(commandNode);
            }
        }
        headPointers.clear();
        headPointers.addAll(nodes);

        return this;
    }

    public MultipleHeadNode<T> addChild(int headIndex, CommandNode<T> node) {
        headPointers.get(headIndex).addChild(node);

        return setHeadPointer(headIndex, node);
    }

    public MultipleHeadNode<T> setHeadPointer(int index, CommandNode<T> node) {
        headPointers.set(index, node);

        return this;
    }

    public MultipleHeadNode<T> addTailPointer(CommandNode<T> node) {
        tailPointers.add(node);

        return this;
    }

    public MultipleHeadNode<T> addTailPointer(int index, CommandNode<T> node) {
        tailPointers.add(index, node);

        return this;
    }

    public MultipleHeadNode<T> setTailPointer(int index, CommandNode<T> node) {
        tailPointers.set(index, node);

        return this;
    }

    public CommandNode<T> getWrappedTail(String name) {
        if (getTailsSize() == 1) {
            return getTail();
        }

        LiteralArgumentBuilder<T> builder = LiteralArgumentBuilder.literal(name);

        for (CommandNode<T> tailPointer : tailPointers) {
            builder.then(tailPointer);
        }

        return builder.build();
    }

    public CommandNode<T> getTail() {
        return tailPointers.get(0);
    }

    public CommandNode<T> getTail(int idx) {
        return tailPointers.get(idx);
    }

    public CommandNode<T> getWrappedHead(String name) {
        if (getHeadsSize() == 1) {
            return getHead();
        }

        LiteralArgumentBuilder<T> builder = LiteralArgumentBuilder.literal(name);

        for (CommandNode<T> headPointer : headPointers) {
            builder.then(headPointer);
        }

        return builder.build();
    }

    public CommandNode<T> getHead() {
        return headPointers.get(0);
    }

    public CommandNode<T> getHead(int idx) {
        return headPointers.get(idx);
    }

    public List<CommandNode<T>> getTails() {
        return tailPointers;
    }

    public List<CommandNode<T>> getHeads() {
        return headPointers;
    }

    public int getHeadsSize() {
        return headPointers.size();
    }

    public int getTailsSize() {
        return tailPointers.size();
    }

}
