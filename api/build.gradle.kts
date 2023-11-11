plugins {
    id("commandflow.publishing-conventions")
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:24.0.1")

    api("net.kyori:adventure-api:4.9.1") {
        exclude(group = "org.checkerframework", module = "checker-qual")
    }
    api("net.kyori:adventure-text-serializer-legacy:4.11.0")
    api("net.kyori:adventure-text-serializer-gson:4.11.0")
    api("net.kyori:adventure-text-serializer-plain:4.11.0")
}