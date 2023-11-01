## Bukkit

`command-flow` provides a Bukkit implementation of the `CommandManager` interface and
some other common interfaces. These implementations are provided by the `commandflow-bukkit`
subproject that can be included in your project by doing the following:

**Gradle:**
```kotlin
dependencies {
    // ...
    implementation("team.unnamed:commandflow-bukkit:%%REPLACE_latestReleaseOrSnapshot{team.unnamed:commandflow-bukkit}%%")
}
```

**Maven:**
```xml
<dependencies>
    <!-- ... -->
    <dependency>
        <groupId>team.unnamed</groupId>
        <artifactId>commandflow-bukkit</artifactId>
        <version>%%REPLACE_latestReleaseOrSnapshot{team.unnamed:commandflow-bukkit}%%</version>
    </dependency>
</dependencies>
```

### Usage

The `commandflow-bukkit` subproject consists on providing the following classes:
- `CommandManager` -> `BukkitCommandManager`: Bukkit's CommandManager implementation
- `Authorizer` -> `BukkitAuthorizer`: Checks permissions using `CommandSender#hasPermission` 
- `Module` -> `BukkitModule`: Provides part factories for `CommandSender`, `GameMode`,
`OfflinePlayer`, `Player`, `@Sender Player`, `World`
- `@Sender`, `@PlayerOrSource`, `@Exact` extra annotations

### Example

```java
@Command(names = "gamemode", permission = "myplugin.gamemode")
class GameModeCommand implements CommandClass {
    
    @Command(names = "")
    public void run(@Sender Player sender, GameMode gameMode, @OptArg Player target) {
        if (target == null) {
            sender.setGameMode(gameMode);
        } else {
            target.setGameMode(gameMode);
        }
        sender.sendMessage("Success!");
    }
    
}

class MyPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        registerCommands();
        
        // now you can use '/gamemode <gamemode> [target]' in game!
    }
    
    private void registerCommands() {
        CommandManager manager = new BukkitCommandManager("myplugin"); // <-- your plugin name
        
        // annotated!
        PartInjector injector = PartInjector.create();
        injector.install(new DefaultsModule());
        injector.install(new BukkitModule()); // <-- BukkitModule!
        AnnotatedCommandTreeBuilder builder = AnnotatedCommandTreeBuilder.create(injector);
        
        // register
        manager.registerCommands(builder.fromClass(new GameModeCommand()));
    }
    
}
```