## #6 With Switches

This example shows how to create a command with "switch" arguments
(boolean flags) with the imperative approach, note that switch arguments
values are always present. Presence of the switch argument indicates `true`,
and its absence indicates `false`.

<!--@formatter:off-->
```java
CommandPart name = string("name");

// If the -g argument is present, then the switch value will be true, otherwise false
// It can be in any position, but the parts registered before will take priority,
// that means that if the name part is registered before the switch part, the -g only
// can be after the name
CommandPart goodByeSwitch = switchPart("goodBye", "g");

Command testUserCommand = Command.builder("test")
        .addPart(goodByeSwitch)
        .addPart(name)
        .action(context -> {
            // The value for a switch is never absent
            boolean goodBye = context.<Boolean>getValue(goodByeSwitch).get();

            context.<String>getValue(name).ifPresent(s -> {
                if (goodBye) {
                    System.out.println("Goodbye " + s);
                    return;
                }
                System.out.println("Hi " + s);
            });
        })
        .build();
```
<!--@formatter:on-->

- Executing `test Fixed` will print `Hi Fixed`
- Executing `test -g Fixed` will print `Goodbye Fixed`
- Executing `test Fixed -g` will print `Goodbye Fixed`