
plugins {
    id("base")
    id("com.github.ben-manes.versions") version "0.51.0"
}

description = "Kafka with Spring Boot Basics "

allprojects {
    group = "com.github.pandamaroder"
    version = "0.1.0"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}


fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}





