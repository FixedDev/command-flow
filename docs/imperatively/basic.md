## #1 Basic Command

Check this basic command example, with no arguments, no subcommands and no permissions,
using the imperative command creation approach:

<!--@formatter:off-->
```java
Command testCommand = Command.builder("test")
        .action(context -> {
            System.out.println("Hello World!");
        })
        .build();
```
<!--@formatter:on-->

Executing `test` will print `Hello World!`