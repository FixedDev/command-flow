## #2 With Arguments

Check this command example with a single argument:

```java
@Command(names = "hello")
public class TestCommand implements CommandClass {
    
    @Command(names = "")
    public void run(String name) {
        System.out.println("Hi " + name);
    }
    
}
```

In this example:
- Executing `hello yusshu` will print `Hi yusshu`
- Executing `hello Fixed` will print `Hi Fixed`
- Executing `hello` will result in an error