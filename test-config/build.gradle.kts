plugins {
    id("java-library")
    //id("io.freefair.lombok")
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
    //annotationProcessor("org.projectlombok:lombok")
    //compileOnly("org.projectlombok:lombok")  // !!!!!!!!!!!!!добавить бомник для аннотейшн процессора
    // Тестовые зависимости, которые будут доступны только для тестов
    implementation("org.springframework.boot:spring-boot-starter-test") // Стартовый пакет для тестов
    implementation("org.testcontainers:kafka") // Testcontainers для Kafka
    implementation("org.springframework.kafka:spring-kafka-test") // Тестирование Kafka
    implementation("org.awaitility:awaitility:4.2.0") // Асинхронное тестирование
}



