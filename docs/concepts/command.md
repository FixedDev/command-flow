## Command

Commands are the most fundamental component of the framework. It contains all the
information related to a command, included but not limited to name, aliases, permission,
parts, etc.

Commands are created using the `Command.builder(String)` method, which returns an
`Command.Builder` instance where you can set all the information of the command.

Example:
<!--@formatter:off-->
```java
Command command = Command.builder("Test")
        .action(context -> {
            System.out.println("Hi!");
        })
        .build();
```
<!--@formatter:on-->