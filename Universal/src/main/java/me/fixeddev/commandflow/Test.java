package me.fixeddev.commandflow;

import me.fixeddev.commandflow.command.Action;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.SequentialCommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.stack.StackSnapshot;
import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    private static final CommandPart stringPart = new ArgumentPart() {
        @Override
        public String getName() {
            return "test";
        }

        @Override
        public List<String> parseValue(ArgumentStack stack) throws ArgumentParseException {
            List<String> values = new ArrayList<>();

            while (stack.hasNext()) {
                values.add(stack.next());
            }

            return values;
        }

        @Override
        public Type getType() {
            return String.class;
        }
    };

    private static CommandPart stringPart2 = new ArgumentPart() {
        @Override
        public String getName() {
            return "test2";
        }

        @Override
        public List<String> parseValue(ArgumentStack stack) throws ArgumentParseException {
            List<String> values = new ArrayList<>();

            while (stack.hasNext()) {
                values.add(stack.next());
            }

            return values;
        }

        @Override
        public Type getType() {
            return String.class;
        }
    };

    private static CommandPart flagPart = new CommandPart() {
        @Override
        public String getName() {
            return "flags";
        }

        @Override
        public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
            StackSnapshot snapshot = stack.getSnapshot();
            List<String> raw = new ArrayList<>();

            while (stack.hasNext()) {
                String arg = stack.next();

                if (arg.startsWith("-")) {
                    context.setValue(flagPart, arg.charAt(1));
                    raw.add(arg);

                    stack.remove();
                }

            }
            if(!raw.isEmpty()){
                context.setRaw(flagPart, raw);
            }
            stack.applySnapshot(snapshot, false);
        }
    };


    private static CommandPart limitingPart = new CommandPart() {
        @Override
        public String getName() {
            return "a";
        }

        @Override
        public void parse(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
            ArgumentStack slice = stack.getSlice(1);
            stringPart.parse(context, slice);
        }
    };

    public static void main(String[] args) {
        CommandManager manager = new SimpleCommandManager();

        manager.registerCommand(new Command() {
            @Override
            public @NotNull String getName() {
                return "test";
            }

            @Override
            public @NotNull List<String> getAliases() {
                return new ArrayList<>();
            }

            @Override
            public Component getDescription() {
                return null;
            }

            @Override
            public String getPermission() {
                return "";
            }

            @Override
            public Component getPermissionMessage() {
                return null;
            }

            @Override
            public @NotNull SequentialCommandPart getPart() {
                return new SequentialCommandPart("123", Arrays.asList(flagPart, limitingPart, stringPart2));
            }

            @Override
            public @NotNull Action getAction() {
                return context -> {
                    for (String test : context.getPart("test").flatMap(context::<String>getValue).orElse(new ArrayList<>())) {
                        System.out.println("Test: " + test);
                    }

                    for (String test : context.getPart("test2").flatMap(context::<String>getValue).orElse(new ArrayList<>())) {
                        System.out.println("Test 2: " + test);
                    }

                    for (Character test : context.getPart("flags").flatMap(context::<Character>getValue).orElse(new ArrayList<>())) {
                        System.out.println("Flag: " + test);
                    }

                    return true;
                };
            }
        });

        manager.execute(new NamespaceImpl(), "test -f -a ola que hace");
    }
}
