package me.fixeddev.commandflow.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An immutable blackbox containing a copy of a {@link ArgumentStack} at a specific time,
 * being able to restore the state of a {@link ArgumentStack} to this state.
 *
 * @see ArgumentStack#applySnapshot(StackSnapshot)
 */
public class StackSnapshot {

    final List<String> backing;
    final int position;

    public StackSnapshot(ArgumentStack stack, int position) {
        this.backing = new ArrayList<>(stack.getBacking());
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StackSnapshot snapshot = (StackSnapshot) o;
        return position == snapshot.position &&
                Objects.equals(backing, snapshot.backing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backing, position);
    }

}
