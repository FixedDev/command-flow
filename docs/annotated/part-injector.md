## Part Injector

The `PartInjector` is a registry which holds the registered PartFactories
and PartModifiers.

- A `PartFactory` is a class that provides a way to create an specific type of part.
- A `PartModifier` is like a PartFactory, the difference is that it may wrap the original part  
  or modify it instead of creating a new one.

### Creating a PartInjector

Creating a `PartInjector` is simple, just do the following:

```java
PartInjector partInjector = PartInjector.create();

// install the default bindings!
// parts for native and core types like String, Boolean, Double, Float, Integer,
// Text(String), ArgumentStack, CommandContext, also modifiers like LimitModifier,
// OptionalModifier, ValueFlagModifier
partInjector.install(new DefaultsModule());
```

### Platform Specific

Some of the platform-specific subprojects include a `Module` for the `PartInjector`
that can be easily installed to obtain the default bindings for that platform.

For example, for Bukkit (`commandflow-bukkit` subproject):
```java
// install bindings for the default bindings for Bukkit, such as
//  CommandSender, OfflinePlayer, Player, World, GameMode and @Sender Player
partInjector.install(new BukkitModule());
```