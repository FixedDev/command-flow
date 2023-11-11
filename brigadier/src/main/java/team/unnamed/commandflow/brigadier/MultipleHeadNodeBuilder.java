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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.SingleRedirectModifier;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

public class MultipleHeadNodeBuilder<T> {

    private final List<ArgumentBuilder<T, ?>> headPointers;
    private final List<ArgumentBuilder<T, ?>> tailPointers;

    private ArgumentBuilder<T, ?> builder;

    public MultipleHeadNodeBuilder() {
        headPointers = new ArrayList<>();
        tailPointers = new ArrayList<>();
    }

    public MultipleHeadNodeBuilder<T> then(MultipleHeadNodeBuilder<T> node) {
        for (ArgumentBuilder<T, ?> headPointer : headPointers) {
            if (headPointer.getRedirect() != null) {
                continue;
            }

            for (ArgumentBuilder<T, ?> tailPointer : node.tailPointers) {
                headPointer.then(tailPointer);
            }
        }

        headPointers.clear();
        headPointers.addAll(node.headPointers);

        return this;
    }

    public MultipleHeadNodeBuilder<T> then(ArgumentBuilder<T, ?> node) {
        if (getHeadsSize() == 0) {
            headPointers.add(node);
            tailPointers.add(node);

            return this;
        }

        ListIterator<ArgumentBuilder<T, ?>> iterator = headPointers.listIterator();

        while (iterator.hasNext()) {
            ArgumentBuilder<T, ?> headPointer = iterator.next();

            if (headPointer.getRedirect() != null) {
                continue;
            }

            iterator.set(headPointer.then(node));
        }

        headPointers.clear();
        headPointers.add(node);

        return this;
    }

    public MultipleHeadNodeBuilder<T> then(Collection<ArgumentBuilder<T, ?>> nodes) {
        if (getHeadsSize() == 0) {
            headPointers.addAll(nodes);

            tailPointers.addAll(nodes);
            return this;
        }

        for (ArgumentBuilder<T, ?> headPointer : headPointers) {
            if (headPointer.getRedirect() != null) {
                continue;
            }

            for (ArgumentBuilder<T, ?> commandNode : nodes) {
                headPointer.then(commandNode);
            }
        }

        headPointers.clear();
        headPointers.addAll(nodes);

        return this;
    }

    public MultipleHeadNodeBuilder<T> then(int headIndex, ArgumentBuilder<T, ?> node) {
        headPointers.get(headIndex).then(node);

        return setHeadPointer(headIndex, node);
    }

    public MultipleHeadNodeBuilder<T> executes(Command<T> command) {
        ListIterator<ArgumentBuilder<T, ?>> iterator = headPointers.listIterator();

        while (iterator.hasNext()) {
            ArgumentBuilder<T, ?> headPointer = iterator.next();

            iterator.set(headPointer.executes(command));
        }

        return this;
    }

    public MultipleHeadNodeBuilder<T> requires(Predicate<T> requirement) {
        ListIterator<ArgumentBuilder<T, ?>> iterator = headPointers.listIterator();

        while (iterator.hasNext()) {
            ArgumentBuilder<T, ?> headPointer = iterator.next();

            iterator.set(headPointer.requires(requirement));
        }

        return this;
    }

    public MultipleHeadNodeBuilder<T> redirect(CommandNode<T> target) {
        ListIterator<ArgumentBuilder<T, ?>> iterator = headPointers.listIterator();

        while (iterator.hasNext()) {
            ArgumentBuilder<T, ?> headPointer = iterator.next();

            iterator.set(headPointer.redirect(target));
        }

        return this;
    }

    public MultipleHeadNodeBuilder<T> redirect(CommandNode<T> target, SingleRedirectModifier<T> modifier) {
        ListIterator<ArgumentBuilder<T, ?>> iterator = headPointers.listIterator();

        while (iterator.hasNext()) {
            ArgumentBuilder<T, ?> headPointer = iterator.next();

            iterator.set(headPointer.redirect(target, modifier));
        }

        return this;
    }

    public MultipleHeadNodeBuilder<T> fork(CommandNode<T> target, RedirectModifier<T> modifier) {
        ListIterator<ArgumentBuilder<T, ?>> iterator = headPointers.listIterator();

        while (iterator.hasNext()) {
            ArgumentBuilder<T, ?> headPointer = iterator.next();

            iterator.set(headPointer.fork(target, modifier));
        }

        return this;
    }

    public MultipleHeadNodeBuilder<T> forward(CommandNode<T> target, RedirectModifier<T> modifier, boolean fork) {
        ListIterator<ArgumentBuilder<T, ?>> iterator = headPointers.listIterator();

        while (iterator.hasNext()) {
            ArgumentBuilder<T, ?> headPointer = iterator.next();

            iterator.set(headPointer.forward(target, modifier, fork));
        }

        return this;
    }

    public MultipleHeadNodeBuilder<T> setHeadPointer(int index, ArgumentBuilder<T, ?> node) {
        headPointers.set(index, node);

        return this;
    }

    public MultipleHeadNodeBuilder<T> addHead(ArgumentBuilder<T, ?> node) {
        headPointers.add(node);

        return this;
    }

    public ArgumentBuilder<T, ?> getTail() {
        return tailPointers.get(0);
    }

    public ArgumentBuilder<T, ?> getHead() {
        return headPointers.get(0);
    }

    public List<ArgumentBuilder<T, ?>> getTails() {
        return tailPointers;
    }

    public List<ArgumentBuilder<T, ?>> getHeads() {
        return headPointers;
    }

    public int getHeadsSize() {
        return headPointers.size();
    }

    public int getTailsSize() {
        return tailPointers.size();
    }

}
