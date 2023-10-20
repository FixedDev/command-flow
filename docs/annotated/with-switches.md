## #6 With Switches

This example shows how to create a command with "switch" arguments
(boolean flags) with the imperative approach, note that switch arguments
values are always present. Presence of the switch argument indicates `true`,
and its absence indicates `false`.

<!--@formatter:off-->
```java
@Command(names = "test")
public class TestCommand implements CommandClass {
    
    @Command(names = "")
    public void run(String name, @Switch("g") boolean goodBye) {
        if (goodBye) {
            System.out.println("Goodbye " + name);
            return;
        }
        System.out.println("Hi " + name);
    }
    
}
```
<!--@formatter:on-->

- Executing `test Fixed` will print `Hi Fixed`
- Executing `test -g Fixed` will print `Goodbye Fixed`
- Executing `test Fixed -g` will print `Goodbye Fixed`