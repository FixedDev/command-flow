## #5 With Permission

This example shows how to create a command with a permission requirement
with the annotated approach:

```java
@Command(names = "greet", permission = "myperm.command.greet")
public class GreetingCommand implements CommandClass {
    
    @Command(names = "")
    public void run(String name) {
        System.out.println("Hello, " + name + "!");
    }
    
}
```

The `@Command` annotation accepts a `permission` parameter, which is
used to specify the permission required to execute the command.

Check the [Authorizer page](../configuration/authorizer.md) for more information
on how to specify how the `CommandManager` should check for permissions. 