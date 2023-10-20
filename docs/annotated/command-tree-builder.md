## Command Builder

The `AnnotatedCommandTreeBuilder` is the interface responsible for building `Command`
instances from annotated commands and classes.

### Creating an AnnotatedCommandTreeBuilder

To create a `AnnotatedCommandTreeBuilder` we must create a `PartInjector` first and
*(optionally)* a `SubCommandInstanceCreator`. More about this in the next pages.

```java
PartInjector injector = ...;
SubCommandInstanceCreator instanceCreator = ...;

AnnotatedCommandTreeBuilder builder = AnnotatedCommandTreeBuilder.create(injector, instanceCreator);
```

### Building a Command

Now that we have a `AnnotatedCommandTreeBuilder` we can build a command (or multiple commands).
To do this we must call the `fromClass` method with the class of the command we want to build.

Note that this method returns a list of commands, since a single class may contain multiple
root commands on it.

```java
// create the builder
AnnotatedCommandTreeBuilder builder = ...;

// build the Command list from our class
List<Command> commands = builder.fromClass(MyCommand.class);

// now we can register them using the CommandManager#registerCommands convenience method
commandManager.registerCommands(commands);
```