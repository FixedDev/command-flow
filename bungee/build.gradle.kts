plugins {
    id("commandflow.publishing-conventions")
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    api(project(":commandflow-api"))
    compileOnly("net.md-5:bungeecord-api:1.15-SNAPSHOT")
}