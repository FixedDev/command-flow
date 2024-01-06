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
    implementation("team.unnamed:commandflow-api:%%REPLACE_latestReleaseOrSnapshot{team.unnamed:commandflow-api}%%")
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
  <groupId>team.unnamed</groupId>
  <artifactId>commandflow-api</artifactId>
  <version>%%REPLACE_latestReleaseOrSnapshot{team.unnamed:commandflow-api}%%</version>
</dependency>
```
