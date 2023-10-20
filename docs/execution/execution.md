## Command Execution

We already know how to create commands and register them, but now we need to
actually execute the commands.

> Note that, in certain platforms like Bukkit, Velocity, Discord with JDA, you will not
have to worry about executing commands since the platform-specific `CommandManager` implementation
will listen to native events and automatically execute the commands for you, awesome!

We can execute a command by calling the `CommandManager#execute` method, which will
parse and execute the command from a string command line.

For example:

<!--@formatter:off-->
```java
// create a command manager
CommandManager manager = ...;

// set up the variables passed to the command
Namespace namespace = Namespace.create();
namespace.setObject(User.class, "USER", new User("Fixed", 16));

// executes the command "greet" with an argument "Yusshu"
manager.execute(namespace, "greet Yusshu");
```
<!--@formatter:on-->

Note that a `Namespace` is given to the `execute` command, namespaces are used to
store variables that can be accessed by the command. For example, the `greet` command
can access the `USER` variable that we set in the namespace.

Here is an example where we access a previously set variable from inside the `Command`
action:

<!--@formatter:off-->
```java
// create a command manager
CommandManager manager = ...;

// create & register the command
Command command = Command.builder("test")
        .action(context -> {
            User sender = context.getObject(User.class, "SENDER");
            System.out.println("'test' command used by " + sender.getName());
        })
        .build();
manager.registerCommand(command);

// set up the variables passed to the command
Namespace namespace = Namespace.create();
namespace.setObject(User.class, "SENDER", new User("Yusshu", 18));

// executes the command "test"
manager.execute(namespace, "test");
```
<!--@formatter:on-->