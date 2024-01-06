package team.unnamed.commandflow.brigadier;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.brigadier.mappings.BrigadierCommandNodeMappings;
import team.unnamed.commandflow.brigadier.mappings.defaults.GeneralMapping;
import team.unnamed.commandflow.command.Command;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.PartsWrapper;
import team.unnamed.commandflow.part.SinglePartWrapper;
import team.unnamed.commandflow.part.defaults.FirstMatchPart;
import team.unnamed.commandflow.part.defaults.OptionalPart;
import team.unnamed.commandflow.part.defaults.SubCommandPart;
import team.unnamed.commandflow.part.defaults.SwitchPart;
import team.unnamed.commandflow.part.defaults.ValueFlagPart;
import team.unnamed.commandflow.part.visitor.CommandPartVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public class CommandBrigadierConverter<T, V> {

    private final BrigadierCommandNodeMappings<T> commandNodeMappings;
    private final Authorizer authorizer;
    private final Function<T, V> senderMapping;

    public CommandBrigadierConverter(Function<T, V> senderMapping,
                                     BrigadierCommandNodeMappings<T> commandNodeMappings,
                                     Authorizer authorizer) {
        this.commandNodeMappings = commandNodeMappings;
        this.senderMapping = senderMapping;
        this.authorizer = authorizer;
    }

    public List<LiteralCommandNode<T>> getBrigadierCommand(Command command) {
        return getBrigadierCommand(command, false);
    }

    public List<LiteralCommandNode<T>> getBrigadierCommand(Command command, boolean optional) {
        LiteralArgumentBuilder<T> argumentBuilder = LiteralArgumentBuilder.<T>literal(command.getName())
                .requires(new PermissionRequirement<>(command.getPermission(), authorizer, senderMapping));

        if (isFirstPartOptional(command.getPart())) {
            argumentBuilder.executes(context -> 1);
        }

        LiteralCommandNode<T> mainNode = argumentBuilder.build();

        CommandNode<T> node = convertToNodes(command);

        if (node != null) {
            if (node instanceof LiteralCommandNode && (node.getName().equals("valueFlag") || node.getName().equals("Wrapper"))) {
                for (CommandNode<T> child : node.getChildren()) {
                    mainNode.addChild(child);
                }
            } else {
                mainNode.addChild(node);
            }
        }


        List<LiteralCommandNode<T>> argumentBuilders = new ArrayList<>();

        argumentBuilders.add(mainNode);

        for (String alias : command.getAliases()) {
            LiteralArgumentBuilder<T> aliasBuilder = LiteralArgumentBuilder
                    .<T>literal(alias)
                    .redirect(mainNode)
                    .requires(new PermissionRequirement<>(command.getPermission(), authorizer, senderMapping));

            if (optional) {
                aliasBuilder = aliasBuilder.executes(context -> 1);
            }

            argumentBuilders.add(aliasBuilder.build());
        }

        return argumentBuilders;
    }

    private CommandNode<T> convertToNodes(Command command) {
        return command.getPart().acceptVisitor(new CommandPartVisitor<CommandNode<T>>() {
            @Override
            public CommandNode<T> visit(CommandPart part) {
                if (part instanceof SwitchPart) {
                    SwitchPart switchPart = (SwitchPart) part;

                    LiteralCommandNode<T> shortName = LiteralArgumentBuilder.<T>literal("-" + switchPart.getShortName()).build();
                    LiteralCommandNode<T> fullName = LiteralArgumentBuilder.<T>literal("--" + switchPart.getName()).build();

                    LiteralArgumentBuilder<T> builder = LiteralArgumentBuilder.literal("valueFlag");

                    builder.then(shortName);

                    if (switchPart.allowsFullName()) {
                        builder.then(fullName);
                    }

                    return builder.build();
                }

                return null;
            }

            @Override
            public CommandNode<T> visit(ArgumentPart argumentPart) {
                return CommandBrigadierConverter.this.visit(argumentPart);
            }

            @Override
            public CommandNode<T> visit(PartsWrapper partsWrapper) {
                return CommandBrigadierConverter.this.visit(partsWrapper, this);
            }

            @Override
            public CommandNode<T> visit(SinglePartWrapper singlePartWrapper) {
                return CommandBrigadierConverter.this.visit(singlePartWrapper, this);
            }

            @Override
            public CommandNode<T> visit(SubCommandPart subCommand) {
                return CommandBrigadierConverter.this.visit(subCommand, this);
            }
        });
    }

    private void handleSimpleWrapperAdd(MultipleHeadNode<T> nodeBuilder, CommandNode<T> node, boolean nextOptional) {
        if (nextOptional) {
            for (CommandNode<T> child : node.getChildren()) {
                ArgumentBuilder<T, ?> childBuilder = child.createBuilder();
                childBuilder.executes(context -> 1);

                nodeBuilder.addChild(childBuilder.build());
            }
        } else {
            nodeBuilder.addChild(node.getChildren());
        }
    }

    private boolean handleValueFlagAdd(MultipleHeadNode<T> nodeBuilder, CommandNode<T> node, boolean shouldAddNextToTail) {
        nodeBuilder.addChild(node.getChildren());

        if (shouldAddNextToTail) {
            for (CommandNode<T> child : node.getChildren()) {
                nodeBuilder.addTailPointer(child);
            }
        }

        Iterator<CommandNode<T>> iterator = node.getChildren().iterator();

        // at least one is present
        CommandNode<T> mainNode = iterator.next();
        shouldAddNextToTail = true;

        // check if it is a SwitchPart, lol
        if (!mainNode.getChildren().isEmpty()) {
            // it isn't
            nodeBuilder.setHeadPointer(0, mainNode.getChildren().iterator().next());
        }

        return shouldAddNextToTail;
    }

    private boolean handleSimplePartAdding(MultipleHeadNode<T> nodeBuilder, boolean shouldAddNextToTail, CommandNode<T> node, boolean nextOptional) {
        if (nextOptional) {
            ArgumentBuilder<T, ?> childBuilder = node.createBuilder();

            childBuilder.executes(context -> 1);
            node = childBuilder.build();
        }

        nodeBuilder.addChild(node);

        if (shouldAddNextToTail) {
            nodeBuilder.addTailPointer(node);
            shouldAddNextToTail = false;
        }
        return shouldAddNextToTail;
    }

    private boolean isFirstPartOptional(CommandPart part) {
        if (part instanceof PartsWrapper) {
            for (CommandPart commandPart : ((PartsWrapper) part).getParts()) {
                if (!(commandPart instanceof ArgumentPart) && !(commandPart instanceof OptionalPart)) {
                    continue;
                }

                return isFirstPartOptional(commandPart);
            }
        }

        if (part instanceof SinglePartWrapper) {
            if (part instanceof OptionalPart) {
                return true;
            }

            return isFirstPartOptional(((SinglePartWrapper) part).getPart());
        }

        if (part instanceof SubCommandPart) {
            return ((SubCommandPart) part).isOptional();
        }

        return false;
    }

    private CommandNode<T> toArgumentBuilder(ArgumentPart part) {

        return commandNodeMappings.getMappingByClass(part.getClass())
                .map(map -> map.convert(part))
                .orElseGet(() -> new GeneralMapping<>(senderMapping).convert(part));

        /*if (part instanceof PlayerPart || part instanceof OfflinePlayerPart) {
            return RequiredArgumentBuilder.argument(part.getName(),
                    constructMinecraftArgumentType(NamespacedKey.minecraft("entity"),
                            new Class[]{boolean.class, boolean.class}, true, true)).build();
        }*/
    }

    public CommandNode<T> visit(ArgumentPart argumentPart) {
        return toArgumentBuilder(argumentPart);
    }

    public CommandNode<T> visit(PartsWrapper partsWrapper, CommandPartVisitor<CommandNode<T>> visitor) {
        if (partsWrapper instanceof FirstMatchPart) {
            LiteralArgumentBuilder<T> builder = LiteralArgumentBuilder.literal("Wrapper");

            FirstMatchPart firstMatchPart = (FirstMatchPart) partsWrapper;

            for (CommandPart part : firstMatchPart.getParts()) {
                CommandNode<T> argumentNode = part.acceptVisitor(visitor);

                if (argumentNode == null) {
                    continue; // not available for brigadier completition.
                }

                if (argumentNode instanceof LiteralCommandNode) {
                    if (argumentNode.getName().equals("Wrapper") || argumentNode.getName().equals("valueFlag")) {
                        // we can't do a lot more lol.
                        argumentNode.getChildren().forEach(builder::then);

                        continue;
                    }
                }

                builder.then(argumentNode);
            }

            return builder.build();
        }

        MultipleHeadNode<T> nodeBuilder = new MultipleHeadNode<>();
        boolean shouldAddNextToTail = false;

        ListIterator<CommandPart> partsIterator = partsWrapper.getParts().listIterator();

        while (partsIterator.hasNext()) {
            CommandPart part = partsIterator.next();

            CommandNode<T> node = part.acceptVisitor(visitor);

            if (node == null) {
                continue; // ignore this part, it's not available to be completed in brigadier.
            }

            boolean nextOptional = false;

            if (partsIterator.hasNext()) {
                CommandPart next = partsIterator.next();

                if (isFirstPartOptional(next)) {
                    nextOptional = true;
                }

                partsIterator.previous();
            }

            if (nodeBuilder.getHeadsSize() > 0) {
                if (node instanceof LiteralCommandNode) {
                    if (node.getName().equals("Wrapper")) {
                        handleSimpleWrapperAdd(nodeBuilder, node, nextOptional);
                    } else if (node.getName().equals("valueFlag")) {
                        shouldAddNextToTail = handleValueFlagAdd(nodeBuilder, node, shouldAddNextToTail);
                    } else {
                        if (nextOptional) {
                            ArgumentBuilder<T, ?> childBuilder = node.createBuilder();

                            childBuilder.executes(context -> 1);
                            node = childBuilder.build();
                        }

                        nodeBuilder.addChild(node);
                    }
                } else {
                    shouldAddNextToTail = handleSimplePartAdding(nodeBuilder, shouldAddNextToTail, node, nextOptional);
                }
            } else {
                if (node.getName().equals("valueFlag")) {
                    shouldAddNextToTail = handleValueFlagAdd(nodeBuilder, node, shouldAddNextToTail);

                    continue;
                }

                shouldAddNextToTail = handleSimplePartAdding(nodeBuilder, shouldAddNextToTail, node, nextOptional);
            }
        }

        return nodeBuilder.getWrappedTail("Wrapper");
    }

    public CommandNode<T> visit(SinglePartWrapper singlePartWrapper, CommandPartVisitor<CommandNode<T>> visitor) {
        if (singlePartWrapper instanceof ValueFlagPart) {
            ValueFlagPart valueFlagPart = (ValueFlagPart) singlePartWrapper;
            CommandNode<T> node = singlePartWrapper.getPart().acceptVisitor(visitor);

            LiteralCommandNode<T> shortName = LiteralArgumentBuilder.<T>literal("-" + valueFlagPart.getShortName()).then(node).build();
            LiteralCommandNode<T> fullName = LiteralArgumentBuilder.<T>literal("--" + valueFlagPart.getName()).redirect(shortName).build();

            LiteralArgumentBuilder<T> builder = LiteralArgumentBuilder.literal("valueFlag");

            builder.then(shortName);

            if (valueFlagPart.allowsFullName()) {
                builder.then(fullName);
            }

            return builder.build();
        }

        return singlePartWrapper.getPart().acceptVisitor(visitor);
    }

    public CommandNode<T> visit(SubCommandPart subCommand, CommandPartVisitor<CommandNode<T>> visitor) {
        LiteralArgumentBuilder<T> builder = LiteralArgumentBuilder.literal("Wrapper");

        for (Command command : subCommand.getSubCommands()) {
            CommandNode<T> node = command.getPart().acceptVisitor(visitor);

            LiteralArgumentBuilder<T> literalNodeBuilder = LiteralArgumentBuilder.literal(command.getName());

            if (command.getPermission() != null) {
                literalNodeBuilder.requires(new PermissionRequirement<>(command.getPermission(), authorizer, senderMapping));
            }

            if (node != null) {
                if (node instanceof LiteralCommandNode && (node.getName().equals("valueFlag") || node.getName().equals("Wrapper"))) {
                    for (CommandNode<T> child : node.getChildren()) {
                        literalNodeBuilder.then(child);
                    }
                } else {
                    literalNodeBuilder.then(node);
                }
            }

            LiteralCommandNode<T> literalNode = literalNodeBuilder.build();

            builder = builder.then(literalNode);

            for (String alias : command.getAliases()) {
                LiteralCommandNode<T> aliasNode = LiteralArgumentBuilder.<T>literal(alias).redirect(literalNode).build();

                builder = builder.then(aliasNode);
            }
        }

        return builder.build();
    }
}
