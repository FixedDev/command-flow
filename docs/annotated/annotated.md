## Annotated Commands

`command-flow` allows developers to create full command trees in a declarative way
using annotated classes and methods.

The annotated command API is just an alternative way to create `Command` instances,
we use this instead of the classic `Command.builder(String)` method.

### Elements of the Annotated Command API

The annotated command API is composed of 3 elements:
- The `@Command` annotation and others.
- The `AnnotatedCommandTreeBuilder` interface