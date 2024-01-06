## #2 With Arguments

Check this command example with a single argument:

<!--@formatter:off-->
```java


//...

// create an (argument) part of type string, with the name 'name'
CommandPart nameArg = string("name");

// create the command
Command helloCommand = Command.builder("hello")
        .addPart(nameArg)
        .action(context -> {
            // get the value of the name argument and print 'Hi <name>'
            context.<String>getValue(nameArg).ifPresent(name -> {
                System.out.println("Hi " + name);
            });
        })
        .build();
```
<!--@formatter:on-->

In this example:
- Executing `hello yusshu` will print `Hi yusshu`
- Executing `hello Fixed` will print `Hi Fixed`
- Executing `hello` will print nothing