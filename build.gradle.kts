import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.4"
    id("pmd")
    id("jacoco")
    id("org.sonarqube") version "4.0.0.2929"
    id("checkstyle")
    id("net.ltgt.errorprone") version "3.1.0"
    //id("com.github.spotbugs")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

// Конфигурация для всех модулей
configure(subprojects.filter { it.name != "common" }) {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "pmd")
    apply(plugin = "jacoco")
    apply(plugin = "checkstyle")
    apply(plugin = "net.ltgt.errorprone")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-web")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.testcontainers:junit-jupiter")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.kafka:spring-kafka")

        testImplementation("org.testcontainers:zookeeper:1.18.3")
        testImplementation("org.testcontainers:kafka:1.18.3")

        errorprone("com.google.errorprone:error_prone_core:2.27.1")
    }

    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar>().configureEach {
        when (project.name) {
            "order-service" -> mainClass.set("OrderServiceApplication")
            "order-status-service" -> mainClass.set("OrderStatusServiceApplication")
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.errorprone {
            disableWarningsInGeneratedCode.set(true)
            disable(
                "StringSplitter",
                "ImmutableEnumChecker",
                "FutureReturnValueIgnored",
                "EqualsIncompatibleType",
                "TruthSelfEquals"
            )
        }
    }

    tasks {
        test {
            dependsOn(checkstyleTest, checkstyleMain, pmdMain, pmdTest)
            testLogging.showStandardStreams = false
            useJUnitPlatform()
            finalizedBy(jacocoTestReport, jacocoTestCoverageVerification)
        }

        jacocoTestReport {
            dependsOn(test)
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
        }

        jacocoTestCoverageVerification {
            dependsOn(jacocoTestReport)
        }
    }

    sonarqube {
        properties {
            property("sonar.projectKey", "pandamaroder_NewsService")
            property("sonar.organization", "pandamaroder")
            property("sonar.host.url", "https://sonarcloud.io")
        }
    }

        pmd {
            toolVersion = "6.55.0"
            ruleSets = listOf()
            ruleSetFiles = files("config/pmd/pmd.xml")
        }

        checkstyle {
            toolVersion = "10.16.0"
            configFile = file("config/checkstyle/checkstyle.xml")
            isIgnoreFailures = false
            maxWarnings = 0
            maxErrors = 0
        }

        jacoco {
            toolVersion = "0.8.12"
        }
    }




