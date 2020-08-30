package me.fixeddev.commandflow.stack;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;

import java.util.List;

public class SimpleArgumentStack implements ArgumentStack {
    protected List<String> originalArguments;
    protected SimpleArgumentStack parent;

    private int position = 0;

    public SimpleArgumentStack(List<String> originalArguments) {
        this.originalArguments = originalArguments;
    }

    protected SimpleArgumentStack(List<String> originalArguments, int position) {
        this.originalArguments = originalArguments;

        this.position = position;
    }

    protected SimpleArgumentStack(List<String> originalArguments, SimpleArgumentStack parent) {
        this.originalArguments = originalArguments;
        this.parent = parent;
    }


    @Override
    public boolean hasNext() {
        return originalArguments.size() > position;
    }

    @Override
    public String next() throws NoMoreArgumentsException {
        if (!hasNext()) {
            throw new NoMoreArgumentsException(originalArguments.size(), position);
        }

        if (parent != null) {
            parent.position++;
        }

        return originalArguments.get(position++);
    }

    @Override
    public String peek() throws NoMoreArgumentsException {
        if (!hasNext()) {
            throw new NoMoreArgumentsException(originalArguments.size(), position);
        }

        return originalArguments.get(position);
    }

    @Override
    public String current() {
        return originalArguments.get(position - 1);
    }

    @Override
    public String remove() {
        if(position == 0){
            throw new IllegalStateException("You must advance the stack at least 1 time before calling remove!");
        }

        String toRemove = current();
        getBacking().remove(toRemove);

        position--;

        if(parent != null){
            position--;
        }

        return toRemove;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public int getSize() {
        return originalArguments.size();
    }

    @Override
    public int getArgumentsLeft() {
        return getSize() - getPosition();
    }

    @Override
    public int nextInt() throws ArgumentParseException {
        String next = next();
        try {
            return Integer.parseInt(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Failed to parse the string " + next + " as int!");
        }
    }

    @Override
    public float nextFloat() throws ArgumentParseException {
        String next = next();

        try {
            return Float.parseFloat(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Failed to parse the string " + next + " as float!");
        }
    }

    @Override
    public double nextDouble() throws ArgumentParseException {
        String next = next();

        try {
            return Double.parseDouble(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Failed to parse the string " + next + " as double!");
        }
    }

    @Override
    public byte nextByte() throws ArgumentParseException {
        String next = next();

        try {
            return Byte.parseByte(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Failed to parse the string " + next + " as byte!");
        }
    }

    @Override
    public boolean nextBoolean() throws ArgumentParseException {
        String next = next();

        if (!next.equalsIgnoreCase("true") && !next.equalsIgnoreCase("false")) {
            throw new ArgumentParseException("Failed to parse the string " + next + " as boolean!");
        }

        return Boolean.parseBoolean(next);
    }

    @Override
    public void markAsConsumed() {
        this.position = originalArguments.size();
    }

    @Override
    public void applySnapshot(StackSnapshot snapshot, boolean changeArgs) {
        int offset = snapshot.position - position;
        this.position = snapshot.position;

        if (parent != null) {
            if (offset < 0) {
                parent.position += offset;
            } else {
                parent.position -= offset;
            }
        }

        if (changeArgs) {
            int index = 0;

            for (String arg : snapshot.backing) {
                originalArguments.set(index, arg);

                index++;
            }
        }
    }

    @Override
    public List<String> getBacking() {
        return originalArguments;
    }

    @Override
    public ArgumentStack getSlice(int start, int end) {
        return new SimpleArgumentStack(originalArguments.subList(start, end), this);
    }

    @Override
    public StackSnapshot getSnapshot(boolean useCurrentPos) {
        return new StackSnapshot(this, useCurrentPos ? position : -1);
    }
}
