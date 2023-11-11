rootProject.name = "commandflow"

includePrefixed("api")
includePrefixed("brigadier")
includePrefixed("bukkit")
includePrefixed("bungee")
includePrefixed("discord")
includePrefixed("velocity")

fun includePrefixed(name: String) {
    val artifactName = name.replace(':', '-')
    val dirName = name.replace(':', '/')

    include("commandflow-$artifactName")
    project(":commandflow-$artifactName").projectDir = file(dirName)
}