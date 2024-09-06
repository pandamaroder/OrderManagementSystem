

dependencies {
    implementation(project(":common"))
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar>().configureEach {
    mainClass.set("com.example.orderstatusservice.OrderStatusServiceApplication")
}
