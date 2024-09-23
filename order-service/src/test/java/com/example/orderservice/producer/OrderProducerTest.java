package com.example.orderservice.producer;

import com.example.common.OrderEvent;
import com.example.orderservice.KafkaConsumerUtils;
import com.example.orderservice.TestBase;
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
public class OrderProducerTest extends TestBase {

    private final BlockingQueue<ConsumerRecord<String, String>> consumerRecords = new LinkedBlockingQueue<>();

    private KafkaMessageListenerContainer<String, String> container;
    @Autowired
    private KafkaProperties kafkaProperties;

    //Этот контейнер прослушивает Kafka-топик и автоматически передает все полученные
    // сообщения в очередь consumerRecords, из которой тест может забирать сообщения для проверки.
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
    void readFromTopicSentOrderEventProducerTest(final CapturedOutput output) throws InterruptedException {
        final OrderEvent orderEvent = new OrderEvent("product", 1);
        webTestClient.post()
                .uri("/order")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderEvent)
                .exchange()
                .expectStatus().isOk();
        assertThat(output.getOut())
                .contains("[Producer clientId=order-producer-1] Cluster ID:");
        //проверяем что в топике есть сообщение
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

    }
}
