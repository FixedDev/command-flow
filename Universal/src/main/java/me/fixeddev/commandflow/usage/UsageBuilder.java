package me.fixeddev.commandflow.usage;

import me.fixeddev.commandflow.CommandContext;
import net.kyori.text.Component;

/**
 * Creates an usage based on the CommandContext that you give to this class.
 * This class should be stateless, that means that it shouldn't have a defined state at any moment.
 */
public interface UsageBuilder {
    /**
     * Gets an usage based on the given {@link CommandContext}.
     *
     * @param commandContext The {@link CommandContext} used to create an usage.
     * @return A {@link Component}containing the usage for this command, shouldn't be send directly to the
     * any executor without being translated first by a {@link me.fixeddev.commandflow.translator.Translator}.
     */
    Component getUsage(CommandContext commandContext);
}
