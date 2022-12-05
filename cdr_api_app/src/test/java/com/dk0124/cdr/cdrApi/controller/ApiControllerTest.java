package com.dk0124.cdr.cdrApi.controller;

import net.bytebuddy.implementation.bind.annotation.Empty;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("container")
@Testcontainers
class ApiControllerTest {
    @Container
    static PostgreSQLContainer container
            = new PostgreSQLContainer().withDatabaseName("testDb");

    @Test
    public void empty(){assertNotNull(container);}


}