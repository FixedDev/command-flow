## Command Manager

The CommandManager manages the command registration, parsing and execution. Also provides
a way to obtain command suggestions *(which can be used for tab-completion in programs like
CLI applications or Minecraft plugins)* for a given input.

### Creating a Command Manager

Depending on your platform, you may want to use a different implementation of the
`CommandManager`. Check your [platform](../platforms/platforms.md)'s documentation
to get more information.

However, there is a default implementation called `SimpleCommandManager` which can be
used in any platform, but it doesn't provide any implementation-specific features.

<!--@formatter:off-->
```java
CommandManager commandManager = new SimpleCommandManager();
```
<!--@formatter:on-->

### Registering commands

To register a command, you need to create a `Command` object and register it using
the `CommandManager.registerCommand(Command)` method.

<!--@formatter:off-->
```java
// create CommandManager
CommandManager manager = ...;

// create Command using builder
Command command = Command.builder("test")
        .description("A test command")
        .action(context -> {
            System.out.println("Hello world!");
        })
        .build();

// register the command
manager.registerCommand(command);
```
<!--@formatter:on-->