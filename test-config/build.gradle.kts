plugins {
    id("java-library") // Для создания библиотечного Java модуля
}

dependencies {
    //spring - boot dependencies 3/2/6 boom
    // Основные зависимости, транзитивно доступные другим модулям
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka") // Kafka через Spring
    implementation("org.threeten:threeten-extra:1.6.0") // Расширенная работа с датами и временем

    // Тестовые зависимости, которые будут доступны только для тестов
    implementation("org.springframework.boot:spring-boot-starter-test") // Стартовый пакет для тестов
    implementation("org.testcontainers:kafka:1.18.3") // Testcontainers для Kafka
    implementation("org.springframework.kafka:spring-kafka-test") // Тестирование Kafka
    implementation("org.awaitility:awaitility") // Асинхронное тестирование
}



