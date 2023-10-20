## #7 With Value Flags

This example shows how to create a command with value flag arguments
the main difference between a value flag and a switch is that the value flag
takes the next argument as its value, and the switch does not.

For example: `-g true`, `-p 25565`, `-n Fixed`

<!--@formatter:off-->
```java
@Command(names = "test")
public class TestCommand implements CommandClass {
    
    @Command(names = "")
    public void run(String name, @Flag("g") String greeting) {
        System.out.println(greeting + " " + name);
    }
    
}
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