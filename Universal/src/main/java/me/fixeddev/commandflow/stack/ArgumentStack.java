package me.fixeddev.commandflow.stack;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;

import java.util.List;

public interface ArgumentStack {
    boolean hasNext();

    String next() throws NoMoreArgumentsException;

    String peek() throws NoMoreArgumentsException;

    String current();

    String remove();

    int getPosition();

    int getSize();

    int getArgumentsLeft();

    int nextInt() throws ArgumentParseException;

    float nextFloat() throws ArgumentParseException;

    double nextDouble() throws ArgumentParseException;

    byte nextByte() throws ArgumentParseException;

    boolean nextBoolean() throws ArgumentParseException;

    void markAsConsumed();

    List<String> getBacking();

    ArgumentStack getSlice(int start, int end);

    default ArgumentStack getSliceFrom(int start) {
        return getSlice(start, getSize());
    }

    default ArgumentStack getSliceTo(int end) {
        return getSlice(getPosition(), end);
    }

    default ArgumentStack getSlice(int size) {
        return getSliceTo(getPosition() + size);
    }

    default StackSnapshot getSnapshot() {
        return getSnapshot(true);
    }

    StackSnapshot getSnapshot(boolean useCurrentPos);

    default void applySnapshot(StackSnapshot snapshot) {
        applySnapshot(snapshot, true);
    }

    void applySnapshot(StackSnapshot snapshot, boolean changeArgs);

}
