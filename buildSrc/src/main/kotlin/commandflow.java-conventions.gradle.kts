plugins {
    `java-library`
    id("org.cadixdev.licenser")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

license {
    header.set(rootProject.resources.text.fromFile("header.txt"))
    include("**/*.java")
    newLine.set(false)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
    javadoc {
        isFailOnError = false
    }
    test {
        useJUnitPlatform()
    }
}