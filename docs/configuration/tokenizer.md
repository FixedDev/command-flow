## Input Tokenizer

The input tokenizer is responsible for converting a simple input string into a
list of tokens. The tokens are then used by the parser to enter the desired
command path and either dispatch a command or get suggestions for the next
token.

The `InputTokenizer` is a functional interface that can be set to the
`CommandManager` via the `CommandManager#setInputTokenizer` method.

For example:

```java
// create a tokenizer
InputTokenizer tokenizer = ...;

// set the tokenizer
CommandManager manager = ...;
manager.setInputTokenizer(tokenizer);
```

### Default Implementations

`command-flow` provides two default implementations of the `InputTokenizer`
interface, `StringSpaceTokenizer` and `QuotedSpaceTokenizer`:

- `StringSpaceTokenizer` - This tokenizer splits the input string by spaces
  and returns the resulting tokens. This is the default tokenizer used by
  `CommandManager` if no other tokenizer is set.
- `QuotedSpaceTokenizer` - This tokenizer splits the input string by spaces
  but also supports quoted strings. For example, the input string
  `hello "world of commands"` would be split into the tokens `hello` and
  `world of commands`.


