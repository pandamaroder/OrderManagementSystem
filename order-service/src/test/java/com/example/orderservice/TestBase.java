package com.example.orderservice;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.threeten.extra.MutableClock;

import java.time.*;

@SuppressWarnings("PMD.ExcessiveImports")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestBase.CustomClockConfiguration.class, initializers = KafkaInitializer.class)
public abstract class TestBase {


    static final LocalDateTime BEFORE_MILLENNIUM = LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59);

    @Autowired
    protected Clock clock;

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected MutableClock mutableClock;

    @LocalServerPort
    protected int port;


    static Instant getTestInstant() {
        return BEFORE_MILLENNIUM.toInstant(ZoneOffset.UTC);
    }


    @TestConfiguration
    static class CustomClockConfiguration {

        @Bean
        public MutableClock mutableClock() {
            return MutableClock.of(getTestInstant(), ZoneOffset.UTC);
        }

        @Bean
        public LocalDateTime fixedDateTime() {
            return LocalDateTime.ofInstant(getTestInstant(), ZoneOffset.UTC);
        }

        @Bean
        @Primary
        public Clock fixedClock(@Nonnull final MutableClock mutableClock) {
            return mutableClock;
        }
    }
}
