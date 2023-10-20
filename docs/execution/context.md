## Command Context

As we have seen in [Command Execution](./execution.md), we can access previously set
variables in the execution namespace, from a command action, awesome!

But now, we are going to explore more about the command context, and how we can
access more information about the command execution.

### Information

The `CommandContext` contains information about the command execution, such as:
- A reference to the command *(it may be root or sub command)* being executed
- A reference to the root command being executed
- The execution path
- The input tokens or arguments
- The used labels (which name/alias was used for every command and sub-command)
- A command part value
- All the data from the execution namespace

### Example

<!--@formatter:off-->
```java
// create the command manager
CommandManager manager = ...;

// create & register our command
Command command = Command.builder("test")
        .addAlias("testalias")
        .action(context -> {
            // Here we have an action of the command, and here we can use the context for this command
            // The CommandContext is the result of parsing the arguments of a command.
            // It also extends Namespace, so you can use the Namespace methods on it.
    
            // The labels are the names for every command/subcommand executed.
            // This is the name of the last subcommand/command
            String label = context.getLabels().get(context.getLabels().size() - 1);
            
            System.out.println("Label: " + label);
        })
        .build();
manager.registerCommand(command);

// execute the command
manager.execute(Namespace.create(), "testalias"); // Will print 'Label: testalias'
manager.execute(Namespace.create(), "test"); // Will print 'Label: test'
```
<!--@formatter:on-->
