package me.fixeddev.commandflow.part.defaults;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TranslatableComponent;

import java.util.Collections;
import java.util.List;

import static net.kyori.text.TextComponent.of;

public class LongPart extends PrimitivePart {

	private final long max;
	private final long min;
	private final boolean ranged;

	/**
	 * Creates a PrimitivePart instance with the given name.
	 * @param name The name for this part.
	 */
	private LongPart(String name, long min, long max, boolean ranged) {
		super(name);
		this.min = min;
		this.max = max;
		this.ranged = ranged;
	}

	public LongPart(String name, long min, long max) {
		this(name, min, max, true);
	}

	public LongPart(String name) {
		this(name, 0L, 0L, false);
	}

	@Override
	public List<Long> parseValue(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
		long next = stack.nextLong();

		if (ranged && (next > max || next < min)) {
			throw new ArgumentParseException(
				TranslatableComponent.of(
					"number.out-range",
					of(next),
					of(min),
					of(max)
				)
			);
		}

		return Collections.singletonList(next);
	}

}
