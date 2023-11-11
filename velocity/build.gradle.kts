plugins {
    id("commandflow.publishing-conventions")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":commandflow-api"))
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
}