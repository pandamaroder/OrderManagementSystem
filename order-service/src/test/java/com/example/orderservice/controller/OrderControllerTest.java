package com.example.orderservice.controller;

import com.example.common.OrderEvent;
import com.example.orderservice.support.KafkaConsumerUtils;
import com.example.orderservice.support.TestBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerTest extends TestBase {

    private final BlockingQueue<ConsumerRecord<String, String>> consumerRecords = new LinkedBlockingQueue<>();
    private KafkaMessageListenerContainer<String, String> container;
    @Autowired
    private KafkaProperties kafkaProperties;

    @BeforeAll
    void setUpKafkaConsumer() {
        container = KafkaConsumerUtils.setUpKafkaConsumer(kafkaProperties, consumerRecords);
    }

    @AfterAll
    void tearDownKafkaConsumer() {
        if (container != null) {
            container.stop();
            container = null;
        }
    }


    @Test
    void testSentMessageAndReadFromTopic(final CapturedOutput output) throws InterruptedException {
        OrderEvent orderEvent = new OrderEvent("product", 1);
        final var result = webTestClient.post()
                .uri("/order")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderEvent)
                .exchange()
                .expectStatus().isOk();
        assertThat(output.getAll())
                .contains("Received message:")
                .contains("[Producer clientId=order-producer-1] Cluster ID:")
                .contains("Key: null; Partition: 0; Topic: order-topic");

        final var received = consumerRecords.poll(10, TimeUnit.SECONDS);
        assertThat(received).isNotNull();
        assertThat(received.value()).startsWith("{\"product\":");
        final Header[] headers = received.headers().toArray();
        final var headerNames = Arrays.stream(headers)
                .map(Header::key)
                .toList();
        assertThat(headerNames)
                .hasSize(1)
                .containsExactlyInAnyOrder("__TypeId__");


       /* Awaitility
                .await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(500L))
                .until(() -> countRecordsInTable() >= 1L);
        assertThat(output.getAll())
                .contains("Received record: " + received.value() + " with traceId " + traceId);
        final String messageFromDb = jdbcTemplate.queryForObject("select message from otel_demo.storage where trace_id = :traceId",
                Map.of("traceId", traceId), String.class);
        assertThat(messageFromDb)
                .isEqualTo(received.value());*/
    }


}
