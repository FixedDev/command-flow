## #4 With Optional Args

This example shows how to create a command with optional arguments
with the imperative approach:

<!--@formatter:off-->
```java
CommandPart titleValue = string("title");

// This makes the titleValue argument optional, with the default value "Mr."
CommandPart title = optional(titleValue, Collections.singletonList("Mr."));

CommandPart name = string("name");

Command greetCommand = Command.builder("greet")
        .addPart(name)
        .addPart(title)
        .action(context -> {
            String nameString = context.<String>getValue(nameValue).orElse("User");

            // Should be present every time, since it has a default value
            // If the default value is not valid for the specified part, then the argument could be absent
            String titleString = context.<String>getValue(titleValue).get();
            
            System.out.println("Hello, " + titleString + " " + nameString);
        })
        .build();
```
<!--@formatter:on-->

- Executing `greet John` will print `Hello, Mr. John!`
- Executing `greet John Dr.` will print `Hello, Dr. John!`
- Executing `greet John Mr.` will print `Hello, Mr. John!`