

dependencies {
    implementation(project(":common"))

    // Другие зависимости для order-status-service
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar>().configureEach {
    mainClass.set("com.example.orderservice.OrderServiceApplication")
}
