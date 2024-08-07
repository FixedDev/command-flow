package team.unnamed.commandflow.brigadier;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.brigadier.mappings.BrigadierCommandNodeMappings;
import team.unnamed.commandflow.bukkit.BukkitCommandManager;
import team.unnamed.commandflow.command.Command;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


public abstract class BrigadierProvider {

    public static CommandBrigadierConverter<BukkitBrigadierCommandSource, CommandSender> obtain(Authorizer authorizer) {
        return new CommandBrigadierConverter<>(
                BukkitBrigadierCommandSource::getBukkitSender,
                BrigadierCommandNodeMappings.<BukkitBrigadierCommandSource>defaultMappings().build(),
                authorizer
        );
    }

    public static Map.Entry<BrigadierCommandManager<BukkitBrigadierCommandSource, CommandSender>, SendDataListener> obtain(
            BukkitCommandManager delegate,
            Authorizer authorizer) {

        BiConsumer<List<LiteralCommandNode<BukkitBrigadierCommandSource>>, Command> empty = (literalCommandNodes, command) -> {
        };

        BrigadierCommandManager<BukkitBrigadierCommandSource, CommandSender> commandManager =
                new BrigadierCommandManager<>(delegate, obtain(authorizer), empty, empty);

        SendDataListener listener = new SendDataListener(commandManager);

        return new AbstractMap.SimpleEntry<>(commandManager, listener);
    }

    public static BrigadierCommandManager<BukkitBrigadierCommandSource, CommandSender> obtainAndRegister(
            BukkitCommandManager delegate,
            Plugin plugin,
            Authorizer authorizer) {

        Map.Entry<BrigadierCommandManager<BukkitBrigadierCommandSource, CommandSender>, SendDataListener> entry = obtain(delegate, authorizer);

        Bukkit.getPluginManager().registerEvents(entry.getValue(), plugin);

        return entry.getKey();
    }

    public static class SendDataListener implements Listener {

        private final BrigadierCommandManager<BukkitBrigadierCommandSource, CommandSender> commandManager;

        public SendDataListener(BrigadierCommandManager<BukkitBrigadierCommandSource, CommandSender> commandManager) {
            this.commandManager = commandManager;
        }

        @EventHandler
        @SuppressWarnings("deprecation")
        public void onData(AsyncPlayerSendCommandsEvent<BukkitBrigadierCommandSource> event) {
            if (event.isAsynchronous() || !event.hasFiredAsync()) {
                commandManager.getBrigadierNodes().forEach((command, literalCommandNodes) -> {
                    for (LiteralCommandNode<BukkitBrigadierCommandSource> node : literalCommandNodes) {
                        register(node, event.getCommandNode());
                    }
                });
            }
        }

        private void register(LiteralCommandNode<BukkitBrigadierCommandSource> node, RootCommandNode<BukkitBrigadierCommandSource> root) {
            root.getChildren().removeIf(tCommandNode ->
                    tCommandNode.getName().equals(node.getName())
            ); // remove the original

            root.addChild(node); // register
        }
    }
}
