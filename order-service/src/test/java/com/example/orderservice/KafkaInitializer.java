package com.example.orderservice;


import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final GenericContainer<?> ZOOKEEPER_CONTAINER = new GenericContainer<>(DockerImageName.parse("confluentinc/cp-zookeeper:7.6.2"))
            .withExposedPorts(2181);
     private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.2"));

    @Override
    public void initialize(ConfigurableApplicationContext context) {

        ZOOKEEPER_CONTAINER.start();


        KAFKA_CONTAINER.withEnv("KAFKA_ZOOKEEPER_CONNECT", ZOOKEEPER_CONTAINER.getHost() + ":" + ZOOKEEPER_CONTAINER.getMappedPort(2181));
        KAFKA_CONTAINER.start();

        // Настройка параметров для подключения к Kafka
        TestPropertyValues.of(
                "spring.kafka.bootstrap-servers=" + KAFKA_CONTAINER.getBootstrapServers()
        ).applyTo(context.getEnvironment());
    }
}
