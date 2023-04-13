package me.fixeddev.commandflow.brigadier;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.ParseResult;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.MessageUtils;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BrigadierCommandManager extends BukkitCommandManager {

    private Commodore commodore;
    private CommandBrigadierConverter commandBrigadierConverter;
    private final Plugin plugin;

    private final Map<Command, List<LiteralCommandNode<Object>>> brigadierNodes;

    private MethodHandle selectEntitiesHandle;

    public BrigadierCommandManager(CommandManager commandManager, Plugin plugin) {
        super(commandManager, plugin.getName());

        this.plugin = plugin;
        this.brigadierNodes = new HashMap<>();

        if (isCommodoreSupported()) {
            commodore = CommodoreProvider.getCommodore(plugin);

            commandBrigadierConverter = new CommandBrigadierConverter(commodore);
        }

        getTranslator().setProvider(new BrigadierDefaultTranslationProvider());

        try {
            selectEntitiesHandle = MethodHandles.lookup().findStatic(Bukkit.class, "selectEntities",
                    MethodType.methodType(List.class, CommandSender.class, String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to find selectEntities method, consider using Paper for selector functionality on commands");
        }

    }

    public BrigadierCommandManager(Plugin plugin) {
        super(plugin.getName());

        this.plugin = plugin;
        this.brigadierNodes = new HashMap<>();

        if (isCommodoreSupported()) {
            commodore = CommodoreProvider.getCommodore(plugin);

            commandBrigadierConverter = new CommandBrigadierConverter(commodore);
        }

        getTranslator().setProvider(new BrigadierDefaultTranslationProvider());

        try {
            selectEntitiesHandle = MethodHandles.lookup().findStatic(Bukkit.class, "selectEntities",
                    MethodType.methodType(List.class, CommandSender.class, String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to find selectEntities method, consider using Paper for selector functionality on commands");
        }
    }

    private boolean isCommodoreSupported() {
        return CommodoreProvider.isSupported();
    }

    @Override
    public void registerCommand(Command command) {
        manager.registerCommand(command);

        BrigadierCommandWrapper bukkitCommand = new BrigadierCommandWrapper(command,
                this, getTranslator());

        wrapperMap.put(command.getName(), bukkitCommand);
        bukkitCommandMap.register(fallbackPrefix, bukkitCommand);

        if (isCommodoreSupported()) {
            brigadierNodes.put(command, commandBrigadierConverter.registerCommand(command, plugin, bukkitCommand));
        }
    }

    @Override
    public void unregisterCommand(Command command) {
        super.unregisterCommand(command);

        if (isCommodoreSupported()) {
            List<LiteralCommandNode<Object>> nodes = brigadierNodes.get(command);

            if (nodes == null) {
                return;
            }

            commandBrigadierConverter.unregisterCommand(nodes);
        }
    }

    @Override
    public boolean execute(Namespace accessor, List<String> arguments) throws CommandException {
        accessor.setObject(CommandManager.class, "commandManager", this);
        try {
            replaceSelectors(accessor, arguments);
        } catch (CommandException throwable) {
            try {
                return getErrorHandler().handleException(accessor, throwable);
            } catch (Throwable ex) {
                throwOrWrap(ex);
            }
        }

        return super.execute(accessor, arguments);
    }


    @Override
    public ParseResult parse(Namespace accessor, List<String> arguments) throws CommandException {
        try {
            replaceSelectors(accessor, arguments);
        } catch (CommandException throwable) {
            return new ParseResult() {
                @Override
                public Optional<CommandContext> getContext() {
                    return Optional.empty();
                }

                @Override
                public Optional<CommandException> getException() {
                    return Optional.of(throwable);
                }
            };
        }

        return super.parse(accessor, arguments);
    }

    @Override
    public boolean execute(Namespace accessor, String line) throws CommandException {
        List<String> tokens = getInputTokenizer().tokenize(line);

        return execute(accessor, tokens);
    }

    @Override
    public ParseResult parse(Namespace accessor, String line) throws CommandException {
        List<String> tokens = getInputTokenizer().tokenize(line);

        return parse(accessor, tokens);
    }


    private void throwOrWrap(Throwable throwable) throws CommandException {
        if (throwable instanceof CommandException) {
            throw (CommandException) throwable;
        }

        throw new CommandException(throwable);
    }

    private void replaceSelectors(Namespace accessor, List<String> arguments) throws CommandException {
        if (selectEntitiesHandle != null) {
            ListIterator<String> iterator = arguments.listIterator();

            while (iterator.hasNext()) {
                String argument = iterator.next();

                if (!argument.isEmpty() && argument.charAt(0) != '@') {
                    continue;
                }

                CommandSender sender = accessor.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);
                try {
                    @SuppressWarnings("unchecked")
                    List<Entity> entities = (List<Entity>) selectEntitiesHandle.invoke(sender, argument);

                    List<Player> matchedPlayers = entities.stream()
                            .filter(entity -> entity instanceof Player)
                            .map(entity -> ((Player) entity))
                            .collect(Collectors.toList());

                    if (matchedPlayers.isEmpty()) {
                        continue;
                    }

                    if (matchedPlayers.size() > 1) {
                        Component component = getTranslator().translate(Component.translatable("selector.ambiguous", Component.text(argument)), accessor);
                        BaseComponent[] components = MessageUtils.kyoriToBungee(component);

                        MessageUtils.sendMessage(sender, components);
                        continue;
                    }

                    iterator.set(matchedPlayers.get(0).getName());

                } catch (Throwable e) {
                    String message = e.getMessage();

                    if (e instanceof IllegalArgumentException) {
                        if (e.getCause() instanceof CommandSyntaxException) {
                            message = e.getCause().getMessage();
                        }
                    }

                    Component component = Component.translatable("selector.parse-error", Component.text(argument), Component.text(message));
                    throw new CommandUsage(component);
                }
            }
        }
    }

}
