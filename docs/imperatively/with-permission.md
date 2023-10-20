## #5 With Permission

This example shows how to create a command with a permission requirement
with the annotated approach:

<!--@formatter:off-->
```java
Command testUserCommand = Command.builder("test")
        // We set the permission of the test command into admin
        .permission("admin")
        .action(context -> {
            System.out.println("Hi");
        })
        .build();
```
<!--@formatter:on-->

Executing the `testUserCommand` will print `Hi` if the user has the `admin`
permission, but will result in a `NoPermissionException` if the user does not.

Check the [Authorizer page](../configuration/authorizer.md) for more information
on how to specify how the `CommandManager` should check for permissions.