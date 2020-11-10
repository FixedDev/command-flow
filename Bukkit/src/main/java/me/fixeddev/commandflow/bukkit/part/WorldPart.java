package me.fixeddev.commandflow.bukkit.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldPart implements ArgumentPart {
    private final String name;
    private final boolean consumeAll;


    public WorldPart(String name) {
        this(name, false);
    }

    public WorldPart(String name, boolean consumeAll) {
        this.name = name;
        this.consumeAll = consumeAll;
    }

    @Override
    public List<World> parseValue(CommandContext context,
                                  ArgumentStack stack)
            throws ArgumentParseException {
        List<World> worlds = new ArrayList<>();
        if (consumeAll) {
            while (stack.hasNext()) {
                worlds.add(
                        checkedWorld(stack));
            }
        } else {
            worlds.add(
                    checkedWorld(stack));
        }
        return worlds;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : "";

        List<String> suggestions = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            if (world.getName().startsWith(prefix)) {
                suggestions.add(world.getName());
            }
        }

        if (suggestions.size() == 1 && Bukkit.getWorld(suggestions.get(0)) != null) {
            return Collections.emptyList();
        }

        return suggestions;
    }

    private World checkedWorld(ArgumentStack stack) {
        World world = Bukkit.getWorld(
                stack.next());
        if (world == null) {
            throw new ArgumentParseException("World not exist!");
        }
        return world;
    }

    @Override
    public Type getType() {
        return World.class;
    }

    @Override
    public String getName() {
        return name;
    }
}
