

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.4"
    id("pmd")
    id("jacoco")
    id("org.sonarqube") version "4.0.0.2929"
    id("checkstyle")

    //id("com.github.spotbugs")
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.kafka:spring-kafka")

    testImplementation("org.testcontainers:zookeeper:1.18.3")
    testImplementation("org.testcontainers:kafka:1.18.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")


    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.awaitility:awaitility")
}

springBoot {
    buildInfo()
}

