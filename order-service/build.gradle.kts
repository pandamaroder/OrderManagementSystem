
plugins {
    id("java")
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.4"
    id("pmd")
    id("jacoco")
    id("org.sonarqube") version "4.0.0.2929"


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

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.projectlombok:lombok:1.18.20")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.kafka:spring-kafka")
    implementation ("org.threeten:threeten-extra:1.6.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") // Стартовый пакет для тестов
    testImplementation("org.testcontainers:kafka:1.18.3") // Testcontainers для Kafka
    testImplementation("org.springframework.kafka:spring-kafka-test") // Тестирование Kafka
    testImplementation("org.awaitility:awaitility") // Асинхронное тестирование
}



