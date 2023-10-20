## Authorizer

`command-flow` has a functional interface `Authorizer` with a single method responsible for
determining if a command should be executed.

### Platform Specific

There are some platform specific implementations of `Authorizer`, which are automatically
set if you use the specific `CommandManager` class, for example, if you instantiate the
`BukkitCommandManager`, it will already use the `BukkitAuthorizer` by default, awesome!

### Implementation

The `Authorizer` single method accepts a `Namespace` and the actual permission string,
and returns a boolean value indicating if the command should be executed or not.

Check the following implementation example, suppose we have a `User` class, and we have
previously set the sender user instance in the execution namespace *(Check page about 
Command Dispatch)*

<!--@formatter:off-->
```java
public class MyAuthorizer implements Authorizer {
    @Override
    public boolean isAuthorized(Namespace namespace, String permission) {
        User user = namespace.getObject(User.class, "USER");

        // User not set!
        if (user == null) {
            return false;
        }

        return user.hasPermission(permission);
    }
}
```
<!--@formatter:on-->

Now we can set our `Authorizer` in the `CommandManager`

<!--@formatter:off-->
```java
CommandManager commandManager = ...;

commandManager.setAuthorizer(new MyAuthorizer());
```
<!--@formatter:on-->

### Permission String

The permission string is an optional attribute for `Command` instances, set during
its instantiation, for example:

Using builders:
<!--@formatter:off-->
```java
Command command = Command.builder("test")
        .permission("this.is.the.permission.string")
        .action(context -> {
            System.out.println("Hello World!");
        })
        .build();
```
<!--@formatter:on-->

Or annotations:
<!--@formatter:off-->
```java
@Command(names = "test", permission = "this.is.the.permission.string")
public class TestCommand implements CommandClass {
    ...
}
```
<!--@formatter:on-->