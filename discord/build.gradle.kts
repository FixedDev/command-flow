plugins {
    id("commandflow.publishing-conventions")
}

repositories {
    maven("https://jcenter.bintray.com/")
}

dependencies {
    api("net.dv8tion:JDA:4.2.0_208")
    api(project(":commandflow-api"))
}