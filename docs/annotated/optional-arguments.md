## #4 With Optional Args

This example shows how to create a command with optional arguments
with the annotated approach:

```java
@Command(names = "greet")
public class GreetingCommand implements CommandClass {
    
    @Command(names = "")
    public void run(String name, @OptArg("Mr.") String title) {
        System.out.println("Hello, " + title + " " + name + "!");
    }
    
}
```

The `@OptArg` annotation is used to mark an argument as optional, and
it accepts a default value as a parameter, which will be used if the
argument is not present in the input.

- Executing `greet John` will print `Hello, Mr. John!`
- Executing `greet John Dr.` will print `Hello, Dr. John!`
- Executing `greet John Mr.` will print `Hello, Mr. John!`