## SubCommand Instantiation

Sometimes we have a command that has subcommands declared with the `@SubCommandClasses`
annotation. In this case, the command framework needs to know how to instantiate the
subcommands.

We can specify how to instantiate the subcommands by implementing the `SubCommandInstanceCreator`
interface.

### The `SubCommandInstanceCreator` interface

Functional interface that has a single method `createInstance(Class<? extends CommandClass>, CommandClass)`,
that receives the subcommand class and its parent instance, and returns the sub command instance.

If we do not specify one, the annotated command tree builder will instantiate the subcommand
using Reflection, by looking for constructor that accepts the parent's class as the single
parameter an empty constructor.

The `SubCommandInstanceCreator` is set to the `AnnotatedCommandTreeBuilder` when instantiating
it, for example:

<!--@formatter:off-->
```java
// will use the default SubCommandInstanceCreator
AnnotatedCommandTreeBuilder commandBuilder = AnnotatedCommandTreeBuilder.create(partInjector);

// will use a custom SubCommandInstanceCreator
AnnotatedCommandTreeBuilder commandBuilder = AnnotatedCommandTreeBuilder.create(
        partInjector,
        new MyCustomSubCommandInstanceCreator()
);
```
<!--@formatter:on-->

### Common Implementations

It is common for `command-flow` to be used along with dependency injection frameworks, so
you can just create a `SubCommandInstanceCreator` that does the following:

**For [Google's Guice](https://github.com/google/guice) or [Unnamed Team's inject](https://github.com/unnamed/inject):**
```java
// obtain the Injector instance
Injector injector = ...;

// create the SubCommandInstanceCreator
SubCommandInstanceCreator subCommandCreator = (clazz, parent) -> injector.getInstance(clazz);

// now set it when creating an AnnotatedCommandTreeBuilder
AnnotatedCommandTreeBuilder commandBuilder = AnnotatedCommandTreeBuilder.create(
        partInjector,
        subCommandCreator
);
```