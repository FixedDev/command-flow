## Annotated Commands

`command-flow` allows developers to create full command trees in a declarative way
using annotated classes and methods.

The annotated command API is just an alternative way to create `Command` instances,
we use this instead of the classic `Command.builder(String)` method.

### Comparison

You can make a side-by-side comparison of the two approaches in the following table
and links:

| Imperative                                                               | Annotated                                                             |
|--------------------------------------------------------------------------|-----------------------------------------------------------------------|
| [Basic Command Creation](../imperatively/basic.md)                       | [Basic Command Creation](../annotated/basic.md)                       |
| [Command with single Argument](../imperatively/argument.md)              | [Command with single Argument](../annotated/argument.md)              |
| [Command with multiple Arguments](../imperatively/multiple-arguments.md) | [Command with multiple Arguments](../annotated/multiple-arguments.md) |
| [Command with optional Arguments](../imperatively/optional-arguments.md) | [Command with optional Arguments](../annotated/optional-arguments.md) |
| [Command with Permission](../imperatively/with-permission.md)            | [Command with Permission](../annotated/with-permission.md)            |
| [Command with Switch Arguments](../imperatively/with-switches.md)        | [Command with Switch Arguments](../annotated/with-switches.md)        |
| [Command with Value Flags](../imperatively/with-value-flags.md)          | [Command with Value Flags](../annotated/with-value-flags.md)          |

### Elements of the Annotated Command API

The annotated command API is composed of:
- The `@Command` annotation and others.
- The `AnnotatedCommandTreeBuilder` interface