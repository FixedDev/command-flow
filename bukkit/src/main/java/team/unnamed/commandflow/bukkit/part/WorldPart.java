package team.unnamed.commandflow.bukkit.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldPart implements ArgumentPart {

    private final String name;

    public WorldPart(String name) {
        this.name = name;
    }

    @Override
    public List<World> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        return Collections.singletonList(checkedWorld(stack));
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if (prefix == null) {
            return Collections.emptyList();
        }

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
        World world = Bukkit.getWorld(stack.next());
        if (world == null) {
            throw new ArgumentParseException("World not exist!");
        }
        return world;
    }

    @Override
    public String getName() {
        return name;
    }

}
