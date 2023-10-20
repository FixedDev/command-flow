## #3 Command with Multiple Args

This example shows how to create a command with multiple arguments
using the annotated approach:

```java
@Command(names = "greet")
public class GreetingCommand implements CommandClass {
    
    @Command(names = "")
    public void run(String name, boolean formal) {
        if (formal) {
            System.out.println("Hello, " + name + "!");
        } else {
            System.out.println("Hi, " + name + "!");
        }
    }
    
}
```

- Executing `greet John false` will print `Hi, John!`
- Executing `greet John true` will print `Hello, John!`
- Executing `greet John` will result in a usage error
- Executing `greet` will result in a usage error