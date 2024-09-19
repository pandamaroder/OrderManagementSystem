package com.example.orderservice;


import jakarta.annotation.Nonnull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    private static final DockerImageName IMAGE_NAME = DockerImageName.parse("confluentinc/cp-kafka:7.6.2");

    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(IMAGE_NAME);

    @Nonnull
    public static String getBootstrapSevers() {
        return KAFKA_CONTAINER.getBootstrapServers();
    }

    @Override
    public void initialize(@Nonnull final ConfigurableApplicationContext applicationContext) {
        KAFKA_CONTAINER.start();
        TestPropertyValues.of(
                "spring.kafka.bootstrap-servers=" + KAFKA_CONTAINER.getBootstrapServers()
        ).applyTo(applicationContext.getEnvironment());
    }
}