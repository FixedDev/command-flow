## #7 With Value Flags

This example shows how to create a command with value flag arguments
the main difference between a value flag and a switch is that the value flag
takes the next argument as its value, and the switch does not.

For example: `-g true`, `-p 25565`, `-n Fixed`

<!--@formatter:off-->
```java
CommandPart name = string("name");

// This part is like a switch part, the difference is that when the main "switch" is found
// the part provided in the first argument takes the parsing, consuming one or more arguments
// from the stack at that position.
CommandPart greetingValue = string("greeting");
CommandPart greetingValueFlag = valueFlag(greetingValue, "g");

Command testUserCommand = Command.builder("test")
        .addPart(greetingValueFlag)
        .addPart(name)
        .action(context -> {
            // The value for a value flag can be absent
            String greeting = context.<String>getValue(greetingValue).orElse("Hi");
            context.<String>getValue(name).ifPresent(s -> {
                System.out.println(greeting + " " + s);
            });
        })
        .build();
```
<!--@formatter:on-->

- Executing `test Fixed` will print `Hi Fixed`
- Executing `test Fixed -g GoodBye` will print `GoodBye Fixed`
- Executing `test Fixed -g Hello` will print `Hello Fixed`
- Executing `test -g Fixed` will throw a `NoMoreArguments` exception, meaning that the parsing failed
  because the `Fixed` argument was taken as the value for the flag and no argument
  is remaining for the name.
- Executing `test Fixed -g` will throw a `NoMoreArguments` exception, meaning that the parsing failed
  because the flag doesn't has any argument left to use.