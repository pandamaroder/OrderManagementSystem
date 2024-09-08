package com.example.orderservice;

//import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.utility.DockerImageName;

public class KafkaInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    // private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.2"));

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      /*  KAFKA_CONTAINER.start();

        // Применяем параметры для подключения к Kafka в среде тестов
        TestPropertyValues.of(
                "spring.kafka.bootstrap-servers=" + KAFKA_CONTAINER.getBootstrapServers()
        ).applyTo(context.getEnvironment());
    }*/
    }
}
