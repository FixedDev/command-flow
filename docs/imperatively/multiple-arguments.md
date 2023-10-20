## #3 Command with Multiple Args

This example shows how to create a command with multiple arguments
using the imperative approach:

<!--@formatter:off-->
```java
// Here we create a StringPart(String argument) with the name "name"
CommandPart name = string("name");

// Here we create a BooleanPart(boolean argument) with the name "formal"
CommandPart formalPart = booleanPart("formal");

Command greetCommand = Command.builder("greet")
        // Here we add a part into the Command
        .addPart(name)
        // You can add multiple parts into a command
        // They will be added into a main part called SequentialCommandPart
        // Which will call every part of the Command in a sequence to parse every argument.
        .addPart(formalPart)
        .action(context -> {
            // The values for a Part(argument) may not be present, check if they're
            // before trying to use them
            boolean formal = context.<Boolean>getValue(formalPart).orElse(false);
            context.<String>getValue(name).ifPresent(s -> {
                if (formal) {
                    System.out.println("Hello, " + s + "!");
                } else {
                    System.out.println("Hi, " + s + "!");
                }
            });
        })
        .build();
```
<!--@formatter:on-->

- Executing `greet John false` will print `Hi, John!`
- Executing `greet John true` will print `Hello, John!`
- Executing `greet John` will print `Hi, John!`
- Executing `greet` will print nothing