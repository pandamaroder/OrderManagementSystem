package com.example.orderservice;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;


public class KafkaConsumerUtils {



    public static KafkaMessageListenerContainer<String, String> setUpKafkaConsumer(
             final KafkaProperties kafkaProperties,
             final BlockingQueue<ConsumerRecord<String, String>> consumerRecords) {
        final var containerProperties = new ContainerProperties(kafkaProperties.getTemplate().getDefaultTopic());
        final Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(KafkaInitializer.getBootstrapSevers(), "test-group", "false");
        //consumerProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
                                   
        //consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        final var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProperties);
        final var container = new KafkaMessageListenerContainer<>(consumer, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) consumerRecords::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);
        return container;
    }
}
