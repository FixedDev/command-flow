## Installation

You can add `command-flow` to your project using [Gradle](https://gradle.org/),
[Maven](https://maven.apache.org/) or manually downloading the JAR files

### Gradle
```kotlin
repositories {
    maven("https://repo.unnamed.team/repository/unnamed-public/")
}
```
```kotlin
dependencies {
    implementation("me.fixeddev:commandflow-universal:%%REPLACE_latestReleaseOrSnapshot{me.fixeddev:commandflow-universal}%%")
}
```

### Maven
```xml
<repository>
  <id>unnamed-public</id>
  <url>https://repo.unnamed.team/repository/unnamed-public/</url>
</repository>
```
```xml
<dependency>
  <groupId>me.fixeddev</groupId>
  <artifactId>commandflow-universal</artifactId>
  <version>%%REPLACE_latestReleaseOrSnapshot{me.fixeddev:commandflow-universal}%%</version>
</dependency>
```
