package team.unnamed.commandflow.bukkit.factory;

import team.unnamed.commandflow.annotated.part.AbstractModule;
import team.unnamed.commandflow.annotated.part.Key;
import team.unnamed.commandflow.annotated.annotation.Sender;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitModule extends AbstractModule {

    @Override
    public void configure() {
        bindFactory(CommandSender.class, new CommandSenderFactory());
        bindFactory(OfflinePlayer.class, new OfflinePlayerPartFactory());
        bindFactory(Player.class, new PlayerPartFactory());
        bindFactory(World.class, new WorldFactory());
        bindFactory(GameMode.class, new GameModeFactory());
        bindFactory(new Key(Player.class, Sender.class), new PlayerSenderFactory());
    }

}
