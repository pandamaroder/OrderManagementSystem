package com.example.orderservice;


//import jakarta.annotation.Nonnull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.threeten.extra.MutableClock;

import java.time.*;

@SuppressWarnings("PMD.ExcessiveImports")

public abstract class TestBase {


    static final LocalDateTime BEFORE_MILLENNIUM = LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59);


}
