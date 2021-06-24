package me.fixeddev.commandflow.stack;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import java.util.List;
import java.util.StringJoiner;

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
        if (position == 0) {
            throw new IllegalStateException("You must advance the stack at least 1 time before calling remove!");
        }

        String toRemove = current();
        getBacking().remove(toRemove);

        position--;

        if (parent != null) {
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
    public String nextQuoted() {
        if (position >= originalArguments.size()) {
            throw new NoMoreArgumentsException();
        }

        String argument = originalArguments.get(position);
        char quote = argument.charAt(0);

        // normal argument, just return it
        if (quote != '"' && quote != '\'') {
            position++;
            return argument;
        }

        // remove the initial quote
        argument = argument.substring(1);
        StringJoiner joiner = new StringJoiner(" ");

        while (true) {
            boolean escaped = false;
            StringBuilder builder = new StringBuilder(argument.length());

            for (int i = 0; i < argument.length(); i++) {
                char current = argument.charAt(i);
                if (current == '\\') {
                    escaped = true;
                    continue;
                }
                if (current == quote) {
                    if (escaped) {
                        builder.append(quote);
                        escaped = false;
                        continue;
                    }

                    position++;
                    String extra = argument.substring(i + 1);
                    if (!extra.isEmpty()) {
                        originalArguments.add(position, extra);
                    }
                    joiner.add(builder.toString());
                    return joiner.toString();
                }

                if (escaped) {
                    builder.append('\\');
                    escaped = false;
                }

                builder.append(current);
            }

            if (builder.length() > 0) {
                joiner.add(builder.toString());
            }

            // pass to the next argument
            position++;
            if (position >= originalArguments.size()) {
                break;
            } else {
                argument = originalArguments.get(position);
            }
        }

        return joiner.toString();
    }

    @Override
    public int nextInt() throws ArgumentParseException {
        String next = next();
        try {
            return Integer.parseInt(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException(
                TranslatableComponent.of(
                    "invalid.integer",
                    TextComponent.of(next)
                )
            );
        }
    }

    @Override
    public float nextFloat() throws ArgumentParseException {
        String next = next();

        try {
            return Float.parseFloat(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException(
                TranslatableComponent.of(
                    "invalid.float",
                    TextComponent.of(next)
                )
            );
        }
    }

    @Override
    public double nextDouble() throws ArgumentParseException {
        String next = next();

        try {
            return Double.parseDouble(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException(
                TranslatableComponent.of(
                    "invalid.double",
                    TextComponent.of(next)
                )
            );
        }
    }

    @Override
    public byte nextByte() throws ArgumentParseException {
        String next = next();

        try {
            return Byte.parseByte(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException(
                TranslatableComponent.of(
                    "invalid.byte",
                    TextComponent.of(next)
                )
            );
        }
    }

    @Override
    public boolean nextBoolean() throws ArgumentParseException {
        String next = next();

        if (!next.equalsIgnoreCase("true") && !next.equalsIgnoreCase("false")) {
            throw new ArgumentParseException(
                TranslatableComponent.of(
                    "invalid.boolean",
                    TextComponent.of(next)
                )
            );
        }

        return Boolean.parseBoolean(next);
    }

    @Override
    public long nextLong() throws ArgumentParseException {
        String next = next();

        try {
            return Long.parseLong(next);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException(
              TranslatableComponent.of(
                "invalid.long",
                TextComponent.of(next)
              )
            );
        }
    }

    @Override
    public void markAsConsumed() {
        int oldPosition = position;
        this.position = originalArguments.size();

        if (parent != null) {
            int offset = position - oldPosition;
            parent.position += offset;
        }
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

            if (originalArguments.size() < snapshot.backing.size()) {
                for (String arg : snapshot.backing) {

                    if(originalArguments.size() > index){
                        originalArguments.set(index, arg);
                    } else {
                        originalArguments.add(index, arg);
                    }

                    index++;
                }
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
