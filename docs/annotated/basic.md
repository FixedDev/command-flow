## #1 Basic Command

Check this basic command example, with no arguments, no subcommands and no permissions,
using the annotated command creation approach:

```java
@Command(names = "test")
public class TestCommand implements CommandClass {
    
    @Command(names = "")
    public void run() {
        System.out.println("Hello World!");
    }
    
}
```

Executing `test` will print `Hello World!`