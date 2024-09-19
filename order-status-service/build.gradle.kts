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
    //id("io.freefair.lombok")
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
dependencies {
    implementation(project(":common"))
    implementation(project(":test-config"))

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    checkstyle("com.thomasjensen.checkstyle.addons:checkstyle-addons:7.0.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.projectlombok:lombok:1.18.20")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.kafka:spring-kafka")
    implementation ("org.threeten:threeten-extra:1.6.0")
    errorprone("com.google.errorprone:error_prone_core:2.27.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test") // Стартовый пакет для тестов
    testImplementation("org.awaitility:awaitility") // Асинхронное тестирование
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation ("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        disableWarningsInGeneratedCode.set(true)
        disable("StringSplitter", "ImmutableEnumChecker", "FutureReturnValueIgnored", "EqualsIncompatibleType", "TruthSelfEquals")
    }
}
tasks {

    test {
        //dependsOn(checkstyleTest, checkstyleMain, pmdMain, pmdTest)
        testLogging.showStandardStreams = false // set to true for debug purposes
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

pmd {
    toolVersion = "6.55.0"
    ruleSets = listOf()
    ruleSetFiles = files("config/pmd/pmd.xml")
}
sonarqube {
    properties {
        property("sonar.projectKey", "pandamaroder_NewsService")
        property("sonar.organization", "pandamaroder")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

checkstyle {
    toolVersion = "10.16.0"
    configFile = file("${project.rootDir}/order-service/config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    maxWarnings = 0
    maxErrors = 0
}

jacoco {
    toolVersion = "0.8.12"
}
