plugins {
    id("commandflow.publishing-conventions")
}

repositories {
    maven("https://libraries.minecraft.net/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    api(project(":commandflow-api"))
    api(project(":commandflow-bukkit")) {
        exclude(group = "org.spigotmc", module = "spigot-api")
    }
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    api("me.lucko:commodore:1.10")
}