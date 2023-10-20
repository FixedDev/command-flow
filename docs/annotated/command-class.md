## Command Class

The annotated command API has a `CommandClass` interface used to mark a class as a container
of commands or a command itself.

To declare the commands, we use annotations:

<!--@formatter:off-->
```java
@Command(names = "teleport")
public class TeleportCommand implements CommandClass {
    
    @Command(names = "") // empty name indicates the root command
    public void run(
            @Sender Player sender, // the sender player
            Player target          // the target player (argument)
    ) {
        sender.teleport(target);
    } 
    
}
```
<!--@formatter:on-->

In this example we have created a `/teleport` command that takes a player name
as an argument *(command-flow will parse it and find the right Player)* and
teleports the sender to the target player.

The `@Command` annotation is used to mark a method as a command, it can be used
on methods inside a `CommandClass` or on the class itself.

See this other example:

<!--@formatter:off-->
```java
@Command(names = { "friends", "friend", "f", "fr" }) // name, ...aliases
public class FriendsCommand implements CommandClass {
    
    @Command(names = "add")
    public void add(@Sender Player sender, Player target) {
        // add 'target' to 'sender' friend list
    }
    
    @Command(names = "remove")
    public void remove(@Sender Player sender, Player target) {
        // remove 'target' from 'sender' friend list
    }
    
    @Command(names = "list")
    public void list(@Sender Player sender) {
        // list all 'sender' friends and show
    }
    
    @Command(names = "broadcast")
    public void broadcast(@Sender Player sender, @Text String message) {
        // send 'message' to all the friends of 'sender'
    }
    
}
```
<!--@formatter:on-->

In this example we have created a `/friends` command with 4 subcommands:
- `/friends add <player>`: add a player to the sender friend list.
- `/friends remove <player>`: remove a player from the sender friend list.
- `/friends list`: list all the sender friends.
- `/friends broadcast <...message>`: send a message to all the sender friends.

In this new example we also see these new stuff:
- `@Command(names = {...})`: multiple names! The first one is considered the actual
command name and the rest are aliases.
- `@Text`: used to mark a parameter as a text argument, this means that it will
consume all the arguments left in the command and join them with spaces.

### Registering them

To register command classes we must convert them to `Command` first, we can do
that using the `AnnotatedCommandTreeBuilder` interface, check the next page!