## Getting Started

Welcome to the `command-flow` documentation

`command-flow` is a flexible and platform-agnostic command framework
for Java. With this framework you can *imperatively* create command trees
using builders or *declaratively* using annotated classes and methods.

See the following example:
```java
@Command("test")
public class TestCommand implements CommandClass {
    
    @Command("hello")
    public void hello(CommandSender sender) {
        sender.sendMessage("Hello World!");
    }
    
}
```

### Features
- Easily create commands using builders or annotations