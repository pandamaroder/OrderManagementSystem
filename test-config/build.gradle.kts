plugins {
    id("java-library") // Для создания библиотечного Java модуля
}

dependencies {
    // Основные зависимости, транзитивно доступные другим модулям
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.kafka:spring-kafka") // Kafka через Spring
    api("org.threeten:threeten-extra:1.6.0") // Расширенная работа с датами и временем

    // Тестовые зависимости, которые будут доступны только для тестов
    testImplementation("org.springframework.boot:spring-boot-starter-test") // Стартовый пакет для тестов
    testImplementation("org.testcontainers:kafka:1.18.3") // Testcontainers для Kafka
    testImplementation("org.springframework.kafka:spring-kafka-test") // Тестирование Kafka
    testImplementation("org.awaitility:awaitility") // Асинхронное тестирование
}



