package com.example.orderservice.controller;

import com.example.common.OrderEvent;
import com.example.orderservice.KafkaConsumerUtils;
import com.example.orderservice.TestBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerTest extends TestBase {

    private final BlockingQueue<ConsumerRecord<String, String>> consumerRecords = new LinkedBlockingQueue<>();
    private KafkaMessageListenerContainer<String, String> container;
    @Autowired
    private KafkaProperties kafkaProperties;

    @BeforeAll
    void setUpKafkaConsumer() {
        container = KafkaConsumerUtils
            .setUpKafkaConsumer(kafkaProperties.getTemplate().getDefaultTopic(), kafkaProperties, consumerRecords);
    }

    @AfterAll
    void tearDownKafkaConsumer() {
        if (container != null && container.isRunning()) {
            container.stop();
        }
    }

    @Test
    void postOrderEndPointTest() {
        final OrderEvent orderEvent = new OrderEvent("product", 1);
        webTestClient.post().uri("/order")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(orderEvent)
            .exchange()
            .expectStatus()
            .isOk();
    }
}
