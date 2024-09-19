plugins {
    id("java-library")

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.2.6"))
    // Основные зависимости, транзитивно доступные другим модулям
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka") // Kafka через Spring
    implementation("org.threeten:threeten-extra:1.6.0") // Расширенная работа с датами и временем!!!!!
    ///annotationProcessor("org.projectlombok:lombok") ?????
    // Тестовые зависимости, которые будут доступны только для тестов
    implementation("org.springframework.boot:spring-boot-starter-test") // Стартовый пакет для тестов
    implementation("org.testcontainers:kafka") // Testcontainers для Kafka
    implementation("org.springframework.kafka:spring-kafka-test") // Тестирование Kafka
    implementation("org.awaitility:awaitility:4.2.0") // Асинхронное тестирование
     // ???????????????????
//    testImplementation("org.springframework.boot:spring-boot-starter-test") // Стартовый пакет для тестов
//    testImplementation("org.testcontainers:kafka") // Testcontainers для Kafka
//    testImplementation("org.springframework.kafka:spring-kafka-test") // Тестирование Kafka
//    testImplementation("org.awaitility:awaitility:4.2.0") // Асинхронное тестирование
}



